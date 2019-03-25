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

import java.io.PrintWriter;

import static java.util.Objects.requireNonNull;

/**
 * {@link PrintWriter} based {@link ComplateStream}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplatePrintWriterStream implements ComplateStream {

    private final PrintWriter writer;

    /**
     * Creates a new stream using the given <code>PrintWriter</code>..
     *
     * @param writer the writer used to write the output
     */
    public ComplatePrintWriterStream(PrintWriter writer) {
        this.writer = requireNonNull(writer, "writer must not be null");
    }

    @Override
    public void write(String string) {
        writer.print(string);
    }

    @Override
    public void writeln(String string) {
        writer.println(string);
    }

    @Override
    public void flush() {
        writer.flush();
    }
}
