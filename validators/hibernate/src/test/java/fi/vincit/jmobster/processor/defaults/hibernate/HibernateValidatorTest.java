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

import fi.vincit.jmobster.util.test.TestUtil;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HibernateValidatorTest {
    @Test
    public void testLengthValidator_MinAndMax() {
        class LengthClass { @Length(min=1,max=3) public String s; }

        Annotation lengthAnnotation = TestUtil.getAnnotationFromClass(LengthClass.class, 0, 0);
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( lengthAnnotation );

        LengthValidator lengthValidator = new LengthValidator();
        lengthValidator.init(annotationBag);

        assertThat(lengthValidator.hasMin(), is(true));
        assertThat(lengthValidator.hasMax(), is(true));
        assertThat(lengthValidator.getMin(), is(1));
        assertThat(lengthValidator.getMax(), is(3));
    }

    @Test
    public void testLengthValidator_Min() {
        class LengthClass { @Length(min=1) public String s; }

        Annotation lengthAnnotation = TestUtil.getAnnotationFromClass(LengthClass.class, 0, 0);
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( lengthAnnotation );

        LengthValidator lengthValidator = new LengthValidator();
        lengthValidator.init(annotationBag);

        assertThat(lengthValidator.hasMin(), is(true));
        assertThat(lengthValidator.hasMax(), is(false));
        assertThat(lengthValidator.getMin(), is(1));
    }

    @Test
    public void testLengthValidator_Max() {
        class LengthClass { @Length(max=3) public String s; }

        Annotation lengthAnnotation = TestUtil.getAnnotationFromClass(LengthClass.class, 0, 0);
        AnnotationBag annotationBag = TestUtil.generateAnnotationBag( lengthAnnotation );

        LengthValidator lengthValidator = new LengthValidator();
        lengthValidator.init(annotationBag);

        assertThat(lengthValidator.hasMin(), is(true));
        assertThat(lengthValidator.hasMax(), is(true));
        assertThat(lengthValidator.getMin(), is(0)); // Hibernate default min is 0
        assertThat(lengthValidator.getMax(), is(3));
    }
}
