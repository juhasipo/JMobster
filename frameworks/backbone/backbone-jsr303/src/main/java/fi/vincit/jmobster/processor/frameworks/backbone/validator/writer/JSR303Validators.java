package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

import fi.vincit.jmobster.processor.defaults.validator.BaseValidator;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptValidatorWriter;

import java.util.Arrays;
import java.util.Collection;

public class JSR303Validators {
    public static Collection<JavaScriptValidatorWriter<? extends BaseValidator>> get() {
        return Arrays.asList(
                new SizeValidatorWriter(),
                new PatternValidatorWriter(),
                new NumberRangeValidatorWriter(),
                new NotNullValidatorWriter()
        );
    }
}
