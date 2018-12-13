package com.github.complate.nashorn.renderer;

import com.github.complate.core.ComplateRenderer;
import com.github.complate.core.ComplateScript;
import com.github.complate.core.script.ClasspathComplateScript;
import com.github.complate.core.stream.StringComplateStream;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.complate.nashorn.renderer.NashornComplateRenderer.nashornComplateRenderer;
import static org.assertj.core.api.Assertions.assertThat;

public class NashornComplateRendererTest {

    @Test
    public void render_with_valid_input_should_work() {
        ComplateScript script = new ClasspathComplateScript("/bundle.js");

        ComplateRenderer sut = nashornComplateRenderer(script).build();

        StringComplateStream stream = new StringComplateStream();

        String view = "my-site-index";
        Map<String, String> parameters =
            Collections.singletonMap("title", "ვეპხის ტყაოსანი შოთა რუსთაველი");

        sut.render(view, parameters, stream);

        assertThat(stream.getContent())
            .isEqualTo("my-site-index\n" +
                "ვეპხის ტყაოსანი შოთა რუსთაველი\n");
    }

    @Test
    public void render_should_provide_global_object_for_script() {
        ComplateScript script = new ClasspathComplateScript("/bundle-global-obj.js");

        ComplateRenderer sut = nashornComplateRenderer(script).build();

        StringComplateStream stream = new StringComplateStream();

        sut.render("", null, stream);

        assertThat(stream.getContent())
            .isEqualTo("[object global]\n");
    }

    @Test
    public void render_should_provide_bindings_for_script() {
        ComplateScript script = new ClasspathComplateScript("/bundle-bindings-test.js");

        Map<String, Object> bindings = new HashMap<>();
        bindings.put("firstBinding", new TestBinding("First binding says"));
        bindings.put("secondBinding", new TestBinding("Second binding says"));

        ComplateRenderer sut = nashornComplateRenderer(script)
            .withBindings(bindings)
            .build();

        StringComplateStream stream = new StringComplateStream();

        sut.render("", null, stream);

        assertThat(stream.getContent())
            .isEqualTo("First binding says: Hello World!\n" +
                "Second binding says: Bye World!\n");
    }

    public static final class TestBinding {

        private final String prefix;

        public TestBinding(String prefix) {
            this.prefix = prefix;
        }

        public String run(String s) {
            return String.format("%s: %s World!", prefix, s);
        }
    }
}
