package com.github.complate;

import com.github.complate.core.ComplateRenderer;
import com.github.complate.core.script.ClasspathComplateScript;
import com.github.complate.core.stream.StringComplateStream;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.complate.nashorn.renderer.NashornComplateRenderer.nashornComplateRenderer;
import static org.junit.Assert.assertEquals;

/**
 * Uses the transpiled output of https://github.com/complate/complate-sample as
 * "integration tests".
 */
public class NashornSampleTest {

    private final ComplateRenderer renderer = nashornComplateRenderer(
        new ClasspathComplateScript("/sample.js")).build();

    @Test
    public void siteIndex_without_layout_should_be_rendered_correct() {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Foo");
        parameters.put("_layout", false);

        String result = render("SiteIndex", parameters);

        assertEquals(
            "<!DOCTYPE html>\n" +
            "<p>lorem ipsum dolor sit amet</p>",
            result);
    }

    @Test
    public void siteIndex_with_layout_should_be_rendered_correct() {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Bar");
        parameters.put("_layout", true);

        String result = render("SiteIndex", parameters);

        assertEquals(
            "<!DOCTYPE html>\n" +
            "<html>" +
                "<head>" +
                    "<meta charset=\"utf-8\">" +
                    "<title>Bar</title>" +
                "</head>" +
                "<body>" +
                    "<h1>Bar</h1>" +
                    "<p>lorem ipsum dolor sit amet</p>" +
                "</body>" +
            "</html>",
            result);
    }

    @Test
    public void bootstrapSample_should_be_rendered_correct() {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Bootstrap");

        String result = render("BootstrapSample", parameters);

        assertEquals(
            "<!DOCTYPE html>\n" +
            "<html>" +
                "<head>" +
                    "<meta charset=\"utf-8\">" +
                    "<title>Bootstrap</title>" +
                    "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">" +
                "</head>" +
                "<body class=\"container-fluid\">" +
                    "<h1>Bootstrap</h1>" +
                    "<div class=\"panel panel-default\">" +
                        "<div class=\"panel-heading\">" +
                            "<h3 class=\"panel-title\">Welcome</h3>" +
                        "</div>" +
                        "<div class=\"panel-body\">" +
                            "<p>lorem ipsum</p>" +
                            "<ul class=\"list-group\">" +
                                "<li class=\"list-group-item\">foo</li>" +
                                "<li class=\"list-group-item\"><em>bar</em></li>" +
                                "<li class=\"list-group-item\">baz</li>" +
                            "</ul>" +
                            "<p>dolor sit amet</p>" +
                        "</div>" +
                    "</div>" +
                "</body>" +
            "</html>",
            result);
    }

    private String render(String view, Map<String, ?> parameters) {
        final StringComplateStream stream = new StringComplateStream();
        renderer.render(view, parameters, stream);
        return stream.getContent();
    }
}
