package com.github.complate;

import com.github.complate.api.ViewsBundle;

public interface ScriptingEngine {
    void invoke(final ViewsBundle scriptLocation,
                final String functionName,
                final Object... args)
            throws ScriptingException;
}
