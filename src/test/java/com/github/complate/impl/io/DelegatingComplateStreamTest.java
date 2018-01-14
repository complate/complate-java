package com.github.complate.impl.io;

import com.github.complate.api.ComplateStream;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class DelegatingComplateStreamTest {

    ComplateStream delegatee = mock(ComplateStream.class);
    DelegatingComplateStream sut = new DelegatingComplateStream(delegatee) {};

    @Test
    public void write_should_delegate() {
        sut.write("Foo");

        verify(delegatee, only()).write("Foo");
    }

    @Test
    public void writeln_should_delegate() {
        sut.writeln("Foo");

        verify(delegatee, only()).writeln("Foo");
    }

    @Test
    public void flush_should_delegate() throws Exception {
        sut.flush();

        verify(delegatee, only()).flush();
    }
}
