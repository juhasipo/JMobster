package fi.vincit.jmobster.processor.frameworks.backbone.validator.writer;

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

import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.StringBufferWriter;

public abstract class BaseValidatorTest {

    private OutputMode mode;

    protected BaseValidatorTest() {
    }

    protected BaseValidatorTest( OutputMode mode ) {
        this.mode = mode;
    }

    protected JavaScriptContext mockWriter(OutputMode mode) {
        StringBufferWriter stringWriter = new StringBufferWriter();
        JavaScriptWriter writer = new JavaScriptWriter(stringWriter);
        if( mode == OutputMode.JSON ) {
            writer.setJSONmode(true);
        }
        return new JavaScriptContext(writer, mode);
    }

    protected JavaScriptContext createAndInjectContext( BaseValidatorWriter validator, ItemStatus status ) {
        if( mode == null ) {
            throw new IllegalStateException( "Mode must be set before using this method. Call the correct constructor." );
        }
        JavaScriptContext context = mockWriter(mode);
        validator.setItemStatus( status );
        validator.setContext(context);
        return context;
    }
}
