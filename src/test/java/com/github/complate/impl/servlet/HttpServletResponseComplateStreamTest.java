package com.github.complate.impl.servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class HttpServletResponseComplateStreamTest {

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter writer = mock(PrintWriter.class);

    HttpServletResponseComplateStream sut;

    @Before
    public void setUp() throws Exception {
        given(response.getWriter()).willReturn(writer);
        sut = HttpServletResponseComplateStream.fromResponse(response);
    }

    @Test
    public void write_should_work() {
        sut.write("Foo");

        verify(writer, only()).print("Foo");
    }

    @Test
    public void writeln_should_work() {
        sut.writeln("Foo");

        verify(writer, only()).println("Foo");
    }

    @Test
    public void flush_should_work() throws Exception {
        sut.flush();

        verify(writer, only()).flush();
    }
}
