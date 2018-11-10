package com.github.complate.api;

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
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static javax.script.ScriptContext.ENGINE_SCOPE;

/**
 * {@link NashornScriptEngine} based {@link ComplateBundle}.
 */
public final class NashornComplateBundle implements ComplateBundle {

    private static final String POLYFILLS =
            "if(typeof global === \"undefined\") {\n" +
            "    var global = this;\n" +
            "}\n" +
            "if(typeof console === \"undefined\") {\n" +
            "    var console = { log: print, error: print };\n" +
            "}\n\n";

    private final NashornScriptEngine engine;

    public NashornComplateBundle(final ComplateScript script) {
        this(script, emptyMap());
    }

    public NashornComplateBundle(final ComplateScript script,
            final Map<String, ?> bindings) {
        engine = createEngine(bindings);

        try {
            engine.eval(POLYFILLS);
        } catch (ScriptException e) {
            throw new ComplateException("Could not evaluate polyfills", e);
        }

        engine.put("javax.script.filename", script.getDescription());
        try (Reader reader = readerFor(script)) {
            engine.eval(reader);
        } catch (IOException e) {
            throw new ComplateException(String.format(
                "failed to read script from resource '%s'",
                script.getDescription()), e);
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e,
                    "failed to evaluate script: %s",
                    jsError))
                .orElseGet(() -> new ComplateException(e,
                    "failed to evaluate script from resource '%s'",
                    script.getDescription()));
        }
    }

    @Override
    public void render(final ComplateStream stream, final String tag,
            final Map<String, ?> parameters) throws ComplateException {
        try {
            final Object[] args = toVarArgs(stream, tag, parameters);
            engine.invokeFunction("render", args);
        } catch (NoSuchMethodException e) {
            throw new ComplateException(
                "could not find 'render' method in script", e);
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e,
                    "failed to render: %s", jsError))
                .orElseGet(() -> new ComplateException(e,
                    "failed to render"));
        }
    }

    private static NashornScriptEngine createEngine(
            final Map<String, ?> bindings) {
        final NashornScriptEngine engine = createEngine();

        final Bindings engineBindings = engine.getBindings(ENGINE_SCOPE);
        bindings.forEach(engineBindings::put);

        return engine;
    }

    private static NashornScriptEngine createEngine() {
        final ScriptEngine engine =
            new NashornScriptEngineFactory().getScriptEngine();
        if (engine == null) {
            throw new ComplateException(
                "Cannot instantiate Nashorn Script Engine");
        }
        return (NashornScriptEngine) engine;
    }

    private static Reader readerFor(final ComplateScript script) {
        final InputStream is;
        try {
            is = script.getInputStream();
        } catch (IOException e) {
            throw new ComplateException(e,
                "failed to initialize input stream for resource '%s'",
                script.getDescription());
        }
        final Reader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    private static Optional<String> extractJavaScriptError(final Exception e) {
        final Throwable cause = e.getCause();
        if (cause instanceof NashornException) {
            return Optional.of(cause.getMessage() + "\n" +
                NashornException.getScriptStackString(cause));
        } else {
            return Optional.empty();
        }
    }

    private static Object[] toVarArgs(final ComplateStream stream,
            final String tag, final Map<String, ?> parameters) {
        final Object[] args = new Object[3];
        args[0] = tag;
        args[1] = parameters == null ? emptyMap() : parameters;
        args[2] = stream;
        return args;
    }
}
