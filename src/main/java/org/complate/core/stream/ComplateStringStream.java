package org.complate.core.stream;

import org.complate.core.ComplateStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * {@link ComplateStream} that captures all write calls in a {@link String}
 * which can be retrieved by calling {@link #getContent()}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateStringStream implements ComplateStream {

    private final ByteArrayOutputStream bytes;
    private final PrintStream out;

    /**
     * Constructs a new stream.
     */
    public ComplateStringStream() {
        bytes = new ByteArrayOutputStream();
        out = new PrintStream(bytes);
    }

    /**
     * Returns the current content of this stream.
     *
     * @return the current content of this stream
     */
    public String getContent() {
        return bytes.toString();
    }

    @Override
    public void write(String string) {
        out.print(string);
    }

    @Override
    public void writeln(String string) {
        out.println(string);
    }

    @Override
    public void flush() {
        out.flush();
    }
}
