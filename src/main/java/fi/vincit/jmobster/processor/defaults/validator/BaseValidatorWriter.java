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
import fi.vincit.jmobster.util.DataWriter;

/**
 *
 * @param <T>
 * @param <W>
 */
public abstract class BaseValidatorWriter<T extends Validator, W extends DataWriter> implements ValidatorWriter<T,W> {
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
     */
    @SuppressWarnings("unchecked")
    public void write(W writer, Object validator) {
        /*
        To get this work this in a meta programming way this has to be done
        like this. When this fails, there may be some strange errors
        or exceptions thrown.
         */
        write( writer, (T)validator );
    }

    protected abstract void write(W writer, T validator);
}
