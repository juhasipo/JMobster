package fi.vincit.jmobster.processor.defaults;

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
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultValidatorScannerTest {
    @Test
    public void testGetValidatorsFromField() throws Exception {
        DefaultValidatorScanner validatorScanner = new DefaultValidatorScanner();

        class T {@NotNull int field;}
        Collection<FieldAnnotation> annotations = validatorScanner.getValidators(T.class.getDeclaredFields()[0]);
        assertThat(annotations.size(), is(1));
    }

    @Test
    public void testGetValidatorsFromProperty() throws Exception {
        DefaultValidatorScanner validatorScanner = new DefaultValidatorScanner();

        class T {int field; @NotNull public int getField() { return field; }}
        final BeanInfo beanInfo = Introspector.getBeanInfo( T.class );
        // getClass is in index 0. getField is in index 1
        Collection<FieldAnnotation> annotations = validatorScanner.getValidators(beanInfo.getPropertyDescriptors()[1]);
        assertThat(annotations.size(), is(1));
    }
}
