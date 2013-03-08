package fi.vincit.jmobster.processor.frameworks.backbone;

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

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseBackboneModelProcessorTest {
    public static final String TEST_MODEL_NAME = "TestModel";
    protected StringBufferWriter writer;
    @Mock protected ModelProcessor<JavaScriptWriter> validatorProcessor;
    @Mock protected ModelProcessor<JavaScriptWriter> valueProcessor;
    @Mock private FieldValueConverter fieldValueConverter;

    protected abstract BackboneModelProcessor.Mode getMode();

    @Before
    public void init() {
        writer = new StringBufferWriter();
        MockitoAnnotations.initMocks(this);
        when(validatorProcessor.getName()).thenReturn("validation");
        when(valueProcessor.getName()).thenReturn("defaults");

        Answer<Object> writeEmptyBlock = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ItemStatus status = (ItemStatus)invocation.getArguments()[1];
                if( status.isNotLastItem() ) {
                    writer.write(
                            "{\n" +
                            "    },\n" +
                            "    "
                    );
                } else {
                    writer.write(
                            "{\n" +
                            "    }");
                }
                return null;
            }
        };
        doAnswer(writeEmptyBlock)
                .when(validatorProcessor)
                .processModel(any(Model.class), any(ItemStatus.class));
        doAnswer(writeEmptyBlock)
                .when(valueProcessor)
                .processModel(any(Model.class), any(ItemStatus.class));
    }

    protected Model mockModel() {
        Model model = mock(Model.class);
        when(model.getName()).thenReturn(TEST_MODEL_NAME);
        return model;
    }

    protected BackboneModelProcessor createProcessor() {
        BackboneModelProcessor processor = new BackboneModelProcessor(
                writer,
                fieldValueConverter,
                getMode(),
                validatorProcessor,
                valueProcessor);

        return processor;
    }
}