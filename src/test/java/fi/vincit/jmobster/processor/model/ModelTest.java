package fi.vincit.jmobster.processor.model;

import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest {
    @Test
    public void testConstructModelNoFields() throws Exception {
        class TestModelClass {};
        Collection<ModelField> modelFields = Collections.emptyList();

        Model model = new Model(TestModelClass.class, "TestModel", modelFields);

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertTrue(model.getFields().isEmpty());
        assertFalse(model.hasValidations());
    }

    @Test
    public void testConstructModelValidations() throws Exception {
        class TestModelClass {};
        ModelField fieldWithoutValidation = mock(ModelField.class);
        when(fieldWithoutValidation.hasValidators()).thenReturn(false);
        ModelField fieldWithValidation = mock(ModelField.class);
        when(fieldWithoutValidation.hasValidators()).thenReturn(true);

        Model model = new Model(TestModelClass.class, "TestModel", TestUtil.collectionFromObjects(fieldWithoutValidation, fieldWithValidation));

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertEquals(2, model.getFields().size());
        assertTrue(model.hasValidations());
    }

    @Test
    public void testConstructModelNoValidations() throws Exception {
        class TestModelClass {};
        ModelField fieldWithoutValidation1 = mock(ModelField.class);
        when(fieldWithoutValidation1.hasValidators()).thenReturn( false );
        ModelField fieldWithoutValidation2 = mock(ModelField.class);
        when(fieldWithoutValidation1.hasValidators()).thenReturn( false );

        Model model = new Model(TestModelClass.class, "TestModel", TestUtil.collectionFromObjects(fieldWithoutValidation1, fieldWithoutValidation2));

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertEquals(2, model.getFields().size());
        assertFalse(model.hasValidations());
    }
}
