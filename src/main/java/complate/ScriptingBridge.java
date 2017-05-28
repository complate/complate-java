package complate;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.Invocable;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class ScriptingBridge {
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    public void invoke(String filepath, String functionName, Object... args)
            throws ScriptingException {
        try {
            engine.eval(readFile(filepath));
        } catch(ScriptException err) {
            throw ScriptingException.create("failed to evaluate script",
                    filepath, functionName, err, extractJavaScriptError(err));
        }
        Invocable invocable = (Invocable) engine;

        try {
            invocable.invokeFunction(functionName, args);
        } catch(ScriptException | NoSuchMethodException err) {
            throw ScriptingException.create("failed to invoke function",
                    filepath, functionName, err, extractJavaScriptError(err));
        }
    }

    // TODO: memoize to avoid redundant I/O
    private static FileReader readFile(String filepath) {
        try {
            return new FileReader(filepath);
        } catch(FileNotFoundException err) {
            throw new ScriptingException(String.format("failed to read script file '%s'",
                    filepath), err);
        }
    }

    private static String extractJavaScriptError(Exception err) {
        Throwable cause = err.getCause();
        return cause instanceof NashornException ?
                cause.getMessage() + "\n" + NashornException.getScriptStackString(cause) :
                null;
    }

    public static final class ScriptingException extends RuntimeException {
        private ScriptingException(String message, String filepath,
                String functionName, Throwable rootCause) {
            super(String.format("%s ('%s' in '%s')", message, functionName, filepath),
                    rootCause);
        }

        private ScriptingException(String message, String filepath,
                String functionName, Throwable rootCause, String jsError) {
            super(String.format("%s ('%s' in '%s')\n%s", message, functionName,
                    filepath, jsError), rootCause);
        }

        public ScriptingException(String message, Throwable cause) {
            super(message, cause);
        }

        public static ScriptingException create(String message, String filepath,
                String functionName, Throwable rootCause, String jsError) {
            return jsError == null ?
                    new ScriptingException(message, filepath, functionName, rootCause) :
                    new ScriptingException(message, filepath, functionName, rootCause,
                            jsError);
        }
    }
}
