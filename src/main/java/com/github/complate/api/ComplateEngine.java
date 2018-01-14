package com.github.complate.api;

import com.github.complate.ScriptingException;

/**
 * Engine that is used to execute a given {@link ComplateScript} and writes its
 * output into the given {@link ComplateStream}.
 *
 * @author Michael Vitz
 * @since 0.1.0
 */
public interface ComplateEngine {

    /**
     * Invokes the <strong>render</strong> function of the given
     * {@link ComplateScript} with the given stream, tag and parameters.
     *
     * @param script     the script to be used as source
     * @param stream     the stream to be passed into the render function of the
     *                   script
     * @param tag        the tag that is passed to the render function of the
     *                   script
     * @param parameters the optional parameters that are passed to the render
     *                   function of the script
     * @throws ScriptingException if any error occurs
     */
    void invoke(ComplateScript script, ComplateStream stream, String tag,
                Object... parameters) throws ScriptingException;
}
