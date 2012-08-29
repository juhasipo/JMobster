package fi.vincit.jmobster.processor.defaults;/*
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

import fi.vincit.jmobster.annotation.IgnoreField;
import fi.vincit.jmobster.exception.CannotAccessDefaultConstructorError;
import fi.vincit.jmobster.exception.DefaultConstructorMissingError;
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import javax.validation.constraints.Min;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

import static fi.vincit.jmobster.util.TestUtil.assertFieldFound;
import static fi.vincit.jmobster.util.TestUtil.assertFieldNotFound;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class DefaultModelFieldFactoryTest {

    private FieldValueConverter valueConverter;

    private DefaultModelFieldFactory getFieldScanner(DefaultModelFieldFactory.FieldScanMode scanMode) {
        valueConverter = mock(FieldValueConverter.class);
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        when(validatorScanner.getValidators(any(Field.class)))
                .thenReturn( TestUtil.collectionFromObjects( mock( Validator.class ) ) );
        when(validatorScanner.getValidators(any(PropertyDescriptor.class)))
                .thenReturn(TestUtil.collectionFromObjects(mock(Validator.class)));
        return new DefaultModelFieldFactory(scanMode, valueConverter, validatorScanner);
    }

    public static class SimpleTestClass {
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }



    @Test
    public void testGetFields() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS);
        List<ModelField> models = fs.getFields( SimpleTestClass.class );

        assertFieldFound(models, "publicLongField");
        assertFieldFound(models, "protectedIntegerField");
        assertFieldFound(models, "privateStringField");
    }

    public static class SimpleIgnoreTestClass {
        @IgnoreField
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testIgnoreField() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( SimpleIgnoreTestClass.class );

        assertFieldNotFound( models, "publicLongField" );
        assertFieldFound(models, "protectedIntegerField");
        assertFieldFound(models, "privateStringField");
    }

    public static class TestClassWithValidation {
        public Long publicLongField;
        @Min(1)
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testFieldWithValidation() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithValidation.class );

        assertFieldFound( models, "publicLongField" );
        int fieldIndexWithAnnotations = assertFieldFound(models, "protectedIntegerField");
        assertFieldFound(models, "privateStringField");

        ModelField fieldWithAnnotations = models.get(fieldIndexWithAnnotations);
        assertEquals(1, fieldWithAnnotations.getValidators().size());
    }

    public static class TestClassNoDefaultConstructor {
        public TestClassNoDefaultConstructor( Long publicLongField, Integer protectedIntegerField, String privateStringField ) {
            this.publicLongField = publicLongField;
            this.protectedIntegerField = protectedIntegerField;
            this.privateStringField = privateStringField;
        }

        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test(expected = DefaultConstructorMissingError.class)
    public void testNoDefaultConstructor() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.getFields( TestClassNoDefaultConstructor.class );
    }

    public static class TestClassPrivateDefaultConstructor {
        private TestClassPrivateDefaultConstructor() {
        }

        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test(expected = CannotAccessDefaultConstructorError.class)
    public void testPrivateDefaultConstructor() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.getFields( TestClassPrivateDefaultConstructor.class );
    }

    public static class TestClassWithStaticMember {
        public static int staticMember;
        public final int finalInt = 1;
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testStaticAndFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldFound( models, "finalInt" );
    }

    @Test
    public void testAllowFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldFound( models, "finalInt" );
    }

    @Test
    public void testDontAllowFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(false);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldNotFound( models, "finalInt" );
    }

    @Test
    public void testAllowStaticMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(false);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldFound( models, "staticMember" );
    }

    public static class TestClassWithStaticFinalMember {
        public static final int staticMember = 10;
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testStaticFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
    }

    @Test
    public void testAllowStaticFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(true);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFound(models, "publicLongField");
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldFound( models, "staticMember" );
    }


    public static class SimpleTestGetterClass {
        private String string1 = "string1";
        private String string2 = "string2";

        private Long longValue = 42L;

        public String getCombinedString() {
            return string1 + string2;
        }

        @Min(1)
        public Long getLongValue() {
            return longValue;
        }

    }

    @Test
    public void testGetterScanning() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( SimpleTestGetterClass.class );
        assertFieldNotFound(models, "string1");
        assertFieldNotFound(models, "string2");
        assertFieldFound(models, "combinedString");
        assertFieldFound(models, "longValue");

        verify( valueConverter, times(1)).convert(eq(String.class), eq("string1string2"));
        verify( valueConverter, times(1)).convert(eq(Long.class), eq(42L));
    }

    @Test
    public void testGetterAnnotation() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( SimpleTestGetterClass.class );

        int i = assertFieldFound(models, "longValue");
        assertEquals(1, models.get(i).getValidators().size());
    }

    public static class IgnoreBeanPropertyClass {
        public Integer getNotIgnored() {
            return 1;
        }
        @IgnoreField
        public Integer getIgnored() {
            return 2;
        }
    }
    @Test
    public void testIgnoreBeanProperty() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( IgnoreBeanPropertyClass.class );
        assertFieldNotFound(models, "ugnored");
        assertFieldFound(models, "notIgnored");
    }


    public static class VisibilityTestGetterClass {
        public Integer getPublic() {
            return 9;
        }
        protected Integer getProtected() {
            return 10;
        }

        private Integer getPrivate() {
            return 11;
        }
    }

    @Test
    public void testBeanVisibility() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( VisibilityTestGetterClass.class );
        assertFieldNotFound(models, "protected");
        assertFieldNotFound(models, "private");
        assertFieldFound(models, "public");
    }

    public static class BeanWithStaticAndFinalProperties {
        public Integer getNormal() { return -1; }
        public static final Integer getStaticFinal() { return 0; }
        public final Integer getFinal() { return 1; }
        public static Integer getStatic() { return 2; }
    }

    @Test
    public void testBeanExtraOptionsDefault() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( BeanWithStaticAndFinalProperties.class );
        assertFieldFound(models, "normal");
        assertFieldNotFound( models, "staticFinal" );
        assertFieldFound( models, "final" );
        assertFieldNotFound( models, "static" );
    }

    @Test
    public void testBeanExtraOptionsAllowAll() {
        DefaultModelFieldFactory fs = getFieldScanner( DefaultModelFieldFactory.FieldScanMode.BEAN_PROPERTY );
        fs.setAllowFinalFields(true);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( BeanWithStaticAndFinalProperties.class );
        assertFieldFound(models, "normal");
        assertFieldNotFound( models, "staticFinal" );
        assertFieldFound( models, "final" );
        assertFieldNotFound( models, "static" );
    }
}
