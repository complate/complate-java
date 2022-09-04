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
package org.complate.core.source;

import org.complate.core.ComplateSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateSource} that loads a JavaScript resource using the java class
 * path.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateClasspathSource implements ComplateSource {

    private final String name;

    /**
     * Creates a new class path based source with the given name.
     *
     * @param name the name of the underlying script. If not absolute it starts
     *             at the package of this class.
     */
    public ComplateClasspathSource(String name) {
        this.name = requireNonNull(name, "name must not be null");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        final InputStream inputStream = ComplateClasspathSource.class.getResourceAsStream(name);
        if (inputStream == null) {
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }
        return inputStream;
    }

    @Override
    public String getDescription() {
        return "class path source [" + name + "]";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
