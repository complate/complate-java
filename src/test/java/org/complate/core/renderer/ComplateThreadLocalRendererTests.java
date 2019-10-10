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

import org.complate.core.ComplateRenderer;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplateThreadLocalRendererTests {

    @Test
    void new_withNullCreator_throwsException() {
        assertThatThrownBy(() -> new ComplateThreadLocalRenderer(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("creator must not be null");
    }

    @Test
    void render_delegatesToCreatorReturnedInstance() {
        // arrange
        ComplateRenderer renderer = mock(ComplateRenderer.class);
        @SuppressWarnings("unchecked")
        Supplier<ComplateRenderer> creator = mock(Supplier.class);
        when(creator.get()).thenReturn(renderer);

        ComplateRenderer sut = new ComplateThreadLocalRenderer(creator);

        // act
        sut.render(null, null, null);

        // assert
        verify(renderer, only()).render(null, null, null);
    }

    @Test
    void render_createsFreshInstanceForEveryThread() throws Exception {
        // arrange
        @SuppressWarnings("unchecked")
        Supplier<ComplateRenderer> creator = mock(Supplier.class);
        when(creator.get()).thenReturn(mock(ComplateRenderer.class));

        ComplateRenderer sut = new ComplateThreadLocalRenderer(creator);

        // act
        Runnable runnable = () -> sut.render(null ,null, null);

        Thread t1 = new Thread(runnable);
        t1.start();

        Thread t2 = new Thread(runnable);
        t2.start();

        t1.join();
        t2.join();

        // assert
        verify(creator, times(2)).get();
    }

    @Test
    void render_reusesInstanceWithinSameThread() throws Exception {
        // arrange
        @SuppressWarnings("unchecked")
        Supplier<ComplateRenderer> creator = mock(Supplier.class);
        when(creator.get()).thenReturn(mock(ComplateRenderer.class));

        ComplateRenderer sut = new ComplateThreadLocalRenderer(creator);

        // act
        Thread t = new Thread(() -> {
            sut.render(null ,null, null);
            sut.render(null ,null, null);
        });
        t.start();
        t.join();

        // assert
        verify(creator, only()).get();
    }
}
