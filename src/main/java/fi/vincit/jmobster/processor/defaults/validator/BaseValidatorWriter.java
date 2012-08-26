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
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * Base class for validator writers. This should be extended when creating own
 * validator writers. The class handles type casting internally so the validator
 * writer gets the correct parameters.
 *
 * @param <V> Validator type the writer uses
 * @param <W> Writer used to write the validator
 */
public abstract class BaseValidatorWriter<V extends Validator, W extends DataWriter> implements ValidatorWriter<V,W> {
    final private Class supportedType;

    /**
     * Configures the validator writer to support the given validator
     * class.
     * @param supportedType Validator type (Class of the validator)
     */
    protected BaseValidatorWriter( Class supportedType ) {
        this.supportedType = supportedType;
    }

    /**
     * Internal implementation of write. This casts the given validator
     * to the type of generic T. If the validator happens to be wrong type
     * there may be strange exceptions thrown.
     *
     * This is implemented like this to avoid writing boilerplate code
     * for each type of validator. The boilerplate code's only purpose would
     * be to cast the validator to correct type.
     * @param writer Model writer
     * @param validator Validator to write
     * @param isLast
     */
    @SuppressWarnings("unchecked")
    public void write( W writer, Object validator, boolean isLast ) {
        /*
        To get this work this in a meta programming way this has to be done
        like this. When this fails, there may be some strange errors
        or exceptions thrown.
         */
        write( writer, (V)validator, isLast );
    }

    /**
     * Writes validator to given writer
     * @param writer Writer
     * @param validator Validator to write
     * @param isLast True if the validator is the last validator to be written for the field
     */
    protected abstract void write(W writer, V validator, boolean isLast);

    @Override
    public Class getSupportedType() {
        return supportedType;
    }
}
