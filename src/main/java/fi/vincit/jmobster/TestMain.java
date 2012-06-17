package fi.vincit.jmobster;

import fi.vincit.jmobster.annotation.IgnoreDefaultValue;
import fi.vincit.jmobster.converter.valueconverters.ConverterMode;
import fi.vincit.jmobster.converter.JavaToJSValueConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestMain {
    private static final Logger LOG = LoggerFactory
            .getLogger( TestMain.class );

    public static class InnerClass {
        @NotNull
        public String innerString = "Inner string";
    }

    @SuppressWarnings( "MismatchedReadAndWriteOfArray" )
    public static class TestModel {

        @IgnoreDefaultValue
        @Size(min = 5)
        private String ignoredDefaultValue = "test string";

        @NotNull
        @Size(min = 5, max = 255)
        private String string = "test string";

        @Size(min = 5)
        @Pattern(regexp = "[a-z ]+")
        private String string2 = "test string";

        @Min(10)
        Long longValue = 42L;

        @Size(min = 5, max = 255)
        private long[] longArray = {1L, 2L};

        @Size()
        public String[] stringArray = {"Foo", "Bar", "FooBar", "123"};

        @NotNull
        protected List<Long> longList = new ArrayList<Long>();

        private Map<Integer, String> intStringMap = new TreeMap<Integer, String>();

        public TestModel() {
            longList.add(1L);
            longList.add(100L);

            intStringMap.put(1, "1 value");
            intStringMap.put(2, "2 value");
            intStringMap.put(100, "100 value");
        }

    }

    public static void main(String[] args) {
        LOG.debug("Start");
        ModelGenerator mg = new ModelGenerator(new ModelProcessor("models.js"), new JavaToJSValueConverter( ConverterMode.ALLOW_NULL ));

        mg.process(TestModel.class);
    }
}
