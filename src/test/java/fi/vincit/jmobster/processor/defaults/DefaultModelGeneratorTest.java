package fi.vincit.jmobster.processor.defaults;

/*
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

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.ModelGeneratorBuilder;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.TestUtil;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultModelGeneratorTest {
    public static class TestClass1 {
    }
    public static class TestClass2 {
    }

    @Test
    public void testProcessWithoutClasses() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        modelGenerator.processAll(new ArrayList<Model>());

        InOrder mpInOrder = inOrder(modelProcessor);
        mpInOrder.verify(modelProcessor, times(1)).startProcessing(ItemStatuses.firstAndLast());
        mpInOrder.verify(modelProcessor, times(1)).endProcessing(ItemStatuses.firstAndLast());
        verify( modelProcessor, never() ).processModel( any( Model.class ), any( ItemStatus.class ) );
    }

    @Test
    public void testProcessWithList() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        Model testModel1 = mock(Model.class);
        Model testModel2 = mock( Model.class );
        modelGenerator.processAll( TestUtil.collectionFromObjects( testModel1, testModel2 ) );

        InOrder mpInOrder = inOrder( modelProcessor );
        mpInOrder.verify(modelProcessor, times(1)).startProcessing( ItemStatuses.firstAndLast() );
        mpInOrder.verify(modelProcessor, times(2)).processModel( any( Model.class ), any( ItemStatus.class ) );
        mpInOrder.verify(modelProcessor, times(1)).endProcessing( ItemStatuses.firstAndLast() );
    }

    @Test
    public void testProcessOne() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        Model testModel1 = mock(Model.class);
        modelGenerator.process( testModel1 );

        InOrder mpInOrder = inOrder( modelProcessor );
        mpInOrder.verify(modelProcessor).startProcessing( ItemStatuses.firstAndLast() );
        mpInOrder.verify(modelProcessor).processModel( eq(testModel1), eq(ItemStatuses.firstAndLast()) );
        mpInOrder.verify(modelProcessor).endProcessing( ItemStatuses.firstAndLast() );
    }

    @Test
    public void testSetWriter() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        DataWriter writer = mock(DataWriter.class);
        modelGenerator.setWriter(writer);

        verify(modelProcessor).setWriter(writer);
    }
}
