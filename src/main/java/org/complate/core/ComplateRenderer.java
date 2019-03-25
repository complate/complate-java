package org.complate.core;

import java.util.Map;

/**
 * TODO: add documentation
 *
 * @author mvitz
 * @since 0.1.0
 */
public interface ComplateRenderer {

    /**
     * Renders the given view with the given parameters into the given stream.
     *
     * @param view the name of the view to render
     * @param parameters the parameters that can be used inside the view
     * @param stream the stream the output is rendered into
     * @throws ComplateException if anything fails
     */
    void render(String view, Map<String, ?> parameters, ComplateStream stream)
        throws ComplateException;
}
