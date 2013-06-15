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

import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.*;

public class DefaultValidatorWriterSetTest {

    public static class TestValidatorWriterSet extends DefaultValidatorWriterSet<LanguageContext<DataWriter>, DataWriter> {
        public boolean beforeCalled;
        public boolean afterCalled;

        public TestValidatorWriterSet(Collection requiredValidatorWriter, Collection... validatorWriters) {
            super(requiredValidatorWriter, validatorWriters);
        }

        @Override
        protected void beforeWrite(ItemStatus status) {
            beforeCalled = true;
        }

        @Override
        protected void afterWrite(ItemStatus status) {
            afterCalled = true;
        }
    }

    @Test
    public void testWriteAnnotation() {
        ValidatorWriter mockWriter = mock(ValidatorWriter.class);
        when(mockWriter.supportsAnnotations(anyCollection())).thenReturn(true);
        Collection writers = Arrays.asList(mockWriter);

        ValidatorWriterSet validatorWriterSet =
                new TestValidatorWriterSet(writers);

        LanguageContext mockContext = mock(LanguageContext.class);
        validatorWriterSet.setLanguageContext(mockContext);

        Collection<Annotation> annotations = Arrays.asList(mock(Annotation.class));
        validatorWriterSet.write(annotations, ItemStatuses.first());

        verify(mockWriter).setContext(mockContext);
        verify(mockWriter).write(annotations);
        verify(mockWriter).setContext(null);
    }

    @Test
    public void testWriteAnnotation_BeforeCalled() {
        ValidatorWriter mockWriter = mock(ValidatorWriter.class);
        when(mockWriter.supportsAnnotations(anyCollection())).thenReturn(true);
        Collection writers = Arrays.asList(mockWriter);

        TestValidatorWriterSet validatorWriterSet =
                new TestValidatorWriterSet(writers);

        LanguageContext mockContext = mock(LanguageContext.class);
        validatorWriterSet.setLanguageContext(mockContext);

        Collection<Annotation> annotations = Arrays.asList(mock(Annotation.class));
        validatorWriterSet.write(annotations, ItemStatuses.first());

        assertThat(validatorWriterSet.beforeCalled, is(true));
    }

    @Test
    public void testWriteAnnotation_AfterCalled() {
        ValidatorWriter mockWriter = mock(ValidatorWriter.class);
        when(mockWriter.supportsAnnotations(anyCollection())).thenReturn(true);
        Collection writers = Arrays.asList(mockWriter);

        TestValidatorWriterSet validatorWriterSet =
                new TestValidatorWriterSet(writers);

        LanguageContext mockContext = mock(LanguageContext.class);
        validatorWriterSet.setLanguageContext(mockContext);

        Collection<Annotation> annotations = Arrays.asList(mock(Annotation.class));
        validatorWriterSet.write(annotations, ItemStatuses.first());

        assertThat(validatorWriterSet.afterCalled, is(true));
    }

    @Test
    public void testWriteAnnotation_NoSupport() {
        ValidatorWriter mockWriter = mock(ValidatorWriter.class);
        when(mockWriter.supportsAnnotations(anyCollection())).thenReturn(false);
        Collection writers = Arrays.asList(mockWriter);

        ValidatorWriterSet validatorWriterSet =
                new TestValidatorWriterSet(writers);

        LanguageContext mockContext = mock(LanguageContext.class);
        validatorWriterSet.setLanguageContext(mockContext);

        Collection<Annotation> annotations = Arrays.asList(mock(Annotation.class));
        validatorWriterSet.write(annotations, ItemStatuses.first());

        verify(mockWriter, never()).setContext(any(LanguageContext.class));
        verify(mockWriter, never()).write(anyCollection());
        verify(mockWriter, never()).setContext(null);
    }
}
