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
package org.complate.core;

import java.io.IOException;

/**
 * This stream is used by an {@link ComplateRenderer} to render the views'
 * result into.
 *
 * @author FND
 * @author mvitz
 * @since 0.1.0
 */
public interface ComplateStream {

    /**
     * Writes the given string.
     *
     * @param object The <code>Object</code> to be written
     */
    void write(Object object);

    /**
     * Writes the given string and terminates the line.
     *
     * @param object The <code>Object</code> to be written
     */
    void writeln(Object object);

    /**
     * Flushes the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    void flush() throws IOException;
}
