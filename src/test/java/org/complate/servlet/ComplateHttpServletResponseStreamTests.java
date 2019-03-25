package org.complate.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.complate.servlet.ComplateHttpServletResponseStream.fromResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class ComplateHttpServletResponseStreamTests {

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter writer = mock(PrintWriter.class);

    ComplateHttpServletResponseStream sut;

    @BeforeEach
    void setUp() throws Exception {
        given(response.getWriter()).willReturn(writer);
        sut = fromResponse(response);
    }

    @Test
    void write_shouldDelegateToResponse() {
        // act
        sut.write("Foo");

        // assert
        verify(writer, only()).print("Foo");
    }

    @Test
    void writeln_shouldDelegateToResponse() {
        // act
        sut.writeln("Foo");

        // assert
        verify(writer, only()).println("Foo");
    }

    @Test
    void flush_shouldDelegateToResponse() throws Exception {
        // act
        sut.flush();

        // assert
        verify(writer, only()).flush();
    }
}
