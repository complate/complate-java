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
 * {@link ComplateRenderer} that delegates to a given {@link ComplateRenderer}
 * that is newly created on every call to
 * {@link #render(String, Map, ComplateStream)}.
 *
 * This may be useful if the {@link ComplateRenderer} implementation you want to
 * use evaluates or caches something on creation which is not desired e.g.
 * during development where the source file changes frequently and you want it
 * to be evaluated freshly on every call.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateReEvaluatingRenderer implements ComplateRenderer {

    private final Supplier<ComplateRenderer> creator;

    public ComplateReEvaluatingRenderer(Supplier<ComplateRenderer> creator) {
        this.creator = requireNonNull(creator, "creator must not be null");
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        creator.get().render(view, parameters, stream);
    }
}
