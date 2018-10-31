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
import static org.graalvm.polyglot.HostAccess.ALL;

/**
 * {@link Context Graal} based {@link ComplateRenderer}.
 */
public final class GraalComplateRenderer implements ComplateRenderer {

    private static final String POLYFILLS =
        "if(typeof global === \"undefined\") { var global = this; }\n" +
        "\n";

    private final Context context;

    public GraalComplateRenderer(ComplateSource source) {
        this(source, emptyMap());
    }

    public GraalComplateRenderer(ComplateSource source, Map<String, ?> bindings) {
        context = createContext(bindings);

        context.eval("js", POLYFILLS);

        try (Reader reader = readerFor(source)) {
            context.eval(Source.newBuilder("js", reader, source.getDescription()).build());
        } catch (IOException e) {
            throw new ComplateException(e, "failed to read script from source '%s'", source.getDescription());
        } catch (PolyglotException e) {
            throw new ComplateException(e, "failed to evaluate script: %s", extractJavaScriptError(e));
        }
    }

    @Override
    public void render(String view, Map<String, ?> parameters, ComplateStream stream) throws ComplateException {
        final Value args = context.getBindings("js");
        args.putMember("view", view);
        args.putMember("params", proxy(parameters));
        args.putMember("stream", stream);
        try {
            context.eval("js", "render(view, ...params, stream);");
        } catch (PolyglotException e) {
            throw new ComplateException(e, "failed to render: %s", extractJavaScriptError(e));
        }
    }

    private static Context createContext(Map<String, ?> bindings) {
        final Context context = createContext();

        final Value contextBindings = context.getBindings("js");
        bindings.forEach(contextBindings::putMember);

        return context;
    }

    private static Context createContext() {
        return Context.newBuilder("js")
            .allowHostAccess(ALL)
            .allowHostClassLookup((s) -> true)
            .allowExperimentalOptions(true).option("js.experimental-foreign-object-prototype", "true")
            .build();
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
