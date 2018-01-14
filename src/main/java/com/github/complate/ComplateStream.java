package com.github.complate;

import java.io.IOException;

public interface ComplateStream {
    void write(final String s);
    void writeln(final String line);
    void flush() throws IOException;
}
