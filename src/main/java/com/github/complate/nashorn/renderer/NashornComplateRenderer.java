package com.github.complate.nashorn.renderer;

import com.github.complate.core.ComplateException;
import com.github.complate.core.ComplateRenderer;
import com.github.complate.core.ComplateScript;
import com.github.complate.core.ComplateStream;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.script.Bindings;
import javax.script.ScriptException;

import com.github.complate.core.ComplateException;
import com.github.complate.core.ComplateRenderer;
import com.github.complate.core.ComplateScript;
import com.github.complate.core.ComplateStream;

import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

/**
 * {@link NashornScriptEngine} based {@link ComplateRenderer}.
 */
public final class NashornComplateRenderer implements ComplateRenderer {

    private static final String POLYFILLS =
            "if(typeof global === \"undefined\") {\n" +
            "    var global = this;\n" +
            "}\n" +
            "if(typeof console === \"undefined\") {\n" +
            "    var console = { log: print, error: print };\n" +
            "}\n\n";

    private final ThreadLocal<NashornScriptEngine> engine = new ThreadLocal<NashornScriptEngine>() {
        @Override
        protected NashornScriptEngine initialValue() {
            return initEngine();
        }
    };

    private final ComplateScript script;
    private final Map<String, ?> bindings;

    private NashornComplateRenderer(ComplateScript script, Map<String, ?> bindings) {
        this.script = script;
        this.bindings = bindings;
    }

    private NashornScriptEngine initEngine() {
        final NashornScriptEngine engine = (NashornScriptEngine) new NashornScriptEngineFactory().getScriptEngine();
        if (engine == null) {
            throw new ComplateException("Cannot instantiate Nashorn Script Engine");
        }

        final Bindings engineBindings = engine.getBindings(ENGINE_SCOPE);
        bindings.forEach(engineBindings::put);

        try {
            engine.eval(POLYFILLS);
        } catch (ScriptException e) {
            throw new ComplateException(e, "Could not evaluate polyfills");
        }

        engine.put("javax.script.filename", script.getDescription());
        try (final Reader reader = readerFor(script)) {
            engine.eval(reader);
            return engine;
        } catch (IOException e) {
            throw new ComplateException("failed to read script from resource '%s'", script.getDescription());
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e, "failed to evaluate script: %s", jsError))
                .orElseGet(() -> new ComplateException(e, "failed to evaluate script from resource '%s'", script.getDescription()));
        }
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        try {
            final Object[] args = toVarArgs(view, parameters, stream);
            engine.get().invokeFunction("render", args);
        } catch (NoSuchMethodException e) {
            throw new ComplateException(e, "could not find 'render' method in script");
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e, "failed to render: %s", jsError))
                .orElseGet(() -> new ComplateException(e, "failed to render"));
        }
    }

    private static Reader readerFor(ComplateScript script) {
        final InputStream is;
        try {
            is = script.getInputStream();
        } catch (IOException e) {
            throw new ComplateException(e, "failed to initialize input stream for resource '%s'", script.getDescription());
        }
        final Reader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    private static Optional<String> extractJavaScriptError(Exception e) {
        final Throwable cause = e.getCause();
        if (cause instanceof NashornException) {
            return Optional.of(cause.getMessage() + "\n" + getScriptStackString(cause));
        } else {
            return Optional.empty();
        }
    }

    private static Object[] toVarArgs(String view, Map<String, ?> parameters, ComplateStream stream) {
        final Object[] args = new Object[3];
        args[0] = view;
        args[1] = parameters == null ? emptyMap() : parameters;
        args[2] = stream;
        return args;
    }

    public static NashornComplateRendererBuilder nashornComplateRenderer(ComplateScript script) {
        return new NashornComplateRendererBuilder(script);
    }

    public static final class NashornComplateRendererBuilder {

        private final ComplateScript script;
        private final Map<String, Object> bindings = new HashMap<>();
        private boolean cacheScriptEvaluation = true;

        private NashornComplateRendererBuilder(ComplateScript script) {
            this.script = script;
        }

        public NashornComplateRendererBuilder withBindings(Map<String, ?> bindings) {
            this.bindings.putAll(bindings);
            return this;
        }

        public NashornComplateRendererBuilder withBinding(String name, Object value) {
            this.bindings.put(name, value);
            return this;
        }

        public NashornComplateRendererBuilder cacheScriptEvaluation(boolean cacheScriptEvaluation) {
            this.cacheScriptEvaluation = cacheScriptEvaluation;
            return this;
        }

        public ComplateRenderer build() {
            if (cacheScriptEvaluation) {
                return new NashornComplateRenderer(script, bindings);
            } else {
                return new AlwaysEvaluatingNashornComplateRenderer(script, bindings);
            }
        }
    }

    private static final class AlwaysEvaluatingNashornComplateRenderer implements ComplateRenderer {

        private final ComplateScript script;
        private final Map<String, ?> bindings;

        private AlwaysEvaluatingNashornComplateRenderer(ComplateScript script, Map<String, ?> bindings) {
            this.script = script;
            this.bindings = bindings;
        }

        @Override
        public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
            new NashornComplateRenderer(script, bindings).render(view, parameters, stream);
        }
    }
}
