package com.github.complate.core;

import java.util.Map;

/**
 * TODO: add documentation
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public interface ComplateRenderer {

    /**
     * Renders the given view with the given parameters into the given stream.
     *
     * @param view the view to render
     * @param parameters the parameters that can be used inside the view
     * @param stream the stream to render the output into
     * @throws ComplateException if anything fails
     */
    void render(String view, Map<String, ?> parameters, ComplateStream stream)
            throws ComplateException;

}
