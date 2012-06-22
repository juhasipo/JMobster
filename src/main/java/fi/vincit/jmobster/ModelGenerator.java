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

import fi.vincit.jmobster.processor.ModelProcessor;

/**
 * <p>
 *     Interface for generating models. Use {@link JMobsterFactory} to
 * create instances of model generators. The interface is very simple.
 * Once the ModelGenerator object is instantiated, just call the
 * {@link ModelGenerator#process(Class[])} method and give the method
 * all the classes that should be processed.
 * </p>
 *
 * <p>
 *     Only call the {@link ModelGenerator#process(Class[])} method once.
 * If the method is called multiple times, the default behaviour is that
 * the previous models are overwritten. This of course depends on what kind
 * of {@link fi.vincit.jmobster.util.StreamModelWriter} or {@link fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor}
 * has been configured.
 * </p>
 */
public interface ModelGenerator {
    /**
     * Process and create the client side model(s) for
     * the given classes.
     * @param classes One or more classes for which the models should be generated.
     */
    void process( Class... classes );

    /**
     * Returns used model processor. Use this to configure
     * the model processor as you wish.
     * @return Model processor used
     */
    ModelProcessor getModelProcessor();
}
