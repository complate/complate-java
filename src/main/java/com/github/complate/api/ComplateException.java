package com.github.complate.api;

public final class ComplateException extends RuntimeException {

    ComplateException(final String message,
                      final String filepath,
                      final String functionName,
                      final Throwable rootCause) {
        super(String.format("%s ('%s' in '%s')",
                message, functionName, filepath), rootCause);
    }

    ComplateException(final String message,
                      final String filepath,
                      final String functionName,
                      final Throwable rootCause,
                      final String jsError) {
        super(String.format("%s ('%s' in '%s')\n%s", message, functionName,
                filepath, jsError), rootCause);
    }

    ComplateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    ComplateException(final String message) {
        super(message);
    }
}
