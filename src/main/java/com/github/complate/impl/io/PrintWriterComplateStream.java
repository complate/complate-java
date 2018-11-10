package com.github.complate.impl.io;

import com.github.complate.api.ComplateStream;

import java.io.IOException;
import java.io.PrintWriter;

import static com.github.complate.impl.io.PrintWriterComplateStream.FlushMode.ALWAYS;
import static java.util.Objects.requireNonNull;

/**
 * {@link java.io.PrintWriter} based {@link ComplateStream}.
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public final class PrintWriterComplateStream implements ComplateStream {

    private final PrintWriter writer;
    private final FlushMode flushMode;

    public PrintWriterComplateStream(PrintWriter writer) {
        this(writer, ALWAYS);
    }

    public PrintWriterComplateStream(PrintWriter writer, FlushMode flushMode) {
        this.writer = requireNonNull(writer, "writer must not be null");
        this.flushMode = requireNonNull(flushMode, "flushMode must not be null");
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
        if (flushMode.shouldFlush()) {
            writer.flush();
        }
    }

    public enum FlushMode {
        ALWAYS {
            @Override
            boolean shouldFlush() {
                return true;
            }
        },
        NEVER {
            @Override
            boolean shouldFlush() {
                return false;
            }
        };

        abstract boolean shouldFlush();

    }
}
