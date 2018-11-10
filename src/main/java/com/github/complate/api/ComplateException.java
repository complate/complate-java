package com.github.complate.api;

import static java.lang.String.format;

public final class ComplateException extends RuntimeException {

    ComplateException(final String format, final Object... args) {
        super(format(format, args));
    }

    ComplateException(final Throwable cause,
            final String format, final Object... args) {
        super(format(format, args), cause);
    }
}
