package org.complate.core.source;

import org.complate.core.ComplateSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/**
 * {@link ComplateSource} that loads a JavaScript resource using the java class
 * path.
 *
 * @author mvitz
 * @since 0.1.0
 */
public final class ComplateClasspathSource implements ComplateSource {

    private final String name;

    /**
     * Creates a new class path based source with the given name.
     *
     * @param name the name of the underlying script. If not absolute it starts
     *             at the package of this class.
     */
    public ComplateClasspathSource(String name) {
        this.name = requireNonNull(name, "name must not be null");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        final InputStream inputStream = ComplateClasspathSource.class.getResourceAsStream(name);
        if (inputStream == null) {
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }
        return inputStream;
    }

    @Override
    public String getDescription() {
        return "class path source [" + name + "]";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
