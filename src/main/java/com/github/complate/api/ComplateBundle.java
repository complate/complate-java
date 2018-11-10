package com.github.complate.api;

import java.util.Map;

import static java.util.Collections.emptyMap;

// TODO: add documentation
public interface ComplateBundle {

    // TODO: add documentation
    default void render(ComplateStream stream, String tag)
            throws ComplateException {
        render(stream, tag, emptyMap());
    }

    // TODO: add documentation
    void render(ComplateStream stream, String tag, Map<String, ?> parameters)
            throws ComplateException;
}
