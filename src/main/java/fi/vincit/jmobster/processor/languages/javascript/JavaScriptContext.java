package fi.vincit.jmobster.processor.languages.javascript;

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

import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.writer.DataWriter;

public class JavaScriptContext extends LanguageContext<JavaScriptWriter> {
    public JavaScriptContext(DataWriter writer, OutputMode outputMode) {
        super(new JavaScriptWriter(writer));
        this.outputMode = outputMode;
    }

    public JavaScriptContext(JavaScriptWriter writer, OutputMode outputMode) {
        super(writer);
        this.outputMode = outputMode;
    }

    private OutputMode outputMode;

    public OutputMode getOutputMode() {
        return outputMode;
    }
}
