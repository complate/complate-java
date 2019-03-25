package org.complate.core.stream;

import org.complate.core.ComplateStream;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

class ComplateDelegatingStreamTests {

    ComplateStream delegatee = mock(ComplateStream.class);
    ComplateDelegatingStream sut = new ComplateDelegatingStream(delegatee) {};

    @Test
    void write_should_delegate() {
        // act
        sut.write("Foo");

        // assert
        verify(delegatee, only()).write("Foo");
    }

    @Test
    void writeln_should_delegate() {
        // act
        sut.writeln("Foo");

        // assert
        verify(delegatee, only()).writeln("Foo");
    }

    @Test
    void flush_should_delegate() throws Exception {
        // act
        sut.flush();

        // assert
        verify(delegatee, only()).flush();
    }
}
