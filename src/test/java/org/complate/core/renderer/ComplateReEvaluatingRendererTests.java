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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplateReEvaluatingRendererTests {

    @Test
    void new_withNullCreator_throwsException() {
        assertThatThrownBy(() -> new ComplateReEvaluatingRenderer(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("creator must not be null");
    }

    @Test
    void render_delegatesToCreatorReturnedInstance() {
        // arrange
        ComplateRenderer renderer = mock(ComplateRenderer.class);
        Supplier<ComplateRenderer> creator = mock(Supplier.class);
        when(creator.get()).thenReturn(renderer);

        ComplateReEvaluatingRenderer sut = new ComplateReEvaluatingRenderer(creator);

        // act
        sut.render(null, null, null);
        sut.render(null, null, null);

        // assert
        verify(creator, times(2)).get();
        verify(renderer, times(2)).render(null, null, null);
    }
}
