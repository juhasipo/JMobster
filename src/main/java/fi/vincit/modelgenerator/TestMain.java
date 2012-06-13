package fi.vincit.modelgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TestMain {
    private static final Logger LOG = LoggerFactory
            .getLogger( TestMain.class );

    public static class TestModel {
        @NotNull
        @Size(min = 5, max = 255)
        private String string = "test string";

        @Size(min = 5, max = 255)
        @Pattern(regexp = "[a-z ]+")
        private String string2 = "test string";

        @Min(10)
        Long longValue = 42L;
    }

    public static void main(String[] args) {
        LOG.debug("Start");
        ModelGenerator mg = new ModelGenerator(new ModelProcessor("models.js"), new FieldDefaultValueProcessor());

        mg.process(TestModel.class);
    }
}
