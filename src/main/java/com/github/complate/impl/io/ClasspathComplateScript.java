package com.github.complate.impl.io;

import com.github.complate.api.ComplateScript;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateScript} that uses the java class path.
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public final class ClasspathComplateScript implements ComplateScript {

    private final String name;

    /**
     * Creates a new class path base script with the given name.
     *
     * @param name the name of the script. If not absolute it starts at the
     *             package of this class.
     */
    public ClasspathComplateScript(String name) {
        this.name = requireNonNull(name, "name must not be null");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return ClasspathComplateScript.class.getResourceAsStream(name);
    }

    @Override
    public String getDescription() {
        return "class path script [" + name + "]";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
