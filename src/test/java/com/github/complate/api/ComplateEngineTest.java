package com.github.complate.api;

import com.github.complate.impl.io.ClasspathComplateScript;
import com.github.complate.impl.io.StringComplateStream;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplateEngineTest {

    @Test
    public void invoke_with_valid_input_should_work() {
        ComplateEngine sut = ComplateEngine.create();

        ComplateScript script = new ClasspathComplateScript("/bundle.js");
        StringComplateStream stream = new StringComplateStream();

        String tag = "my-site-index";
        Map<String, String> parameters =
            Collections.singletonMap("title", "ვეპხის ტყაოსანი შოთა რუსთაველი");

        sut.invoke(script, stream, tag, parameters);

        assertThat(stream.getContent())
            .isEqualTo("my-site-index\n" +
                       "ვეპხის ტყაოსანი შოთა რუსთაველი\n");
    }

    @Test
    public void invoke_should_provide_global_object_for_script() {
        ComplateEngine sut = ComplateEngine.create();

        ComplateScript script = new ClasspathComplateScript("/bundle-global-obj.js");
        StringComplateStream stream = new StringComplateStream();

        sut.invoke(script, stream, "");

        assertThat(stream.getContent())
            .isEqualTo("[object global]\n");
    }

    @Test
    public void invoke_should_provide_bindings_for_script() {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("firstBinding", new TestBinding("First binding says"));
        bindings.put("secondBinding", new TestBinding("Second binding says"));

        ComplateEngine sut = ComplateEngine.create(bindings);

        ComplateScript script = new ClasspathComplateScript("/bundle-bindings-test.js");
        StringComplateStream stream = new StringComplateStream();

        sut.invoke(script, stream, "");

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
