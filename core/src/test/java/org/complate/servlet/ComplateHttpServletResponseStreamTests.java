/**
 * Copyright 2019-2023 complate.org
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
package org.complate.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.complate.servlet.ComplateHttpServletResponseStream.fromResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

class ComplateHttpServletResponseStreamTests {

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter writer = mock(PrintWriter.class);

    ComplateHttpServletResponseStream sut;

    @BeforeEach
    void setUp() throws Exception {
        given(response.getWriter()).willReturn(writer);
        sut = fromResponse(response);
    }

    @Test
    void write_shouldDelegateToResponse() {
        // act
        sut.write("Foo");

        // assert
        verify(writer, only()).print((Object) "Foo");
    }

    @Test
    void writeln_shouldDelegateToResponse() {
        // act
        sut.writeln("Foo");

        // assert
        verify(writer, only()).println((Object) "Foo");
    }

    @Test
    void flush_shouldDelegateToResponse() throws Exception {
        // act
        sut.flush();

        // assert
        verify(writer, only()).flush();
    }
}
