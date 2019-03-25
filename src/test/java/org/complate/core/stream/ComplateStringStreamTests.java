package org.complate.core.stream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComplateStringStreamTests {

    ComplateStringStream sut = new ComplateStringStream();

    @Test
    void write_withString_addsStringToOutput() {
        // arrange
        String string = "abc";

        // act
        sut.write(string);

        // assert
        assertThat(sut.getContent()).isEqualTo("abc");
    }

    @Test
    void writeln_withString_addsStringWithEndingNewLineToOutput() {
        // arrange
        String string = "abc";

        // act
        sut.writeln(string);

        // assert
        assertThat(sut.getContent()).isEqualTo("abc\n");
    }
}
