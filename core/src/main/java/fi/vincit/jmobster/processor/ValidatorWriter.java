package fi.vincit.jmobster.processor;

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
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * <p>
 *     Interface for validator writers that write validator with given
 * writer. You should not implement this interface. Instead use {@link fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriter}
 * which handles necessary type casts internally so that the validator writer will get the parameters
 * correctly.
 * </p>
 *
 * @param <V> Validator type the writer uses
 * @param <C> LanguageContext that excepts DataWriters of type W
 * @param <W> Data writer type. Should be compatible with corresponding {@link fi.vincit.jmobster.processor.defaults.validator.ValidatorWriterSet}
 */
public interface ValidatorWriter<V extends Validator, C extends LanguageContext<? extends W>, W extends DataWriter> {
    /**
     * Writes the given validator with the given writer
     * @param languageContext Language context to use
     * @param validator Validator to write
     * @param status Item status
     */
    void write( C languageContext, Object validator, ItemStatus status );

    /**
     * Returns the type this validator writer supports
     * @return Type of validator (Class of the {@link Validator})
     */
    Class getSupportedType();
}
