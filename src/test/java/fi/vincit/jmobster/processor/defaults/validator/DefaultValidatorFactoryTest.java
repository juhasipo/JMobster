package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class DefaultValidatorFactoryTest {
    @Test
    public void testSize() {
        DefaultValidatorFactory factory = new DefaultValidatorFactory();
        class SizeClass {
            @Size(min=1, max=3) public int i;
        }
        /*
        Validator validator = factory.createValidators( null );
        Assert.assertNotNull( validator );
        Assert.assertEquals( Size.class, validator.getType() );
        */
    }

    private Annotation getAnnotation(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Annotation[] annotations = fields[0].getAnnotations();
        return annotations[0];
    }
}
