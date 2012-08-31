package fi.vincit.jmobster.processor.defaults.validator;

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseValidatorFactoryTest {

    public static class TestValidatorFactory extends BaseValidatorFactory {
    }

    @Test
    public void testCreateValidator() {
        TestValidatorFactory factory = new TestValidatorFactory();
        ValidatorConstructor constructor = mock(ValidatorConstructor.class);
        factory.setValidator(constructor);

        final List<FieldAnnotation> annotationsIn = Collections.emptyList();
        final Validator validatorOut = mock(Validator.class);
        when(constructor.construct(eq(annotationsIn))).thenReturn(validatorOut);

        final List<Validator> validators = factory.createValidators(annotationsIn);
        assertEquals(validators.size(), 1);
    }

    @Test
    public void testCreateValidatorNotFound() {
        TestValidatorFactory factory = new TestValidatorFactory();
        ValidatorConstructor constructor = mock(ValidatorConstructor.class);
        factory.setValidator(constructor);

        final List<FieldAnnotation> annotationsIn = Collections.emptyList();
        when(constructor.construct(eq(annotationsIn))).thenReturn(null);

        final List<Validator> validators = factory.createValidators(annotationsIn);
        assertEquals(validators.size(), 0);
    }

}
