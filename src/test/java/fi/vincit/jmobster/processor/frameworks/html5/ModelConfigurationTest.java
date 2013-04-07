package fi.vincit.jmobster.processor.frameworks.html5;

import org.junit.Test;

public class ModelConfigurationTest {

    @Test
    public void test() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                    .field("test")
                        .type("textarea")
                        .classes("foo hidden")
                .model(Long.class)
                    .field("test2")
                        .type("number")
                        .classes("foo hidden");
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFieldAfterModel() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                    .field("test")
                        .type("textarea")
                        .classes("foo hidden")
                .model(Long.class)
                    .type("number");
    }

    @Test(expected = IllegalStateException.class)
    public void testNoModelSet() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.field("test");
    }
}
