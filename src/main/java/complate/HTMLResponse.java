package complate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: ensure `#setStatus` is invoked first and only once
public class HTMLResponse extends WritableStream {
    HTMLResponse(HttpServletResponse response) throws IOException {
        super(response);
    }

    public void setStatus(int code) {
        this.response.setStatus(code);
        this.response.setContentType("text/html; charset=UTF-8");
    }
}
