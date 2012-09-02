package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.ValidatorWriter;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Abstract implementation for a manager that provides validator
 * writers. The subclasses should configure the used validator writers
 * in the constructor or other suitable method before using.
 * </p>
 *
 * <p>
 *     The concrete type of data writer can be configured via generic parameter.
 * This will allow the user to use higher level data writers to write validators
 * instead of using just a low level {@link DataWriter}. This writer will be given
 * to the validator writers as the correct type as long as the validator writer
 * has the correct generic type set.
 * </p>
 *
 * @param <W> DataWriter to use. This has be compatible with the ValidatorWriters that are given to this manager.
 */
public abstract class BaseValidatorWriterManager<W extends DataWriter> {
    final private Map<Class, ValidatorWriter<? extends Validator, W>> writers =
            new HashMap<Class, ValidatorWriter<? extends Validator, W>>();

    private W dataWriter;

    /**
     * Constructs validator writer manager with the given data writer
     * @param dataWriter Data writer to use
     */
    public BaseValidatorWriterManager( W dataWriter ) {
        this.dataWriter = dataWriter;
    }

    /**
     * Configure new validator writer.
     * @param validatorWriters One or more validation writers to add.
     */
    public void setValidator(ValidatorWriter<? extends Validator, W>... validatorWriters) {
        for( ValidatorWriter validatorWriter : validatorWriters ) {
            writers.put( validatorWriter.getSupportedType(), validatorWriter );
        }
    }

    /**
     * Writes the given validator with tie configured data writer
     * @param validator Validator to write
     * @param isLast Set to true if the validator is the last validator that is going to be written for the field
     */
    public void write( Validator validator, boolean isLast ) {
        Class validatorType = validator.getType();
        if( writers.containsKey(validatorType) ) {
            ValidatorWriter writer = writers.get(validatorType);
            writer.write( dataWriter, (Object)validator, isLast );
        }
    }

    public void setWriter( W dataWriter ) {
        this.dataWriter = dataWriter;
    }
}
