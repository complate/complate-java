package com.github.complate;

import com.github.complate.api.ComplateScript;
import com.github.complate.api.ComplateEngine;
import com.github.complate.api.ComplateStream;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;
import java.util.Map;
import java.util.Optional;

public final class NashornScriptingBridge implements ComplateEngine {

    private final NashornScriptEngine engine;

    private static final String POLYFILLS =
            "if(typeof global === \"undefined\") {\n" +
            "    var global = this;\n" +
            "}\n" +
            "if(typeof console === \"undefined\") {\n" +
            "    var console = { log: print, error: print };\n" +
            "}\n\n";

    public NashornScriptingBridge() {
        this.engine = createEngine();
    }

    public NashornScriptingBridge(final Map<String,Object> bindings) {
        this();
        addEngineScopeBindings(bindings);
    }

    public void invoke(final ComplateScript bundle,
                       final ComplateStream stream,
                       final String tag,
                       final Object... args) throws ScriptingException {
        final String functionName = "render";

        try (Reader reader = readerForScript(bundle)) {
            engine.eval(reader);
        } catch (IOException err) {
            throw new ScriptingException(String.format(
                    "failed to read script from resource '%s'",
                    bundle.getDescription()), err);
        } catch (ScriptException err) {
            throw extractJavaScriptError(err)
                    .map(jsError -> new ScriptingException(
                            "failed to evaluate script",
                            "filepath", functionName, err, jsError))
                    .orElseGet(() -> new ScriptingException(
                            "failed to evaluate script", "filepath",
                            functionName, err));
        }

        try {
            engine.invokeFunction(functionName, stream, tag, args);
        } catch (ScriptException | NoSuchMethodException err) {
            throw extractJavaScriptError(err)
                    .map(jsError -> new ScriptingException(
                            "failed to invoke function",
                            "filepath", functionName, err, jsError))
                    .orElseGet(() -> new ScriptingException(
                            "failed to invoke function", "filepath",
                            functionName, err));
        }
    }

    private static NashornScriptEngine createEngine() {
        final ScriptEngine engine =
                new NashornScriptEngineFactory().getScriptEngine();
        if (engine == null) {
            throw new ScriptingException(
                    "Cannot instantiate Nashorn Script Engine");
        } else {
            return (NashornScriptEngine) engine;
        }
    }

    private static Reader readerForScript(final ComplateScript scriptLocation)
            throws IOException {
        final InputStream is;
        try {
            is = scriptLocation.getInputStream();
        } catch (IOException err) {
            throw new ScriptingException(String.format(
                    "failed to initialize input stream for resource '%s'",
                    scriptLocation.getDescription()), err);
        }
        InputStream stream = prependPolyfills(is);
        final Reader isr = new InputStreamReader(stream);
        return new BufferedReader(isr);
    }

    private static InputStream prependPolyfills(InputStream is) {
        InputStream polyfillsIS = new ByteArrayInputStream(POLYFILLS.getBytes());
        return new SequenceInputStream(polyfillsIS, is);
    }

    private static Optional<String> extractJavaScriptError(final Exception err) {
        final Throwable cause = err.getCause();
        if (cause instanceof NashornException) {
            return Optional.of(cause.getMessage() + "\n" +
                    NashornException.getScriptStackString(cause));
        } else {
            return Optional.empty();
        }
    }

    private void addEngineScopeBindings(final Map<String, Object> bindings) {
        Bindings engineBindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.forEach(engineBindings::put);
    }
}
