package fi.vincit.jmobster;/*
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

import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.Collection;

/**
 * Model generator processes given models and outputs the data
 * in format that can be used in the target platform. Use {@link fi.vincit.jmobster.processor.ModelFactory}
 * to generate the {@link Model} objects to process.
 */
public interface ModelGenerator<W extends DataWriter> {

    /**
     * Process and create the client side model(s) for the given models.
     * @param models One or more models for which the models should be generated.
     */
    void processAll( Collection<Model> models );

    /**
     * Process a single model
     * @param model Model to process
     */
    void process(Model model);

    /**
     * Sets language context to use
     * @param context Language context
     */
    void setLanguageContext( LanguageContext<W> context );


}
