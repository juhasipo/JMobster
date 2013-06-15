package fi.vincit.jmobster.processor.defaults.validator;

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
import fi.vincit.jmobster.util.groups.GroupManager;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidatorFilterTest {
    @Test
    public void testGroup_Match() {
        GroupManager manager = mock(GroupManager.class);
        ValidatorFilter filter = new ValidatorFilter(manager);

        FieldAnnotation annotation = mock(FieldAnnotation.class);
        Annotation mockAnnotation = mock(Annotation.class);
        when(annotation.getAnnotation()).thenReturn(mockAnnotation);
        when(manager.match(annotation)).thenReturn(true);

        Collection<Annotation> filtered = filter.filterByGroup(Arrays.asList(annotation));

        assertThat(filtered.size(), is(1));
    }

    @Test
    public void testGroup_DoesNotMatch() {
        GroupManager manager = mock(GroupManager.class);
        ValidatorFilter filter = new ValidatorFilter(manager);

        FieldAnnotation annotation = mock(FieldAnnotation.class);

        Collection<Annotation> filtered = filter.filterByGroup(Arrays.asList(annotation));

        assertThat(filtered.size(), is(0));
    }
}
