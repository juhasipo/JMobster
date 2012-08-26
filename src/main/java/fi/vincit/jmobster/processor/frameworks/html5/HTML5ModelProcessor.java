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

import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.io.IOException;

public class HTML5ModelProcessor extends BaseModelProcessor {
    private HTML5FormWriter html5FormWriter;

    public HTML5ModelProcessor( String modelFilePath ) {
        super( modelFilePath );
        this.html5FormWriter = new HTML5FormWriter(getWriter());
    }

    public HTML5ModelProcessor( DataWriter writer ) {
        super( writer );
        this.html5FormWriter = new HTML5FormWriter(writer);
    }

    @Override
    public void startProcessing() throws IOException {

    }

    @Override
    public void processModel( Model model, boolean isLastModel ) {
        html5FormWriter.write(model);
    }

    @Override
    public void endProcessing() throws IOException {
        getWriter().close();
    }
}
