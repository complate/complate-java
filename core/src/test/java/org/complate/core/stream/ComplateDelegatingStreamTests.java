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
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

class ComplateDelegatingStreamTests {

    ComplateStream delegatee = mock(ComplateStream.class);
    ComplateDelegatingStream sut = new ComplateDelegatingStream(delegatee) {};

    @Test
    void write_should_delegate() {
        // act
        sut.write("Foo");

        // assert
        verify(delegatee, only()).write("Foo");
    }

    @Test
    void writeln_should_delegate() {
        // act
        sut.writeln("Foo");

        // assert
        verify(delegatee, only()).writeln("Foo");
    }

    @Test
    void flush_should_delegate() throws Exception {
        // act
        sut.flush();

        // assert
        verify(delegatee, only()).flush();
    }
}
