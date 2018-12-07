package com.github.complate.core.stream;

import com.github.complate.core.ComplateStream;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateStream} implementation that delegates all method invocations
 * to another {@link ComplateStream}.
 */
public abstract class DelegatingComplateStream implements ComplateStream {

    private final ComplateStream delegatee;

    protected DelegatingComplateStream(ComplateStream delegatee) {
        this.delegatee = requireNonNull(delegatee, "delegatee must not be null");
    }

    @Override
    public final void write(String s) {
        delegatee.write(s);
    }

    @Override
    public final void writeln(String s) {
        delegatee.writeln(s);
    }

    @Override
    public final void flush() throws IOException {
        delegatee.flush();
    }
}
