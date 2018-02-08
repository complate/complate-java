package com.github.complate.impl.io;

import com.github.complate.api.ComplateStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * {@link ComplateStream} that captures all calls to write in a {@link String}
 * which can be retrieved calling {@link #getContent()}.
 */
public final class StringComplateStream implements ComplateStream {

    private final ByteArrayOutputStream bytes;
    private final PrintStream out;

    /**
     * Constructs a new stream.
     */
    public StringComplateStream() {
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
    public void write(String s) {
        out.print(s);
    }

    @Override
    public void writeln(String s) {
        out.println(s);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
