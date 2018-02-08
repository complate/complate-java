package com.github.complate.impl.io;

import com.github.complate.api.ComplateStream;

import java.io.IOException;
import java.io.PrintWriter;

import static java.util.Objects.requireNonNull;

/**
 * {@link java.io.PrintWriter} based {@link ComplateStream}.
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public final class PrintWriterComplateStream implements ComplateStream {

    private final PrintWriter writer;

    public PrintWriterComplateStream(PrintWriter writer) {
        this.writer = requireNonNull(writer, "writer must not be null");
    }

    @Override
    public void write(String s) {
        writer.print(s);
    }

    @Override
    public void writeln(String s) {
        writer.println(s);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }
}
