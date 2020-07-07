/**
 * Copyright 2019 complate.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.complate.graal;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.complate.core.ComplateException;
import org.complate.core.ComplateRenderer;
import org.complate.core.ComplateSource;
import org.complate.core.source.ComplateClasspathSource;
import org.complate.core.stream.ComplateStringStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GraalComplateRendererTests {

    private ComplateRenderer renderer(ComplateSource source, Map<String, ?> bindings) {
        return GraalComplateRenderer
            .of(source)
            .withBindings(bindings)
            .build();
    }

    private ComplateRenderer renderer(ComplateSource source) {
        return renderer(source, Collections.emptyMap());
    }

    @Test
    void new_sourceWithNonExistingBundle_throwsException() {
        // given
        ComplateClasspathSource source = new ComplateClasspathSource("/non_existing_bundle.js");

        // when
        ThrowingCallable createRenderer = () -> renderer(source);

        // then
        assertThatThrownBy(createRenderer)
            .isInstanceOf(ComplateException.class)
            .hasMessage("failed to initialize input stream for source 'class path source [/non_existing_bundle.js]'");
    }

    @Test
    void new_sourceWithInvalidBundle_throwsException() {
        // given
        ComplateClasspathSource source = new ComplateClasspathSource("/invalid_bundle.js");

        // when
        ThrowingCallable createRenderer = () -> renderer(source);

        // then
        assertThatThrownBy(createRenderer)
            .isInstanceOf(ComplateException.class)
            .hasMessageContaining("failed to evaluate script")
            .hasMessageContaining("SyntaxError")
            .hasMessageContaining("class path source [/invalid_bundle.js]")
            .hasMessageContaining("Expected {");
    }

    @Test
    void render_sourceWithoutRenderFunction_throwsException() {
        // given
        ComplateRenderer sut = renderer(
            new ComplateClasspathSource("/bundle_without_render_function.js"));

        // when
        ThrowingCallable render = () -> sut.render("view", emptyMap(), new ComplateStringStream());

        // then
        assertThatThrownBy(render)
            .isInstanceOf(ComplateException.class)
            .hasMessageContaining("ReferenceError")
            .hasMessageContaining("render is not defined");
    }

    @Test
    void render_sourceWithRuntimeError_throwsException() {
        // given
        ComplateRenderer sut = renderer(
            new ComplateClasspathSource("/bundle_with_runtime_error.js"));

        // when
        ThrowingCallable render = () -> sut.render("view", emptyMap(), new ComplateStringStream());

        // act + assert
        assertThatThrownBy(render)
            .isInstanceOf(ComplateException.class)
            .hasMessageContaining("ReferenceError")
            .hasMessageContaining("foo is not defined");
    }

    public static final class FunctionBinding {
        public String greet(String name) {
            return "Hello, " + name + "!";
        }
    }

    @Nested
    class WithSimpleBundle {

        ComplateRenderer sut = renderer(
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
            assertThat(stream.getContent())
                .startsWith("global{")
                .endsWith("}");
        }

        @Test
        void render_bindingsView_shouldProvideBindingsToView() {
            // act
            sut.render("bindings", emptyMap(), stream);

            // assert
            assertThat(stream.getContent()).isEqualTo("Hello, World!");
        }
    }
}
