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

import fi.vincit.jmobster.util.collection.AnnotationBag;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import fi.vincit.jmobster.util.writer.StreamDataWriter;
import org.junit.Test;

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
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, DataWriter> {
            @Override protected void write( DataWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<DataWriter> {
            TestManager() {}
        }

        TestManager manager = new TestManager();
        manager.setValidator(new TestValidatorWriter());
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
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, TestWriter> {
            @Override protected void write( TestWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<TestWriter> {
            TestManager() {}
        }

        TestManager manager = new TestManager();
        manager.setValidator(new TestValidatorWriter());
    }

    /**
     * Manager requires TestWriter, but TestValidatorWriter only
     * requires DataWriter
     */
    @Test
    public void testWithImplementedDataWriterButDataWriterInManager() {
        class TestWriter extends StreamDataWriter {}
        class TestValidator extends BaseValidator {
            @Override public void init( AnnotationBag annotationBag ) {}
        }
        class TestValidatorWriter extends BaseValidatorWriter<TestValidator, DataWriter> {
            @Override protected void write( DataWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<TestWriter> {
            TestManager() {}
        }

        TestManager manager = new TestManager();
        manager.setValidator(new TestValidatorWriter());
    }
}