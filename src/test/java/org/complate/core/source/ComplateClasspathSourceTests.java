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
