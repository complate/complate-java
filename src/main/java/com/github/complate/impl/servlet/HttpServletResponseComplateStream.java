package com.github.complate.impl.servlet;

import com.github.complate.api.ComplateStream;
import com.github.complate.impl.io.DelegatingComplateStream;
import com.github.complate.impl.io.PrintWriterComplateStream;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateStream} that uses the output stream of a given
 * {@link HttpServletResponse}.
 *
 * @author Michael Vitz
 * @see #fromResponse(HttpServletResponse)
 * @since 0.1.0
 */
public final class HttpServletResponseComplateStream
        extends DelegatingComplateStream {

    private HttpServletResponseComplateStream(ComplateStream stream) {
        super(stream);
    }

    /**
     * Creates a new complate stream for the given {@link HttpServletResponse}.
     *
     * @param response the response that provides the output stream
     * @return a freshly created complate stream
     * @throws IOException if any I/O error occurs
     */
    public static HttpServletResponseComplateStream fromResponse(
            HttpServletResponse response) throws IOException {
        requireNonNull(response, "response must not be null");
        final ComplateStream stream =
            new PrintWriterComplateStream(response.getWriter());
        return new HttpServletResponseComplateStream(stream);
    }
}
