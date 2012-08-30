package fi.vincit.jmobster.processor.frameworks.html5;

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

import fi.vincit.jmobster.processor.ModelWriter;
import fi.vincit.jmobster.processor.frameworks.html5.validator.writer.HTML5ValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.html.HTML5Writer;
import fi.vincit.jmobster.processor.languages.html.TagEndMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.ItemHandler;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

public class HTML5FormWriter implements ModelWriter {
    final private HTML5Writer writer;
    final private HTML5ValidatorWriterManager validatorWriterManager;

    public HTML5FormWriter( DataWriter writer ) {
        this.writer = new HTML5Writer(writer);
        this.validatorWriterManager = new HTML5ValidatorWriterManager(this.writer);
    }

    @Override
    public void write( Model model ) {
        writer.startTag("form", TagEndMode.TAG_WITH_CONTENT);

        final ItemHandler<Validator> validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write( validator, status.isLastItem() );
            }
        };

        ItemProcessor.process(model.getFields()).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                writer.startTagWithAttr("input").writeAttr("type", "text");
                ItemProcessor.process(field.getValidators()).with(validatorWriter);
                writer.writeAttr("value", field.getDefaultValue());
                writer.endStartTagWithAttr(TagEndMode.EMPTY_TAG);
            }
        });

        writer.endTag();
    }
}
