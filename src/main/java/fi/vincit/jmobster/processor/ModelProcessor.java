package fi.vincit.jmobster.processor;/*
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

import fi.vincit.jmobster.processor.frameworks.backbone.AnnotationProcessor;
import fi.vincit.jmobster.processor.model.Model;

import java.io.IOException;

/**
 * BackboneModelProcessor controls how the models given from {@link fi.vincit.jmobster.ModelGenerator}
 * are processed.
 */
public interface ModelProcessor {
    /**
     * Called before the first model is processed.
     * @throws IOException If something goes wrong with writing the data
     */
    void startProcessing() throws IOException;

    /**
     * Called for each model once in the order the models
     * were given to the {@link fi.vincit.jmobster.ModelGenerator}.
     * @param model Model to process
     * @param isLastModel True if the given model is the last model to process. Otherwise false.
     */
    void processModel( Model model, boolean isLastModel );

    /**
     * Called when the last model has been processed.
     * @throws IOException If something goes wrong with writing the data
     */
    void endProcessing() throws IOException;

    /**
     * Returns the used annotation processor.
     * @return Used annotation processor.
     */
    AnnotationProcessor getAnnotationProcessor();

    /**
     * Set naming strategy for the processor
     * @param modelNamingStrategy Naming strategy
     */
    void setModelNamingStrategy( ModelNamingStrategy modelNamingStrategy );
}
