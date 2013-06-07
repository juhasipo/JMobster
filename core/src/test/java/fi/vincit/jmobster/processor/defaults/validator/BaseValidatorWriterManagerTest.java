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

import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.util.collection.AnnotationBag;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import fi.vincit.jmobster.util.writer.StreamDataWriter;
import org.junit.Test;

import java.util.Arrays;

public class BaseValidatorWriterManagerTest {

    /*
    Following classes test only the possibilities to construct validator writer managers and
    the always pass when the code compiles.
     */

    /**
     * Every class uses plain DataWriter interface
     */
    @Test
    public void testWithPlainDataWriterInterface() {
        class TestValidator extends BaseValidator {
            @Override public void init( AnnotationBag annotationBag ) {}
        }
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, LanguageContext<DataWriter>, DataWriter> {
            @Override protected void write( LanguageContext<DataWriter> languageContext, TestValidator validator, ItemStatus status ) {}
        }

        BaseValidatorWriterManager manager =
                new BaseValidatorWriterManager(Arrays.asList(new TestValidatorWriter()));
    }

    /**
     * Manager requires an implemented DataWriter, TestWriter.
     * TestValidatorWriter also uses this same TestWriter
     */
    @Test
    public void testWithImplementedDataWriter() {
        class TestWriter extends StreamDataWriter {}
        class TestValidator extends BaseValidator {
            @Override public void init( AnnotationBag annotationBag ) {}
        }
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, LanguageContext<TestWriter>, TestWriter> {
            @Override protected void write( LanguageContext<TestWriter> languageContext, TestValidator validator, ItemStatus status ) {}
        }

        BaseValidatorWriterManager manager =
                new BaseValidatorWriterManager(Arrays.asList(new TestValidatorWriter()));
    }

    /**
     * Manager requires TestWriter, but TestValidatorWriter only
     * requires DataWriter
     */
    /*
    @Test
    public void testWithImplementedDataWriterButDataWriterInManager() {
        class TestWriter extends StreamDataWriter {}
        class TestValidator extends BaseValidator {
            @Override public void init( AnnotationBag annotationBag ) {}
        }
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, LanguageContext<DataWriter>, DataWriter> {
            @Override protected void write(LanguageContext<DataWriter> languageContext, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<LanguageContext<TestWriter>, TestWriter> {
            TestManager() {}
        }

        TestManager manager = new TestManager();
        manager.setValidator(new TestValidatorWriter());
    }*/
}
