package com.github.complate.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * IO Abstraction for a JavaScript script that is used by complate.
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public interface ComplateScript {

    /**
     * Return an {@link InputStream} for the content of an underlying script.
     * <p>It is expected that each call creates a <i>fresh</i> stream.
     *
     * @return the input stream for the underlying script (must not be {@code null})
     * @throws IOException if the content stream could not be opened
     */
    InputStream getInputStream() throws IOException;

    /**
     * Return a description for this script, to be used for error output when
     * working with the script.
     * <p>Implementations are also encouraged to return this value from their
     * {@code toString} method.
     *
     * @return the description for this script
     * @see Object#toString()
     */
    String getDescription();
}
