package fi.vincit.jmobster.processor.defaults.validator.jsr303;

/*
 * Copyright 2012-2013 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

public class JSR303ValidatorFactoryTest {
    @Test
    public void testSizeValidator() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @Size(min=1, max=3) public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( SizeValidator.class, validators.get( 0 ).getType() );
    }

    @Test
    public void testNumberRangeValidatorWithMin() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @Min(1) public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( NumberRangeValidator.class, validators.get(0).getType() );
    }

    @Test
    public void testNumberRangeValidatorWithMax() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @Max(1) public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( NumberRangeValidator.class, validators.get(0).getType() );
    }

    @Test
    public void testNumberRangeValidatorWithMinAndMax() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @Min(1) @Max(100) public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( NumberRangeValidator.class, validators.get(0).getType() );
    }

    @Test
    public void testNumberRangeValidatorWithMinAndMaxAndNotNull() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @NotNull @Min(1) @Max(100) public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 2, validators.size() );
        // Validator factory guarantees the order
        Assert.assertEquals( NumberRangeValidator.class, validators.get(0).getType() );
        Assert.assertEquals( NotNullValidator.class, validators.get(1).getType() );
    }



    @Test
    public void testPatternValidator() {
        JSR303ValidatorFactory factory = new JSR303ValidatorFactory();
        class SizeClass {
            @Pattern(regexp = "FooBar") public int i;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(SizeClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull( validators );
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( PatternValidator.class, validators.get(0).getType() );
    }


}
