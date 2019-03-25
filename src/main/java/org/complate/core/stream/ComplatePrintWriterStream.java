package org.complate.core.stream;

import org.complate.core.ComplateStream;

import java.io.PrintWriter;

import static java.util.Objects.requireNonNull;

/**
 * {@link PrintWriter} based {@link ComplateStream}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplatePrintWriterStream implements ComplateStream {

    private final PrintWriter writer;

    /**
     * Creates a new stream using the given <code>PrintWriter</code>..
     *
     * @param writer the writer used to write the output
     */
    public ComplatePrintWriterStream(PrintWriter writer) {
        this.writer = requireNonNull(writer, "writer must not be null");
    }

    @Override
    public void write(String string) {
        writer.print(string);
    }

    @Override
    public void writeln(String string) {
        writer.println(string);
    }

    @Override
    public void flush() {
        writer.flush();
    }
}
