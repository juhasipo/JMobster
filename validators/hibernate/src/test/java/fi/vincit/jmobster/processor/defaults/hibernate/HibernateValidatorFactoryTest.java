package fi.vincit.jmobster.processor.defaults.hibernate;

/*
 * Copyright 2012 Juha Siponen
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
import fi.vincit.jmobster.util.test.TestUtil;
import org.hibernate.validator.constraints.Length;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

public class HibernateValidatorFactoryTest {
    @Test
    public void testLengthValidator() {
        HibernateValidatorFactory factory = new HibernateValidatorFactory();
        class LengthClass {
            @Length(min = 1, max = 3) public String s;
        }

        Annotation[] annotations = TestUtil.getAnnotationsFromClassField(LengthClass.class, 0);
        Collection<FieldAnnotation> fieldAnnotations = FieldAnnotation.convertToFieldAnnotations(annotations);
        List<Validator> validators = factory.createValidators( fieldAnnotations );

        Assert.assertNotNull(validators);
        Assert.assertEquals( 1, validators.size() );
        Assert.assertEquals( LengthValidator.class, validators.get( 0 ).getType() );
    }
}
