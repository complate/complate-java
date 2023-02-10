/**
 * Copyright 2019 complate.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.complate.servlet;

import org.complate.core.ComplateStream;
import org.complate.core.stream.ComplateDelegatingStream;
import org.complate.core.stream.ComplatePrintWriterStream;

import jakarta.servlet.http.HttpServletResponse;
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
