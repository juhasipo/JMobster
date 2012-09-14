package fi.vincit.jmobster.processor.defaults.validator;

import fi.vincit.jmobster.util.AnnotationBag;
import fi.vincit.jmobster.util.ItemStatus;
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
            TestValidatorWriter() { super( TestValidator.class ); }
            @Override protected void write( DataWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<DataWriter> {
            TestManager() { super( null ); }
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
            TestValidatorWriter() { super( TestValidator.class ); }
            @Override protected void write( TestWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<TestWriter> {
            TestManager() { super( null ); }
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
            TestValidatorWriter() { super( TestValidator.class ); }
            @Override protected void write( DataWriter writer, TestValidator validator, ItemStatus status ) {}
        }
        class TestManager extends BaseValidatorWriterManager<TestWriter> {
            TestManager() { super( null ); }
        }

        TestManager manager = new TestManager();
        manager.setValidator(new TestValidatorWriter());
    }
}
