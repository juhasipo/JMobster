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

import fi.vincit.jmobster.processor.AnnotationProcessor;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.util.List;

public class ValidationSectionWriter {
    private ModelWriter writer;
    private AnnotationProcessor annotationProcessor;

    public ValidationSectionWriter( ModelWriter writer, AnnotationProcessor annotationProcessor ) {
        this.writer = writer;
        this.annotationProcessor = annotationProcessor;
    }

    public void writeValidators( List<ModelField> fields ) {
        writer.writeLine("validate: {").indent();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.write(field.getField().getName()).writeLine( ": {" ).indent();
                annotationProcessor.writeValidation(field.getAnnotations(), writer);
                writer.indentBack();
                writer.writeLine( "}", ",", !isLastItem );
            }
        };
        modelFieldItemProcessor.process(fields);
        writer.indentBack();
        writer.writeLine( "}" );
    }
}
