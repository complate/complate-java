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
package org.complate.nashorn;

import org.complate.core.ComplateRenderer;
import org.complate.core.source.ComplateClasspathSource;
import org.complate.core.stream.ComplateStringStream;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class NashornComplateRendererTests {

    ComplateRenderer sut = new NashornComplateRenderer(
        new ComplateClasspathSource("/simple_bundle.js"),
        new HashMap<String, Object>() {{
            put("constantBinding", "World");
            put("functionBinding", new FunctionBinding());
        }});

    ComplateStringStream stream = new ComplateStringStream();

    @Test
    void render_listView_withAllParams_works() {
        // arrange
        Map<String, String> parameters = new HashMap<>();
        parameters.put("a", "1");
        parameters.put("b", "2");
        parameters.put("c", "3");

        // act
        sut.render("list", parameters, stream);

        // assert
        assertThat(stream.getContent()).isEqualTo("Arguments: 1, 2, 3");
    }

    @Test
    void render_globalView_shouldRenderAvailableGlobalObject() {
        // act
        sut.render("global", emptyMap(), stream);

        // assert
        assertThat(stream.getContent()).isEqualTo("[object global]");
    }

    @Test
    void render_consoleView_shouldRenderAvailableConsoleStuff() {
        // act
        sut.render("console", emptyMap(), stream);

        // assert
        assertThat(stream.getContent()).isEqualTo(
            "[object Object]\n" +
            "function print() { [native code] }\n" +
            "function print() { [native code] }");
    }

    @Test
    void render_bindingsView_shouldProvideBindingsToView() {
        // act
        sut.render("bindings", emptyMap(), stream);

        // assert
        assertThat(stream.getContent()).isEqualTo("Hello, World!");
    }

    public static final class FunctionBinding {

        public String greet(String name) {
            return "Hello, " + name + "!";
        }
    }
}
