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

import static java.lang.String.format;

/**
 * TODO: add documentation
 *
 * @author mvitz
 * @since 0.1.0
 */

public final class ComplateException extends RuntimeException {

    /**
     * Creates a new exception with a message constructed by the given format
     * and arguments.
     *
     * @param format any format valid for usage in
     *               {@link String#format(String, Object...)}
     * @param args any arguments valid for usage in
     *             {@link String#format(String, Object...)} for the given format
     */
    public ComplateException(String format, Object... args) {
        super(format(format, args));
    }

    /**
     * Creates a new exception with the given throwable as cause and a message
     * constructed by the given format and arguments.
     *
     * @param cause the throwable that was the cause for this exception
     * @param format any format valid for usage in
     *               {@link String#format(String, Object...)}
     * @param args any arguments valid for usage in
     *             {@link String#format(String, Object...)} for the given format
     */
    public ComplateException(Throwable cause, String format, Object... args) {
        super(format(format, args), cause);
    }
}
