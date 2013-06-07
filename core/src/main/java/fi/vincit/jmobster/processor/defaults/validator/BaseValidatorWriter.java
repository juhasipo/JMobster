package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.ValidatorWriter;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.reflection.CastUtil;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.lang.reflect.Type;

/**
 * Base class for validator writers. This should be extended when creating own
 * validator writers. The class handles type casting internally so the validator
 * writer gets the correct parameters.
 *
 * @param <V> Validator type the writer uses
 * @param <C> Language context that can use DataWriter of type W
 * @param <W> Writer used to write the validator
 * @see ValidatorWriterSet
 */
public abstract class BaseValidatorWriter<V extends Validator, C extends LanguageContext<? extends W>, W extends DataWriter> implements ValidatorWriter<V, C, W> {
    final private Class supportedType;

    /**
     * Configures the validator writer to support the given validator V class.
     *
     * The type is resolved from the given generic type parameter V.
     */
    protected BaseValidatorWriter() {
        this.supportedType = resolveSupportedType();
    }

    private Class resolveSupportedType() {
        Type type = this.getClass().getGenericSuperclass();
        return CastUtil.castGenericTypeToClass(type);
    }

    /**
     * Internal implementation of write. This casts the given validator
     * to the type of generic T. If the validator happens to be wrong type
     * there may be strange exceptions thrown.
     *
     * This is implemented like this to avoid writing boilerplate code
     * for each type of validator. The boilerplate code's only purpose would
     * be to cast the validator to correct type.
     * @param languageContext Language context
     * @param validator Validator to write
     * @param status Item status
     */
    @SuppressWarnings("unchecked")
    public void write( C languageContext, Object validator, ItemStatus status ) {
        /*
        To get this work this in a meta programming way this has to be done
        like this. When this fails, there may be some strange errors
        or exceptions thrown.
         */
        write( languageContext, (V)validator, status );
    }

    /**
     * Writes validator to given language context
     * @param languageContext Writer
     * @param validator Validator to write
     * @param status Item status
     */
    protected abstract void write(C languageContext, V validator, ItemStatus status);

    @Override
    public Class getSupportedType() {
        return supportedType;
    }
}
