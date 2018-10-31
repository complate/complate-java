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
package org.complate.core.stream;

import org.complate.core.ComplateStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * {@link ComplateStream} that captures all write calls in a {@link String}
 * which can be retrieved by calling {@link #getContent()}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateStringStream implements ComplateStream {

    private final ByteArrayOutputStream bytes;
    private final PrintStream out;

    /**
     * Constructs a new stream.
     */
    public ComplateStringStream() {
        bytes = new ByteArrayOutputStream();
        out = new PrintStream(bytes);
    }

    /**
     * Returns the current content of this stream.
     *
     * @return the current content of this stream
     */
    public String getContent() {
        return bytes.toString();
    }

    @Override
    public void write(Object object) {
        out.print(object);
    }

    @Override
    public void writeln(Object object) {
        out.println(object);
    }

    @Override
    public void flush() {
        out.flush();
    }
}
