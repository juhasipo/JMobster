package fi.vincit.jmobster.processor.frameworks.html5.validator.writer;

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
import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriter;
import fi.vincit.jmobster.processor.defaults.validator.SizeValidator;
import fi.vincit.jmobster.processor.languages.html.HTML5Writer;
import fi.vincit.jmobster.util.DataWriter;

public class SizeValidatorWriter extends BaseValidatorWriter<SizeValidator, HTML5Writer> {

    public SizeValidatorWriter() {
        super( SizeValidator.class );
    }

    @Override
    protected void write( HTML5Writer writer, SizeValidator validator, boolean isLast ) {
        writer.writeAttr("maxlength", ""+validator.getMax());
    }
}
