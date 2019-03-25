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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ComplateClasspathSourceTests {

    @Test
    void new_withNullName_throwsException() {
        assertThatThrownBy(() -> new ComplateClasspathSource(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("name must not be null");
    }

    @Test
    void getInputStream_withNonExistingScript_throwsFileNotFoundException() {
        // arrange
        ComplateClasspathSource sut = new ComplateClasspathSource("non_existing.js");

        // act + assert
        assertThatThrownBy(() -> sut.getInputStream())
            .isInstanceOf(FileNotFoundException.class)
            .hasMessage("class path source [non_existing.js] cannot be opened because it does not exist");
    }

    @Nested
    class WithExistingScript {

        ComplateClasspathSource sut = new ComplateClasspathSource("/existing_script.js");

        @Test
        void getDescription_returnsHumanReadableDescription() {
            // act
            String description = sut.getDescription();

            // assert
            assertThat(description).isEqualTo("class path source [/existing_script.js]");
        }

        @Test
        void getInputStream_returnsStreamOfGivenScript() throws Exception {
            // act
            InputStream inputStream = sut.getInputStream();

            // assert
            assertThat(inputStream).hasContent("function render(view, params, stream) {}");
        }
    }

}
