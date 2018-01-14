package com.github.complate;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.servlet.View;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComplateView implements View {

    private static final String DEFAULT_CONTENT_TYPE =
            "text/html; charset=UTF-8";

    private static final String RENDER_FUNCTION_NAME = "render";

    private final ScriptingEngine scriptingEngine;

    private final Resource bundle;

    private final String tag;

    ComplateView(final ScriptingEngine scriptingEngine,
                 final Resource bundle,
                 final String tag) {
        Assert.notNull(scriptingEngine, "ScriptingEngine must not be null");
        Assert.notNull(bundle,
            "bundle must not be null");
        Assert.notNull(tag, "tag must not be null");
        this.scriptingEngine = scriptingEngine;
        this.bundle = bundle;
        this.tag = tag;
    }

    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {

        response.setContentType(DEFAULT_CONTENT_TYPE);

        final ComplateStream stream = new ServletResponseStream(response);

        this.scriptingEngine.invoke(
                this.bundle,
                RENDER_FUNCTION_NAME,
                stream,
                this.tag,
                model);
    }
}
