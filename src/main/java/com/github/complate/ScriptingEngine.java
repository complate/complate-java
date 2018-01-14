package com.github.complate;

import com.github.complate.api.ComplateScript;

public interface ScriptingEngine {
    void invoke(final ComplateScript scriptLocation,
                final String functionName,
                final Object... args)
            throws ScriptingException;
}
