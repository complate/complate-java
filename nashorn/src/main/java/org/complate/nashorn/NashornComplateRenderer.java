/**
 * Copyright 2019 complate.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.complate.nashorn;

import org.openjdk.nashorn.api.scripting.NashornException;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.complate.core.ComplateException;
import org.complate.core.ComplateRenderer;
import org.complate.core.ComplateSource;
import org.complate.core.ComplateStream;

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
import static org.openjdk.nashorn.api.scripting.NashornException.getScriptStackString;

/**
 * {@link NashornScriptEngine} based {@link ComplateRenderer}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class NashornComplateRenderer implements ComplateRenderer {

    private static final String POLYFILLS =
        "if(typeof global === \"undefined\") { var global = this; }\n" +
            "if(typeof console === \"undefined\") { var console = { log: print, error: print }; }\n" +
            "\n";

    private final NashornScriptEngine engine;

    public NashornComplateRenderer(ComplateSource source) {
        this(source, emptyMap());
    }

    public NashornComplateRenderer(ComplateSource source, Map<String, ?> bindings) {
        engine = createEngine(bindings);

        try {
            engine.eval(POLYFILLS);
        } catch (ScriptException e) {
            throw new ComplateException(e, "Could not evaluate nashorn polyfills");
        }

        engine.put("javax.script.filename", source.getDescription());
        try (Reader reader = readerFor(source)) {
            engine.eval(reader);
        } catch (IOException e) {
            throw new ComplateException("failed to read script from source '%s'", source.getDescription());
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e, "failed to evaluate script: %s", jsError))
                .orElseGet(() -> new ComplateException(e, "failed to evaluate script from resource '%s'", source.getDescription()));
        }
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        try {
            final Object[] args = toVarArgs(view, parameters, stream);
            engine.invokeFunction("render", args);
        } catch (NoSuchMethodException e) {
            throw new ComplateException(e, "could not find 'render' method in script");
        } catch (ScriptException e) {
            throw extractJavaScriptError(e)
                .map(jsError -> new ComplateException(e, "failed to render: %s", jsError))
                .orElseGet(() -> new ComplateException(e, "failed to render"));
        }
    }

    private static NashornScriptEngine createEngine(Map<String, ?> bindings) {
        final NashornScriptEngine engine = createEngine();

        final Bindings engineBindings = engine.getBindings(ENGINE_SCOPE);
        bindings.forEach(engineBindings::put);

        return engine;
    }

    private static NashornScriptEngine createEngine() {
        final ScriptEngine engine =
            new NashornScriptEngineFactory().getScriptEngine();
        if (engine == null) {
            throw new ComplateException("Cannot instantiate Nashorn Script Engine");
        }
        return (NashornScriptEngine) engine;
    }

    private static Reader readerFor(ComplateSource source) {
        final InputStream is;
        try {
            is = source.getInputStream();
        } catch (IOException e) {
            throw new ComplateException(e, "failed to initialize input stream for source '%s'", source.getDescription());
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
}
