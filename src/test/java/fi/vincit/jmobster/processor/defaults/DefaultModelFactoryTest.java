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

import fi.vincit.jmobster.util.groups.GroupMode;
import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelFieldFactory;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultModelFactoryTest {

    private static final String MODEL_NAME = "ModelName";
    private ModelFieldFactory modelFieldFactory;
    private ModelNamingStrategy modelNamingStrategy;

    private static List<ModelField> mockModelFieldList = new ArrayList<ModelField>();
    static {
        mockModelFieldList.add(mock(ModelField.class));
    }

    private ModelFactory createModelFactory() {
        modelFieldFactory = mock(ModelFieldFactory.class);
        when(modelFieldFactory.getFields(any(Class.class))).thenReturn(mockModelFieldList);

        modelNamingStrategy = mock(ModelNamingStrategy.class);
        when(modelNamingStrategy.getName(any(Class.class))).thenReturn( MODEL_NAME );

        ModelFactory factory = new DefaultModelFactory(modelFieldFactory, modelNamingStrategy);
        return factory;
    }

    @Test
    public void testCreate() throws Exception {
        ModelFactory factory = createModelFactory();
        Model model = factory.create(String.class);

        assertNotNull(model);
        assertEquals(MODEL_NAME, model.getName());
        assertEquals(1, model.getFields().size());
    }

    @Test
    public void testCreateAll() throws Exception {
        ModelFactory factory = createModelFactory();
        Collection<Model> models = factory.createAll(String.class, Integer.class);

        assertEquals(2, models.size());
        verify(modelNamingStrategy, times(2)).getName(any(Class.class));
        verify(modelFieldFactory, times(2)).getFields( any( Class.class ) );
    }

    @Test
    public void testCreateAllFromCollection() throws Exception {
        ModelFactory factory = createModelFactory();
        Collection inputs = TestUtil.collectionFromObjects( String.class, Integer.class );
        Collection<Model> models = factory.createAll(inputs);

        assertEquals(2, models.size());
        verify(modelNamingStrategy, times(2)).getName(any(Class.class));
        verify(modelFieldFactory, times(2)).getFields(any(Class.class));
    }

    public static interface Group1 {}
    public static interface Group2 {}

    @Test
    public void testSetValidatorFilterGroups() throws Exception {
        ModelFactory factory = createModelFactory();

        factory.setValidatorFilterGroups( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );

        ArgumentCaptor<Collection> groupsArgument = ArgumentCaptor.forClass( Collection.class );
        verify( modelFieldFactory, times( 1 ) ).setValidatorFilterGroups( eq( GroupMode.EXACTLY_REQUIRED ), groupsArgument.capture() );
        assertEquals( 2, groupsArgument.getValue().size() );
    }

    @Test
    public void testSetFieldFilterGroups() throws Exception {
        ModelFactory factory = createModelFactory();

        factory.setFieldFilterGroups( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        ArgumentCaptor<Collection> groupsArgument = ArgumentCaptor.forClass( Collection.class );
        verify( modelFieldFactory, times( 1 ) ).setFieldFilterGroups( eq( GroupMode.AT_LEAST_REQUIRED ), groupsArgument.capture() );
        assertEquals( 2, groupsArgument.getValue().size() );
    }
}
