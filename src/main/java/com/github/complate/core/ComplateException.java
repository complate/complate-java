package com.github.complate.core;

import static java.lang.String.format;

/**
 * TODO: add documentation
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public final class ComplateException extends RuntimeException {

    /**
     * Creates a new exception with a message constructed by the given format
     * and arguments.
     *
     * @param format any format valid for usage in
     *               {@link String#format(String, Object...)}
     * @param args any arguments valid for usage in
     *             {@link String#format(String, Object...)} for the given format
     */
    public ComplateException(String format, Object... args) {
        super(format(format, args));
    }

    /**
     * Creates a new exception with the given throwable as cause and a message
     * constructed by the given format and arguments.
     *
     * @param cause the throwable that was the cause for this exception
     * @param format any format valid for usage in
     *               {@link String#format(String, Object...)}
     * @param args any arguments valid for usage in
     *             {@link String#format(String, Object...)} for the given format
     */
    public ComplateException(Throwable cause, String format, Object... args) {
        super(format(format, args), cause);
    }
}
