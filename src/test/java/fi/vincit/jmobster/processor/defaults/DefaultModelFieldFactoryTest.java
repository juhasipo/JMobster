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
import fi.vincit.jmobster.processor.FieldScanMode;
import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import javax.validation.constraints.Min;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fi.vincit.jmobster.util.TestUtil.assertFieldFoundOnce;
import static fi.vincit.jmobster.util.TestUtil.assertFieldNotFound;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultModelFieldFactoryTest {

    private DefaultModelFieldFactory getFieldScanner(FieldScanMode scanMode) {
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        when(validatorScanner.getValidators(any(Field.class)))
                .thenReturn( TestUtil.collectionFromObjects( mock( Validator.class ) ) );
        when(validatorScanner.getValidators(any(PropertyDescriptor.class)))
                .thenReturn(TestUtil.collectionFromObjects(mock(Validator.class)));
        return new DefaultModelFieldFactory(scanMode, validatorScanner);
    }

    @Test
    public void testSetValidatorFilterGroups() {
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        DefaultModelFieldFactory fs =
                new DefaultModelFieldFactory( FieldScanMode.DIRECT_FIELD_ACCESS, validatorScanner );
        final GroupMode groupMode = GroupMode.ANY_OF_REQUIRED;
        final Collection<Class> groups = new ArrayList<Class>();
        fs.setValidatorFilterGroups(groupMode, groups);

        verify(validatorScanner, times(1)).setFilterGroups(groupMode, groups);
    }

    public static class SimpleTestClass {
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testGetFields() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( SimpleTestClass.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
    }

    public static class SimpleIgnoreTestClass {
        @IgnoreField
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testIgnoreField() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( SimpleIgnoreTestClass.class );

        assertFieldNotFound( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
    }

    public static class TestClassWithValidation {
        public Long publicLongField;
        @Min(1)
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testFieldWithValidation() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithValidation.class );

        assertFieldFoundOnce( models, "publicLongField" );
        int fieldIndexWithAnnotations = assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );

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

    @Test
    public void testClassWithNoDefaultConstructor() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassNoDefaultConstructor.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
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
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldFoundOnce( models, "finalInt" );
    }

    @Test
    public void testAllowFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldFoundOnce( models, "finalInt" );
    }

    @Test
    public void testDontAllowFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(false);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldNotFound( models, "finalInt" );
    }

    @Test
    public void testAllowStaticMember() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(false);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldFoundOnce( models, "staticMember" );
    }

    public static class TestClassWithStaticFinalMember {
        public static final int staticMember = 10;
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testStaticFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        List<ModelField> models = fs.getFields( TestClassWithStaticFinalMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
    }

    @Test
    public void testAllowStaticFinalMember() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );
        fs.setAllowFinalFields(true);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( TestClassWithStaticMember.class );
        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldFoundOnce( models, "staticMember" );
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
    public void testGetterAnnotation() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( SimpleTestGetterClass.class );

        int i = assertFieldFoundOnce( models, "longValue" );
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
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( IgnoreBeanPropertyClass.class );
        assertFieldNotFound(models, "ugnored");
        assertFieldFoundOnce( models, "notIgnored" );
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
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( VisibilityTestGetterClass.class );
        assertFieldNotFound(models, "protected");
        assertFieldNotFound(models, "private");
        assertFieldFoundOnce( models, "public" );
    }

    public static class BeanWithStaticAndFinalProperties {
        public Integer getNormal() { return -1; }
        public static final Integer getStaticFinal() { return 0; }
        public final Integer getFinal() { return 1; }
        public static Integer getStatic() { return 2; }
    }

    @Test
    public void testBeanExtraOptionsDefault() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY );
        List<ModelField> models = fs.getFields( BeanWithStaticAndFinalProperties.class );
        assertFieldFoundOnce( models, "normal" );
        assertFieldNotFound( models, "staticFinal" );
        assertFieldFoundOnce( models, "final" );
        assertFieldNotFound( models, "static" );
    }

    @Test
    public void testBeanExtraOptionsAllowAll() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY );
        fs.setAllowFinalFields(true);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields( BeanWithStaticAndFinalProperties.class );
        assertFieldFoundOnce( models, "normal" );
        assertFieldNotFound( models, "staticFinal" );
        assertFieldFoundOnce( models, "final" );
        assertFieldNotFound( models, "static" );
    }


    public static abstract class BaseTestClass {
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }
    public static class ExtendedTestClass extends BaseTestClass {
        public Long publicLongFieldEx;
        protected Integer protectedIntegerFieldEx;
        private String privateStringFieldEx;
    }

    public static class ExtendedTestClass2 extends ExtendedTestClass {
        public Long publicLongFieldEx2;
        protected Integer protectedIntegerFieldEx2;
        private String privateStringFieldEx2;
    }

    @Test
    public void testExtendedFields() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS);
        List<ModelField> models = fs.getFields( ExtendedTestClass.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldFoundOnce( models, "publicLongFieldEx" );
        assertFieldFoundOnce( models, "protectedIntegerFieldEx" );
        assertFieldFoundOnce( models, "privateStringFieldEx" );
    }

    @Test
    public void testExtendedFieldsTwoLevels() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS);
        List<ModelField> models = fs.getFields( ExtendedTestClass2.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldFoundOnce( models, "protectedIntegerField" );
        assertFieldFoundOnce( models, "privateStringField" );
        assertFieldFoundOnce( models, "publicLongFieldEx" );
        assertFieldFoundOnce( models, "protectedIntegerFieldEx" );
        assertFieldFoundOnce( models, "privateStringFieldEx" );
        assertFieldFoundOnce( models, "publicLongFieldEx2" );
        assertFieldFoundOnce( models, "protectedIntegerFieldEx2" );
        assertFieldFoundOnce( models, "privateStringFieldEx2" );
    }


    public interface BeanInterface {
        public String getPublicStringIn();
    }
    public static class BaseTestBean {
        public Long getPublicLongField() { return 0L; }
        protected Integer getProtectedIntegerField() { return 1; }
        private String getPrivateStringField() { return ""; }
    }

    public static class ExtendedTestBean extends BaseTestBean {
        public Long getPublicLongFieldEx() { return 0L; }
        protected Integer getProtectedIntegerFieldEx() { return 1; }
        private String getPrivateStringFieldEx() { return ""; }
    }

    public static class ExtendedInterfacedTestBean extends BaseTestBean implements BeanInterface {
        public Long getPublicLongFieldEx() { return 0L; }
        protected Integer getProtectedIntegerFieldEx() { return 1; }
        private String getPrivateStringFieldEx() { return ""; }
        @Override public String getPublicStringIn() { return ""; }
    }

    public static class ExtendedOverrideTestBean extends BaseTestBean {
        public Long getPublicLongFieldEx() { return 0L; }
        protected Integer getProtectedIntegerFieldEx() { return 1; }
        private String getPrivateStringFieldEx() { return ""; }

        @Override public Long getPublicLongField() { return super.getPublicLongField(); }
    }

    @Test
    public void testExtendedBaseBeans() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY);
        List<ModelField> models = fs.getFields( BaseTestBean.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldNotFound( models, "protectedIntegerField" );
        assertFieldNotFound( models, "privateStringField" );
    }

    @Test
    public void testExtendedBeans() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY);
        List<ModelField> models = fs.getFields( ExtendedTestBean.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldNotFound( models, "protectedIntegerField" );
        assertFieldNotFound( models, "privateStringField" );
        assertFieldFoundOnce( models, "publicLongFieldEx" );
        assertFieldNotFound( models, "protectedIntegerFieldEx" );
        assertFieldNotFound( models, "privateStringFieldEx" );
    }

    @Test
    public void testExtendedAndImplementingBeans() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY);
        List<ModelField> models = fs.getFields( ExtendedInterfacedTestBean.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldNotFound( models, "protectedIntegerField" );
        assertFieldNotFound( models, "privateStringField" );
        assertFieldFoundOnce( models, "publicLongFieldEx" );
        assertFieldNotFound( models, "protectedIntegerFieldEx" );
        assertFieldNotFound( models, "privateStringFieldEx" );
        assertFieldFoundOnce( models, "publicStringIn" );
    }

    @Test
    public void testExtendedAndOneGetterOverriddenBean() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.BEAN_PROPERTY);
        List<ModelField> models = fs.getFields( ExtendedOverrideTestBean.class );

        assertFieldFoundOnce( models, "publicLongField" );
        assertFieldNotFound( models, "protectedIntegerField" );
        assertFieldNotFound( models, "privateStringField" );
        assertFieldFoundOnce( models, "publicLongFieldEx" );
        assertFieldNotFound( models, "protectedIntegerFieldEx" );
        assertFieldNotFound( models, "privateStringFieldEx" );
    }
}
