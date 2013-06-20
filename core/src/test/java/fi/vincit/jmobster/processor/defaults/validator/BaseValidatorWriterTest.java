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

import fi.vincit.jmobster.exception.InvalidImplementationError;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.util.Optional;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.test.TestUtil;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseValidatorWriterTest {

    public static class Annotations {
        @NotNull
        @Size
        public String foo;
    }

    public abstract static class TestBaseValidatorWriter extends BaseValidatorWriter<LanguageContext<DataWriter>, DataWriter> {
    }

    @Test
    public void testWrite_OnlyRequired() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public boolean allParamsNotNull = false;
            public void write(NotNull notNull) {
                this.wasCalled = true;
                this.allParamsNotNull = notNull != null;
            }
        }

        TestWriter writer = new TestWriter();
        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        writer.write(Arrays.asList(notNull));

        assertThat(writer.wasCalled, is(true));
        assertThat(writer.allParamsNotNull, is(true));
    }

    @Test
    public void testWrite_RequiredAndOptional() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public boolean allParamsNotNull = false;
            public void write(NotNull notNull, Optional<Size> size) {
                this.wasCalled = true;
                this.allParamsNotNull = notNull != null && size != null && size.isPresent();
            }
        }

        TestWriter writer = new TestWriter();
        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        Size size = (Size)TestUtil.getAnnotationFromClass(Annotations.class, 0, 1);
        writer.write(Arrays.asList(notNull, size));

        assertThat(writer.wasCalled, is(true));
        assertThat(writer.allParamsNotNull, is(true));
    }

    @Test
    public void testWrite_RequiredAndOptional_AlternativeOrder() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public boolean allParamsNotNull = false;
            public void write(Optional<Size> size, NotNull notNull) {
                this.wasCalled = true;
                this.allParamsNotNull = notNull != null && size != null && size.isPresent();
            }
        }

        TestWriter writer = new TestWriter();
        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        Size size = (Size)TestUtil.getAnnotationFromClass(Annotations.class, 0, 1);
        writer.write(Arrays.asList(notNull, size));

        assertThat(writer.wasCalled, is(true));
        assertThat(writer.allParamsNotNull, is(true));
    }

    @Test
    public void testWrite_RequiredAndOptional_NoOptional() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public boolean allParamsNotNull = false;
            public void write(NotNull notNull, Optional<Size> size) {
                this.wasCalled = true;
                this.allParamsNotNull = notNull != null && size != null && !size.isPresent();
            }
        }

        TestWriter writer = new TestWriter();
        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        writer.write(Arrays.asList(notNull));

        assertThat(writer.wasCalled, is(true));
        assertThat(writer.allParamsNotNull, is(true));
    }

    @Test
    public void testWrite_RequiredAndOptional_AllOptional() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public Optional notNull;
            public Optional size;
            public void write(Optional<NotNull> notNull, Optional<Size> size) {
                this.wasCalled = true;
                this.notNull = notNull;
                this.size = size;
            }
        }

        TestWriter writer = new TestWriter();
        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        Size size = (Size)TestUtil.getAnnotationFromClass(Annotations.class, 0, 1);
        writer.write(Arrays.asList(notNull, size));

        assertThat(writer.wasCalled, is(true));

        assertThat(writer.notNull, notNullValue());
        assertThat(writer.notNull.isPresent(), is(true));

        assertThat(writer.size, notNullValue());
        assertThat(writer.size.isPresent(), is(true));
    }

    @Test
    public void testWrite_RequiredAndOptional_AllOptional_NothingGiven() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public void write(Optional<NotNull> notNull, Optional<Size> size) {
                this.wasCalled = true;
            }
        }

        TestWriter writer = new TestWriter();
        writer.write(Collections.EMPTY_LIST);

        assertThat(writer.wasCalled, is(false));
    }

    @Test
    public void testWrite_NoAnnotations() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public boolean allParamsNotNull = false;
            public void write(NotNull notNull, Optional<Size> size) {
                this.wasCalled = true;
                this.allParamsNotNull = notNull != null && size != null && !size.isPresent();
            }
        }

        TestWriter writer = new TestWriter();
        writer.write(Collections.EMPTY_LIST);

        assertThat(writer.wasCalled, is(false));
        assertThat(writer.allParamsNotNull, is(false));
    }

    @Test
    public void testWrite_NoParameters() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean wasCalled = false;
            public void write() {
                this.wasCalled = true;
            }
        }

        TestWriter writer = new TestWriter();
        writer.write(Collections.EMPTY_LIST);

        assertThat(writer.wasCalled, is(true));
    }

    @Test(expected = InvalidImplementationError.class)
    public void testWrite_TooManyWriteMethods() {
        class TestWriter extends TestBaseValidatorWriter {
            public void write(NotNull notNull, Optional<Size> size) {
            }
            public void write(NotNull notNull) {
            }
        }

        new TestWriter();
    }

    @Test(expected = InvalidImplementationError.class)
    public void testWrite_NoWriteMethods() {
        class TestWriter extends TestBaseValidatorWriter {
        }

        new TestWriter();
    }

    @Test
    public void testWrite_Context() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean contextPresent = false;
            public boolean writerPresent = false;
            public void write(NotNull notNull, Optional<Size> size) {
                contextPresent = getContext() != null;
                writerPresent = getWriter() != null;
            }
        }

        TestWriter writer = new TestWriter();
        LanguageContext<DataWriter> mockContext = mock(LanguageContext.class);
        DataWriter mockDataWriter = mock(DataWriter.class);
        when(mockContext.getWriter()).thenReturn(mockDataWriter);
        writer.setContext(mockContext);

        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        Size size = (Size)TestUtil.getAnnotationFromClass(Annotations.class, 0, 1);
        writer.write(Arrays.asList(notNull, size));

        assertThat(writer.contextPresent, is(true));
        assertThat(writer.writerPresent, is(true));

        assertThat(writer.getContext(), is(mockContext));
        assertThat(writer.getWriter(), is(mockDataWriter));
    }

    @Test
    public void testWrite_Status() {
        class TestWriter extends TestBaseValidatorWriter {
            public boolean status = false;
            public void write(NotNull notNull, Optional<Size> size) {
                status = getItemStatus() != null;
            }
        }

        TestWriter writer = new TestWriter();
        writer.setItemStatus( ItemStatuses.first() );

        NotNull notNull = (NotNull)TestUtil.getAnnotationFromClass(Annotations.class, 0, 0);
        Size size = (Size)TestUtil.getAnnotationFromClass(Annotations.class, 0, 1);
        writer.write(Arrays.asList(notNull, size));

        assertThat(writer.status, is(true));
    }


    public static class TestWriterOneOptional extends TestBaseValidatorWriter {
        public void write(NotNull notNull, Optional<Size> size) {
        }
    }

    public static class TestWriterBothOptional extends TestBaseValidatorWriter {
        public void write(Optional<NotNull> notNull, Optional<Size> size) {
        }
    }

    @Test
    public void testSupportsAnnotation() {
        TestWriterOneOptional writer = new TestWriterOneOptional();

        NotNull notNull = mockAnnotation( NotNull.class );
        Size size = mockAnnotation( Size.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(notNull, size) ), is( true ) );
    }

    @Test
    public void testSupportsAnnotation_OnlyRequired() {
        TestWriterOneOptional writer = new TestWriterOneOptional();

        NotNull notNull = mockAnnotation( NotNull.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(notNull) ), is( true ) );
    }

    @Test
    public void testSupportsAnnotation_OnlyOptional() {
        TestWriterOneOptional writer = new TestWriterOneOptional();

        Size size = mockAnnotation( Size.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(size) ), is( false ) );
    }

    @Test
    public void testSupportsAnnotation_AllOptional_OnlyOptional() {
        TestWriterBothOptional writer = new TestWriterBothOptional();

        NotNull notNull = mockAnnotation( NotNull.class );
        Size size = mockAnnotation( Size.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(notNull, size) ), is( true ) );
    }

    @Test
    public void testSupportsAnnotation_AllOptional_OneOptional() {
        TestWriterBothOptional writer = new TestWriterBothOptional();

        Size size = mockAnnotation( Size.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(size) ), is( true ) );
    }

    @Test
    public void testSupportsAnnotation_NoSupport() {
        TestWriterOneOptional writer = new TestWriterOneOptional();

        Null nullValidator = mockAnnotation( Null.class );
        assertThat( writer.supportsAnnotations( Arrays.asList(nullValidator) ), is( false ) );
    }

    @Test
    public void testSupportsAnnotation_Nothing() {
        TestWriterOneOptional writer = new TestWriterOneOptional();
        assertThat( writer.supportsAnnotations( Collections.EMPTY_LIST ), is( false ) );
    }

    @Test
    public void testSupportsAnnotation_AllOptional_Nothing() {
        TestWriterBothOptional writer = new TestWriterBothOptional();
        assertThat( writer.supportsAnnotations( Collections.EMPTY_LIST ), is( false ) );
    }


    private static <T extends Annotation> T mockAnnotation(Class<T> type) {
        T t = mock( type );
        when( t.annotationType() ).thenReturn( (Class)type );
        return t;
    }
}
