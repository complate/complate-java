package com.github.complate.core.stream;

import com.github.complate.core.ComplateStream;

import java.io.PrintWriter;

import static com.github.complate.core.stream.PrintWriterComplateStream.DefaultFlushModes.ALWAYS;
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

    /**
     * Creates a new stream using the given <code>PrintWriter</code> with
     * {@link DefaultFlushModes#ALWAYS}.
     *
     * @param writer the writer used to write the output
     */
    public PrintWriterComplateStream(PrintWriter writer) {
        this(writer, ALWAYS);
    }

    /**
     * Creates a new stream using the given <code>PrintWriter</code> and
     * <code>FlushMode</code>.
     *
     * @param writer the writer used to write the output
     * @param flushMode the flush mode that should be used
     */
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
    public void flush() {
        if (flushMode.shouldFlush()) {
            writer.flush();
        }
    }

    public interface FlushMode {
        boolean shouldFlush();
    }

    public enum DefaultFlushModes implements FlushMode {
        ALWAYS {
            @Override
            public boolean shouldFlush() {
                return true;
            }
        },
        NEVER {
            @Override
            public boolean shouldFlush() {
                return false;
            }
        };
    }
}
