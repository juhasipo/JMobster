package fi.vincit.jmobster.processor.frameworks.configuration;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ModelConfigurationTest {

    @Test
    public void testGetModelFieldConfiguration() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ImmutableModelFieldConfiguration field =
                configuration.getModelFieldConfiguration(String.class, "test");
        Assert.assertThat(field, CoreMatchers.notNullValue());
    }

    @Test
    public void testGetModelFieldConfiguration_ModelNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ImmutableModelFieldConfiguration field =
                configuration.getModelFieldConfiguration(Long.class, "test");
        Assert.assertThat(field, CoreMatchers.nullValue());
    }

    @Test
    public void testGetModelFieldConfiguration_FieldNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        ImmutableModelFieldConfiguration field =
                configuration.getModelFieldConfiguration(String.class, "test2");
        Assert.assertThat(field, CoreMatchers.nullValue());
    }

    @Test
    public void testGetModelFieldConfiguration_FieldCalledButNotSetAnything() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test");

        ImmutableModelFieldConfiguration field =
                configuration.getModelFieldConfiguration(String.class, "test");
        Assert.assertThat(field, CoreMatchers.notNullValue());
    }

    @Test
    public void testHasConfiguration() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(String.class, "test");
        Assert.assertThat(field, CoreMatchers.is(true));
    }

    @Test
    public void testHasConfiguration_ModelNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(Long.class, "test");
        Assert.assertThat(field, CoreMatchers.is(false));
    }

    @Test
    public void testHasConfiguration_FieldNotFound() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test").type("textarea");

        boolean field =
                configuration.hasConfiguration(String.class, "test2");
        Assert.assertThat(field, CoreMatchers.is(false));
    }

    @Test
    public void testHasConfiguration_FieldCalledButNotSetAnything() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration.model(String.class).field("test");

        boolean field =
                configuration.hasConfiguration(String.class, "test");
        Assert.assertThat(field, CoreMatchers.is(true));
    }

    @Test
    public void testSetType() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                    .field("test")
                    .type("textarea");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.getType(), CoreMatchers.is("textarea"));
    }

    @Test
    public void testSetClasses() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                .field("test")
                .classes("foo bar");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.getClasses(), CoreMatchers.is("foo bar"));
    }

    @Test
    public void testSetName() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                .field("test")
                .name("name");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.getName(), CoreMatchers.is("name"));
    }

    @Test
    public void testSetName_DontUseDefaultName() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                .field("test")
                .type("foo");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.useDefaultType(), CoreMatchers.is(false));
    }

    @Test
    public void testSetNoName_CheckUseDefaultName() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                .field("test");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.useDefaultType(), CoreMatchers.is(true));
    }

    @Test
    public void testSetTypeAndClassesAndName_TwoModels() {
        ModelConfiguration configuration = new ModelConfiguration();

        configuration
                .model(String.class)
                    .field("test")
                        .type("textarea")
                        .classes("foo hidden")
                        .name("foo")
                .model(Long.class)
                    .field("test2")
                        .type("number")
                        .classes("foo hidden")
                        .name("bar");

        ImmutableModelFieldConfiguration field1 =
                configuration.getModelFieldConfiguration(String.class, "test");
        ImmutableModelFieldConfiguration field2 =
                configuration.getModelFieldConfiguration(Long.class, "test2");

        Assert.assertThat(field1, CoreMatchers.notNullValue());
        Assert.assertThat(field1.getType(), CoreMatchers.is("textarea"));
        Assert.assertThat(field1.getClasses(), CoreMatchers.is("foo hidden"));
        Assert.assertThat(field1.getName(), CoreMatchers.is("foo"));

        Assert.assertThat(field2, CoreMatchers.notNullValue());
        Assert.assertThat(field2.getType(), CoreMatchers.is("number"));
        Assert.assertThat(field2.getClasses(), CoreMatchers.is("foo hidden"));
        Assert.assertThat(field2.getName(), CoreMatchers.is("bar"));
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
