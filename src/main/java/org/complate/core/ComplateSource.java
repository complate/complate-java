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
import java.io.InputStream;

/**
 * IO Abstraction for a JavaScript script that is used by complate to generate a
 * views' output.
 *
 * <p>The script is required to declare a function named <strong>render</strong>
 * that takes the arguments <strong>view</strong>, <strong>params</strong> and
 * <strong>stream</strong>. The following code snippet shows an simple example:
 * <pre>{@code
 * function render(view, params, stream) {
 *   stream.writeln(view);
 *   stream.writeln(params);
 *   stream.flush();
 * }
 * }</pre>
 *
 * @author mvitz
 * @since 0.1.0
 */
public interface ComplateSource {

    /**
     * Return an {@link InputStream} for the content of the underlying script.
     * <p>It is expected that each call creates a <i>fresh</i> stream.
     *
     * @return the input stream for the underlying script (must not be {@code null})
     * @throws IOException if the stream could not be opened
     */
    InputStream getInputStream() throws IOException;

    /**
     * Return a description for this script, to be used for error output when
     * working with the script.
     * <p>Implementations are also encouraged to return this value from their
     * {@code toString} method.
     *
     * @return the description for this script
     * @see Object#toString()
     */
    String getDescription();

}
