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
package org.complate.core.renderer;

import org.complate.core.ComplateException;
import org.complate.core.ComplateRenderer;
import org.complate.core.ComplateStream;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateRenderer} that uses the given {@link Supplier} to create an
 * {@link ComplateRenderer} instance for every thread.
 *
 * This may be useful in multi threaded environments to ensure there are no
 * concurrent calls to a single {@link ComplateRenderer}.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateThreadLocalRenderer implements ComplateRenderer {

    private final ThreadLocal<ComplateRenderer> rendererPerThread;

    public ComplateThreadLocalRenderer(Supplier<ComplateRenderer> creator) {
        requireNonNull(creator, "creator must not be null");
        rendererPerThread = ThreadLocal.withInitial(creator);
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        rendererPerThread.get().render(view, parameters, stream);
    }
}
