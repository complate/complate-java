package org.complate.servlet;

import org.complate.core.ComplateStream;
import org.complate.core.stream.ComplateDelegatingStream;
import org.complate.core.stream.ComplatePrintWriterStream;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateStream} that uses the output stream of a given
 * {@link HttpServletResponse}.
 *
 * @author mvitz
 * @see #fromResponse(HttpServletResponse)
 * @since 0.1.0
 */
public final class ComplateHttpServletResponseStream extends ComplateDelegatingStream {

    private ComplateHttpServletResponseStream(ComplateStream stream) {
        super(stream);
    }

    /**
     * Creates a new complate stream for the given {@link HttpServletResponse}.
     *
     * @param response the response that provides the output stream
     * @return a freshly created complate stream
     * @throws IOException if any I/O error occurs
     */
    public static ComplateHttpServletResponseStream fromResponse(
        HttpServletResponse response) throws IOException {
        requireNonNull(response, "response must not be null");
        final ComplateStream stream = new ComplatePrintWriterStream(response.getWriter());
        return new ComplateHttpServletResponseStream(stream);
    }
}
