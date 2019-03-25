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

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateStream} implementation that delegates all method invocations
 * to another {@link ComplateStream}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public abstract class ComplateDelegatingStream implements ComplateStream {

    private final ComplateStream delegatee;

    protected ComplateDelegatingStream(ComplateStream delegatee) {
        this.delegatee = requireNonNull(delegatee, "delegatee must not be null");
    }

    @Override
    public final void write(String string) {
        delegatee.write(string);
    }

    @Override
    public final void writeln(String string) {
        delegatee.writeln(string);
    }

    @Override
    public final void flush() throws IOException {
        delegatee.flush();
    }
}
