/**
 * Copyright 2019-2020 complate.org
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
package org.complate.graal;

import org.complate.core.ComplateException;
import org.complate.core.ComplateRenderer;
import org.complate.core.ComplateSource;
import org.complate.core.ComplateStream;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static java.util.Collections.emptyMap;
import static org.complate.graal.GraalComplateRendererBuilder.DEFAULT_CONTEXT_CUSTOMIZATIONS;

/**
 * {@link Context Graal} based {@link ComplateRenderer}.
 *
 * @author mvitz
 * @author larsrh
 * @since 0.2.0
 */
public final class GraalComplateRenderer implements ComplateRenderer {

    private static final String CONTEXT_LANGUAGE = "js";
    private static final String POLYFILLS =
        "if(typeof global === \"undefined\") { var global = this; }\n" +
        "\n";

    private final Context context;

    private GraalComplateRenderer(Context context, ComplateSource source, Map<String, ?> bindings) {
        this.context = context;

        context.eval(CONTEXT_LANGUAGE, POLYFILLS);
        addBindings(bindings);

        try (Reader reader = readerFor(source)) {
            context.eval(Source.newBuilder(CONTEXT_LANGUAGE, reader, source.getDescription()).build());
        } catch (IOException e) {
            throw new ComplateException(e, "failed to read script from source '%s'", source.getDescription());
        } catch (PolyglotException e) {
            throw new ComplateException(e, "failed to evaluate script: %s", extractJavaScriptError(e));
        }
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        final Value args = context.getBindings(CONTEXT_LANGUAGE);
        args.putMember("view", view);
        args.putMember("params", proxy(parameters));
        args.putMember("stream", stream);
        try {
            context.eval(CONTEXT_LANGUAGE, "render(view, ...params, stream);");
        } catch (PolyglotException e) {
            throw new ComplateException(e, "failed to render: %s", extractJavaScriptError(e));
        }
    }

    /**
     * Creates a {@link GraalComplateRendererBuilder} that can be used to create
     * an instance of this {@link ComplateRenderer}.
     *
     * @param source the {@link ComplateSource} used to render the views
     * @return a new {@link GraalComplateRendererBuilder}
     */
    public static GraalComplateRendererBuilder of(ComplateSource source) {
        return new GraalComplateRendererBuilder(GraalComplateRenderer::new, () -> Context.newBuilder(CONTEXT_LANGUAGE), source);
    }

    private void addBindings(Map<String, ?> bindings) {
        final Value contextBindings = context.getBindings(CONTEXT_LANGUAGE);
        bindings.forEach(contextBindings::putMember);
    }

    private static Reader readerFor(ComplateSource source) {
        final InputStream is;
        try {
            is = source.getInputStream();
        } catch (IOException e) {
            throw new ComplateException(e, "failed to initialize input stream for source '%s'", source.getDescription());
        }
        final Reader isr = new InputStreamReader(is);
        return new BufferedReader(isr);
    }

    private static String extractJavaScriptError(PolyglotException e) {
        final StringBuilder buf = new StringBuilder();
        buf.append(e.getMessage());
        buf.append("\n");
        for (PolyglotException.StackFrame frame : e.getPolyglotStackTrace()) {
            buf.append(frame);
            buf.append("\n");
        }
        return buf.toString();
    }

    @SuppressWarnings("unchecked")
    private static Object[] proxy(Object... objects) {
        return Arrays.stream(objects)
            .map(o -> o instanceof Map ? ProxyObject.fromMap((Map) o) : o)
            .toArray();
    }
}
