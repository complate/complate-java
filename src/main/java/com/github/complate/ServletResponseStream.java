package com.github.complate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

final class ServletResponseStream implements ComplateStream {

    private final PrintWriter writer;

    ServletResponseStream(final HttpServletResponse response) throws IOException {
        if (response == null) {
            throw new IllegalArgumentException(
                    "Servlet response may not be null");
        }
        this.writer = response.getWriter();
    }

    @Override
    public void write(final String s) {
        this.writer.print(s);
    }

    @Override
    public void writeln(final String line) {
        this.writer.println(line);
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }
}
