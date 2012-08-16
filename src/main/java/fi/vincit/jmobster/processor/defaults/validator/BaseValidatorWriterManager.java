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

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation for a manager that provides validator
 * writers.
 *
 * The constructor has to implemented. The used validator writers can be initialized in the
 * constructor.
 * @param <W> DataWriter to use. This has be compatible with the ValidatorWriters that are given to this manager.
 */
public abstract class BaseValidatorWriterManager<W extends DataWriter> {
    final private Map<Class, ValidatorWriter<? extends Validator, W>> writers =
            new HashMap<Class, ValidatorWriter<? extends Validator, W>>();

    final private W modelWriter;

    public BaseValidatorWriterManager( W modelWriter ) {
        this.modelWriter = modelWriter;
    }

    /**
     * Configure new validator writer.
     * @param validatorType Validation annotation type (class).
     * @param validatorWriter Validation writer to use for this type.
     */
    public void setValidator(Class validatorType, BaseValidatorWriter<? extends Validator, W> validatorWriter) {
        writers.put(validatorType, validatorWriter);
    }

    public void writeValidator(Validator validator) {
        Class validatorType = validator.getType();
        if( writers.containsKey(validatorType) ) {
            ValidatorWriter writer = writers.get(validatorType);
            writer.write( modelWriter, (Object)validator );
        }
    }
}
