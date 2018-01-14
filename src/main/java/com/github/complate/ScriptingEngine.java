package com.github.complate;

import org.springframework.core.io.Resource;

public interface ScriptingEngine {
    void invoke(final Resource scriptLocation,
                final String functionName,
                final Object... args)
            throws ScriptingException;
}
