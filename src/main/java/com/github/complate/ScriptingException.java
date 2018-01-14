package com.github.complate;

public final class ScriptingException extends RuntimeException {

    ScriptingException(final String message,
                       final String filepath,
                       final String functionName,
                       final Throwable rootCause) {
        super(String.format("%s ('%s' in '%s')",
                message, functionName, filepath), rootCause);
    }

    ScriptingException(final String message,
                       final String filepath,
                       final String functionName,
                       final Throwable rootCause,
                       final String jsError) {
        super(String.format("%s ('%s' in '%s')\n%s", message, functionName,
                filepath, jsError), rootCause);
    }

    ScriptingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    ScriptingException(final String message) {
        super(message);
    }

}