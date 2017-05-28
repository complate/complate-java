package complate;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

public class WritableStream {
    protected HttpServletResponse response;
    protected PrintWriter stream;

    WritableStream(HttpServletResponse response) throws IOException {
        this.response = response;
        this.stream = response.getWriter();
    }

    public void write(String msg) {
        this.stream.print(msg);
    }

    public void writeln(String msg) {
        this.stream.println(msg);
    }

    public void flush() throws IOException {
        this.response.flushBuffer();
    }
}
