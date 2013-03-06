package fi.vincit.jmobster.processor.defaults.validator;

import fi.vincit.jmobster.annotation.AfterInit;
import fi.vincit.jmobster.annotation.BeforeInit;
import fi.vincit.jmobster.annotation.InitMethod;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.util.Optional;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import org.junit.Test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseValidatorTest {
    @Test
    public void testDefaultType() throws Exception {
        class TestClass1 extends BaseValidator {
            TestClass1() {}
            @Override public void init( AnnotationBag annotationBag ) {}
        }

        TestClass1 testClass1 = new TestClass1();
        assertEquals(testClass1.getType(), TestClass1.class);
    }

    @Test
    public void testSetType() throws Exception {
        class TestClass1 extends BaseValidator {
            TestClass1() {}
            @Override public void init( AnnotationBag annotationBag ) {}
        }

        TestClass1 testClass1 = new TestClass1();
        testClass1.setType(String.class);
        assertEquals( testClass1.getType(), String.class );
    }

    @Test
    public void testInitWithOneParam() {
        class TestClass1 extends  BaseValidator {
            Size size;
            TestClass1() {}
            @InitMethod public void init(Size size) {
                this.size = size;
            }
        }

        TestClass1 c = new TestClass1();
        Size s = mockAnnotation(Size.class);
        c.init(AnnotationBag.forAnnotations(new FieldAnnotation(s)));

        assertThat(c.size, sameInstance(s));
    }

    @Test
    public void testInitWithThreeParams() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, NotNull notNull) {
                this.min = min;
                this.max = max;
                this.notNull = notNull;
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);
        NotNull notNull = mockAnnotation(NotNull.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max),
                new FieldAnnotation(notNull)));

        assertThat(c.min, sameInstance(min));
        assertThat(c.max, sameInstance(max));
        assertThat(c.notNull, sameInstance(notNull));
    }

    @Test
    public void testInitWithOptionalGiven() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, Optional<NotNull> notNull) {
                this.min = min;
                this.max = max;
                this.notNull = notNull.getValue();
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);
        NotNull notNull = mockAnnotation(NotNull.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max),
                new FieldAnnotation(notNull)));

        assertThat(c.min, sameInstance(min));
        assertThat(c.max, sameInstance(max));
        assertThat(c.notNull, sameInstance(notNull));
    }

    @Test
    public void testInitWithExtendedOptionalGiven() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, Optional<? extends NotNull> notNull) {
                this.min = min;
                this.max = max;
                this.notNull = notNull.getValue();
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);
        NotNull notNull = mockAnnotation(NotNull.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max),
                new FieldAnnotation(notNull)));

        assertThat(c.min, sameInstance(min));
        assertThat(c.max, sameInstance(max));
        assertThat(c.notNull, sameInstance(notNull));
    }

    @Test
    public void testInitWithOptionalNotGiven() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;
            boolean notNullPresent;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, Optional<NotNull> notNull) {
                this.min = min;
                this.max = max;
                this.notNull = null;
                notNullPresent = notNull.isPresent();
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max)));

        assertThat(c.min, sameInstance(min));
        assertThat(c.max, sameInstance(max));
        assertThat(c.notNull, nullValue());
        assertThat(c.notNullPresent, is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithInvalidGeneric() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, List<NotNull> notNull) {
                this.min = min;
                this.max = max;
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max)));
    }

    @Test()
    public void testInitWithInvalidOptional() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, Optional<?> notNull) {
                this.min = min;
                this.max = max;
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max)));

        assertThat(c.min, nullValue());
        assertThat(c.max, nullValue());
        assertThat(c.notNull, nullValue());
    }

    @Test()
    public void testInitWithInvalidOptional2() {
        class TestClass1 extends  BaseValidator {
            Min min;
            NotNull notNull;
            Max max;

            TestClass1() {}
            @InitMethod public void init(Min min, Max max, Optional notNull) {
                this.min = min;
                this.max = max;
            }
        }

        TestClass1 c = new TestClass1();
        Min min = mockAnnotation(Min.class);
        Max max = mockAnnotation(Max.class);

        c.init(AnnotationBag.forAnnotations(
                new FieldAnnotation(min),
                new FieldAnnotation(max)));

        assertThat(c.min, nullValue());
        assertThat(c.max, nullValue());
        assertThat(c.notNull, nullValue());
    }


    static int beforeAfterTestOrder = 0;
    @Test
    public void testBeforeAndAfterInit() {

        class BeforeAfterTestClass extends  BaseValidator {
            int afterCalled;
            int beforeCalled;
            BeforeAfterTestClass() {}
            @BeforeInit public void initBefore() { beforeCalled = beforeAfterTestOrder; ++beforeAfterTestOrder; }
            @AfterInit public void initAfter() { afterCalled = beforeAfterTestOrder; ++beforeAfterTestOrder; }
        }

        BeforeAfterTestClass c = new BeforeAfterTestClass();
        c.init(new AnnotationBag());

        assertThat(c.beforeCalled, sameInstance(0));
        assertThat(c.afterCalled, sameInstance(1));
    }

    @Test
    public void testBeforeInit() {
        class TestClass1 extends  BaseValidator {
            boolean called;
            TestClass1() {}
            @BeforeInit public void init() { called = true; }
        }

        TestClass1 c = new TestClass1();
        c.init(new AnnotationBag());

        assertThat(c.called, sameInstance(true));
    }

    @Test
    public void testAfterInit() {
        class TestClass1 extends  BaseValidator {
            boolean called;
            TestClass1() {}
            @AfterInit public void init() { called = true; }
        }

        TestClass1 c = new TestClass1();
        c.init(new AnnotationBag());

        assertThat(c.called, sameInstance(true));
    }




    @Test
    public void testMockAnnotation() {
        Size s = mockAnnotation(Size.class);

        assertThat(s.annotationType().equals(Size.class), is(true));
    }

    private static <T extends Annotation> T mockAnnotation(Class<T> type) {
        Class typeClass = type;
        T typeMock = mock(type);
        when(typeMock.annotationType()).thenReturn(typeClass);
        return typeMock;
    }
}
