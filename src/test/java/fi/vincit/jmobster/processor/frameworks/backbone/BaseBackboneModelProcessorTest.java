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

import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.StringBufferWriter;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public abstract class BaseBackboneModelProcessorTest {
    public static final String TEST_MODEL_NAME = "TestModel";
    protected StringBufferWriter writer;
    protected JavaScriptWriter jsWriter;
    protected JavaScriptContext context;
    @Mock protected ModelProcessor<JavaScriptContext, JavaScriptWriter> validatorProcessor;
    @Mock protected ModelProcessor<JavaScriptContext, JavaScriptWriter> valueProcessor;

    protected abstract OutputMode getMode();

    @Before
    public void init() {
        writer = new StringBufferWriter();
        jsWriter = new JavaScriptWriter(writer);
        context = new JavaScriptContext(jsWriter, getMode());
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
                .doProcessModel(any(Model.class), any(ItemStatus.class));
        doAnswer(writeEmptyBlock)
                .when(valueProcessor)
                .doProcessModel(any(Model.class), any(ItemStatus.class));
    }

    protected Model mockModel() {
        Model model = mock(Model.class);
        when(model.getName()).thenReturn(TEST_MODEL_NAME);
        return model;
    }

    protected BackboneModelProcessor createProcessor() {
        BackboneModelProcessor processor =
                new BackboneModelProcessor
                        .Builder(context)
                        .setModelProcessors(validatorProcessor, valueProcessor)
                        .build();

        return processor;
    }
}
