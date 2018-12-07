package com.github.complate.core;

import java.io.IOException;

/**
 * This stream is used by complate for rendering the views.
 *
 * @author FND
 * @since 0.1.0
 */
public interface ComplateStream {

    /**
     * Writes the given string.
     *
     * @param s The <code>String</code> to be written
     */
    void write(String s);

    /**
     * Writes the given string and terminates the line.
     *
     * @param s The <code>String</code> to be written
     */
    void writeln(String s);

    /**
     * Flushes the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    void flush() throws IOException;
}
