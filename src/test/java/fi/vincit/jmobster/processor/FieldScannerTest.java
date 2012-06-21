package fi.vincit.jmobster.processor;/*
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
import fi.vincit.jmobster.backbone.AnnotationProcessorProvider;
import fi.vincit.jmobster.converter.FieldValueConverter;
import fi.vincit.jmobster.exception.CannotAccessDefaultConstructorError;
import fi.vincit.jmobster.exception.DefaultConstructorMissingError;
import org.junit.Test;

import javax.validation.constraints.Min;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FieldScannerTest {

    private FieldScanner getFieldScanner() {
        FieldValueConverter fvc = mock(FieldValueConverter.class);
        AnnotationProcessorProvider app = mock(AnnotationProcessorProvider.class);
        when(app.isAnnotationForValidation(any(Min.class))).thenReturn( true );
        return new FieldScanner(fvc, app);
    }

    public static class SimpleTestClass {
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testGetFields() {
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(SimpleTestClass.class);

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
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(SimpleIgnoreTestClass.class);

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
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(TestClassWithValidation.class);

        assertFieldFound( models, "publicLongField" );
        int fieldIndexWithAnnotations = assertFieldFound(models, "protectedIntegerField");
        assertFieldFound(models, "privateStringField");

        ModelField fieldWithAnnotations = models.get(fieldIndexWithAnnotations);
        assertEquals(1, fieldWithAnnotations.getAnnotations().size());
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
        FieldScanner fs = getFieldScanner();
        fs.getFields(TestClassNoDefaultConstructor.class);
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
        FieldScanner fs = getFieldScanner();
        fs.getFields(TestClassPrivateDefaultConstructor.class);
    }

    public static class TestClassWithStaticMember {
        public static int staticMember;
        public final int finalInt = 1;
        public Long publicLongField;
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    @Test
    public void testStaticMember() {
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldNotFound( models, "finalInt" );
    }

    @Test
    public void testAllowFinalMember() {
        FieldScanner fs = getFieldScanner();
        fs.setAllowFinalFields(true);
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldFound( models, "finalInt" );
    }

    @Test
    public void testDontAllowFinalMember() {
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
        assertFieldNotFound( models, "finalInt" );
    }

    @Test
    public void testAllowStaticMember() {
        FieldScanner fs = getFieldScanner();
        fs.setAllowFinalFields(false);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
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
        FieldScanner fs = getFieldScanner();
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldNotFound( models, "staticMember" );
    }

    @Test
    public void testAllowStaticFinalMember() {
        FieldScanner fs = getFieldScanner();
        fs.setAllowFinalFields(true);
        fs.setAllowStaticFields(true);
        List<ModelField> models = fs.getFields(TestClassWithStaticMember.class);
        assertFieldFound( models, "publicLongField" );
        assertFieldFound( models, "protectedIntegerField" );
        assertFieldFound( models, "privateStringField" );
        assertFieldFound( models, "staticMember" );
    }


    private int assertFieldFound(List<ModelField> models, String fieldName) {
        for( int i = 0; i < models.size(); ++i ) {
            ModelField field = models.get(i);
            if( field.getField().getName().equals(fieldName) ) {
                return i;
            }
        }
        assertTrue( "Field with name <" + fieldName + "> not found.", false );
        return -1; // Never returned due to assertion
    }

    private void assertFieldNotFound(List<ModelField> models, String fieldName) {
        for( ModelField field : models ) {
            if( field.getField().getName().equals(fieldName) ) {
                assertTrue( "Field with name <" + fieldName + "> found when it should be ignored.", false );
            }
        }
    }
}
