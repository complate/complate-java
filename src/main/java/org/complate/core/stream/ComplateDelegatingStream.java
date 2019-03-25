package org.complate.core.stream;

import org.complate.core.ComplateStream;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateStream} implementation that delegates all method invocations
 * to another {@link ComplateStream}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public abstract class ComplateDelegatingStream implements ComplateStream {

    private final ComplateStream delegatee;

    protected ComplateDelegatingStream(ComplateStream delegatee) {
        this.delegatee = requireNonNull(delegatee, "delegatee must not be null");
    }

    @Override
    public final void write(String string) {
        delegatee.write(string);
    }

    @Override
    public final void writeln(String string) {
        delegatee.writeln(string);
    }

    @Override
    public final void flush() throws IOException {
        delegatee.flush();
    }
}
