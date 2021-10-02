/**
 * Copyright 2020-2021 complate.org
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

import org.complate.core.ComplateSource;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.graalvm.polyglot.HostAccess.ALL;

/**
 * Builder with sane defaults (see {@link #DEFAULT_CONTEXT_CUSTOMIZATIONS}) that
 * has to be used to create an instance of {@link GraalComplateRenderer}.
 *
 * @author mvitz
 * @since 0.2.0
 */
public final class GraalComplateRendererBuilder {

    /**
     * These default customizations are applied to the {@link Context.Builder}
     * prior to calling {@link Context.Builder#build()} if no other customizations
     * are configured via {@link #withContextCustomizations(Function)}.
     * <p>
     * These customizations do the following things:
     * <ol>
     *   <li>Call {@link Context.Builder#allowHostAccess(HostAccess)} with {@link HostAccess#ALL}.</li>
     *   <li>Call {@link Context.Builder#allowHostClassLookup(Predicate)} with a {@link Predicate} that always returns {@code true}.</li>
     *   <li>Call {@link Context.Builder#allowExperimentalOptions(boolean)} with {@code true}.</li>
     *   <li>Call {@link Context.Builder#option(String, String)} with {@code "js.experimental-foreign-object-prototype"} and {@code "true"}.</li>
     *   <li>Call {@link Context.Builder#option(String, String)} with {@code "engine.WarnInterpreterOnly"} and {@code "false"}.</li>
     * </ol>
     * <p>
     * If you want to extends these defaults please use {@link #withAdditionalContextCustomizations(Function)}.
     */
    public static final UnaryOperator<Context.Builder> DEFAULT_CONTEXT_CUSTOMIZATIONS =
        builder -> builder
            .allowHostAccess(ALL)
            .allowHostClassLookup(s -> true)
            .allowExperimentalOptions(true)
            .option("js.experimental-foreign-object-prototype", "true")
            .option("engine.WarnInterpreterOnly", "false");

    private final GraalComplateRendererFactory factory;
    private final Supplier<Context.Builder> contextBuilderFactory;
    private Function<Context.Builder, Context.Builder> contextCustomizations = DEFAULT_CONTEXT_CUSTOMIZATIONS;
    private final Map<String, Object> bindings = new HashMap<>();
    private final ComplateSource source;

    GraalComplateRendererBuilder(GraalComplateRendererFactory factory,
                                 Supplier<Context.Builder> contextBuilderFactory,
                                 ComplateSource source) {
        this.factory = factory;
        this.contextBuilderFactory = contextBuilderFactory;
        this.source = source;
    }

    /**
     * Adds the given bindings to the previous ones.
     *
     * @param bindings the bindings to add
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this bindings on top
     */
    public GraalComplateRendererBuilder withBindings(Map<String, ?> bindings) {
        this.bindings.putAll(bindings);
        return this;
    }

    /**
     * Adds the given binding to the previous ones.
     *
     * @param identifier the global identifier the value can be accessed within
     *                   the views
     * @param value      the value of this binding
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this binding on top
     */
    public GraalComplateRendererBuilder withBinding(String identifier, Object value) {
        bindings.put(identifier, value);
        return this;
    }

    /**
     * The internally used {@link Context} can be customized within the given
     * {@link Function} before being created. This is useful if you have special
     * needs (e.g. require nashorn compatibility) or want to allow more access.
     * <p>
     * <strong>Note:</strong> The {@link #DEFAULT_CONTEXT_CUSTOMIZATIONS} is
     * deactivated when using this method. If you want to only extend these
     * defaults you can use {@link #withAdditionalContextCustomizations(Function)}.
     *
     * @param contextCustomizations the customizations used on creation
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this customizations
     *
     * @deprecated Please use {@link #withContextCustomizations(UnaryOperator)}.
     */
    @Deprecated
    public GraalComplateRendererBuilder withContextCustomizations(Function<Context.Builder, Context.Builder> contextCustomizations) {
        return withContextCustomizations(contextCustomizations::apply);
    }

    /**
     * The internally used {@link Context} can be customized within the given
     * {@link Function} before being created. This is useful if you have special
     * needs (e.g. require nashorn compatibility) or want to allow more access.
     * <p>
     * <strong>Note:</strong> The {@link #DEFAULT_CONTEXT_CUSTOMIZATIONS} is
     * deactivated when using this method. If you want to only extend these
     * defaults you can use {@link #withAdditionalContextCustomizations(Function)}.
     *
     * @param contextCustomizations the customizations used on creation
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this customizations
     *
     * @since 0.3.0
     */
    public GraalComplateRendererBuilder withContextCustomizations(UnaryOperator<Context.Builder> contextCustomizations) {
        this.contextCustomizations = contextCustomizations;
        return this;
    }

    /**
     * The internally use {@link Context} can be customized within the given
     * {@link Function} before being created. The given additional customizations
     * are applied on top of the {@link #DEFAULT_CONTEXT_CUSTOMIZATIONS}. If you
     * don't want these to be applied please use {@link #withContextCustomizations(Function)}.
     *
     * @param additionalContextCustomizations the additional customizations used on creation
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this customizations added on top
     *
     * @deprecated Please use {@link #withContextCustomizations(UnaryOperator)}.
     */
    @Deprecated
    public GraalComplateRendererBuilder withAdditionalContextCustomizations(Function<Context.Builder, Context.Builder> additionalContextCustomizations) {
        return withAdditionalContextCustomizations(additionalContextCustomizations::apply);
    }

    /**
     * The internally use {@link Context} can be customized within the given
     * {@link Function} before being created. The given additional customizations
     * are applied on top of the {@link #DEFAULT_CONTEXT_CUSTOMIZATIONS}. If you
     * don't want these to be applied please use {@link #withContextCustomizations(Function)}.
     *
     * @param additionalContextCustomizations the additional customizations used on creation
     * @return an {@link GraalComplateRendererBuilder} instance with all previous
     * state and this customizations added on top
     *
     * @since 0.3.0
     */
    public GraalComplateRendererBuilder withAdditionalContextCustomizations(UnaryOperator<Context.Builder> additionalContextCustomizations) {
        contextCustomizations = contextCustomizations.andThen(additionalContextCustomizations);
        return this;
    }

    /**
     * Creates a new {@link GraalComplateRenderer} instance based on this
     * {@link GraalComplateRendererBuilder}s state.
     *
     * @return a new {@link GraalComplateRenderer} instance
     */
    public GraalComplateRenderer build() {
        final Context.Builder contextBuilder = contextBuilderFactory.get();
        final Context.Builder customizedContextBuilder = contextCustomizations.apply(contextBuilder);
        final Context context = customizedContextBuilder.build();
        return factory.create(context, source, bindings);
    }

    @FunctionalInterface
    interface GraalComplateRendererFactory {

        GraalComplateRenderer create(Context context,
                                     ComplateSource source,
                                     Map<String, ?> bindings);
    }
}
