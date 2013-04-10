package fi.vincit.jmobster.processor.frameworks.html5;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ModelConfigurationTest {

    @Test
    public void testGetModelFieldConfiguration() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ModelConfiguration.ModelFieldConfigurationImmutable field =
                configuration.getModelFieldConfiguration(String.class, "test");
        assertThat(field, notNullValue());
    }

    @Test
    public void testGetModelFieldConfiguration_ModelNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ModelConfiguration.ModelFieldConfigurationImmutable field =
                configuration.getModelFieldConfiguration(Long.class, "test");
        assertThat(field, nullValue());
    }

    @Test
    public void testGetModelFieldConfiguration_FieldNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ModelConfiguration.ModelFieldConfigurationImmutable field =
                configuration.getModelFieldConfiguration(String.class, "test2");
        assertThat(field, nullValue());
    }

    @Test
    public void testHasConfiguration() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(String.class, "test");
        assertThat(field, is(true));
    }

    @Test
    public void testHasConfiguration_ModelNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(Long.class, "test");
        assertThat(field, is(false));
    }

    @Test
    public void testHasConfiguration_FieldNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(String.class, "test2");
        assertThat(field, is(false));
    }

    @Test
    public void testSetTypeAndClasses_TwoModels() {
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

        ModelConfiguration.ModelFieldConfigurationImmutable field1 =
                configuration.getModelFieldConfiguration(String.class, "test");
        ModelConfiguration.ModelFieldConfigurationImmutable field2 =
                configuration.getModelFieldConfiguration(Long.class, "test2");

        assertThat(field1, notNullValue());
        assertThat(field1.getType(), is("textarea"));
        assertThat(field1.getClasses(), is("foo hidden"));

        assertThat(field2, notNullValue());
        assertThat(field2.getType(), is("number"));
        assertThat(field2.getClasses(), is("foo hidden"));
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
