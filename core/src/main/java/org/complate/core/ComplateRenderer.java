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

import java.util.Map;

/**
 * TODO: add documentation
 *
 * @author mvitz
 * @since 0.1.0
 */
public interface ComplateRenderer {

    /**
     * Renders the given view with the given parameters into the given stream.
     *
     * @param view the name of the view to render
     * @param parameters the parameters that can be used inside the view
     * @param stream the stream the output is rendered into
     * @throws ComplateException if anything fails
     */
    void render(String view, Map<String, ?> parameters, ComplateStream stream)
        throws ComplateException;
}
