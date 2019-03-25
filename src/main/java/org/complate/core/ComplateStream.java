package org.complate.core;

import java.io.IOException;

/**
 * This stream is used by an {@link ComplateRenderer} to render the views'
 * result into.
 *
 * @author FND
 * @author mvitz
 * @since 0.1.0
 */
public interface ComplateStream {

    /**
     * Writes the given string.
     *
     * @param string The <code>String</code> to be written
     */
    void write(String string);

    /**
     * Writes the given string and terminates the line.
     *
     * @param string The <code>String</code> to be written
     */
    void writeln(String string);

    /**
     * Flushes the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    void flush() throws IOException;
}
