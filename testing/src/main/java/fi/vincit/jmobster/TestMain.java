package fi.vincit.jmobster;
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


import fi.vincit.jmobster.processor.FieldScanMode;
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.defaults.DefaultValidatorScanner;
import fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.ValidatorProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.JSR303Validators;
import fi.vincit.jmobster.processor.languages.JavaScriptModelCache;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.EnumConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriterSet;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.util.ConverterMode;
import fi.vincit.jmobster.util.groups.GroupMode;
import fi.vincit.jmobster.util.writer.StringBufferWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings( "ALL" )
public class TestMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Analyze models
        ModelFactory factory = JMobsterFactory.getModelFactoryBuilder()
                .setFieldScanMode(FieldScanMode.DIRECT_FIELD_ACCESS)
                .setFieldGroups(GroupMode.ANY_OF_REQUIRED)
                .setValidatorScanner(new DefaultValidatorScanner())
                .build();

        System.out.println("Generate test classes");
        final int n = 2;
        Collection<Class> classesToConvert = new ArrayList<Class>(n);
        for( int i = 0; i < n; ++i ) {
            if( i % 2 == 0 ) {
                classesToConvert.add(DemoClasses.BeanPropertyDemo.class);
            } else {
                classesToConvert.add(DemoClasses.MyModelDto.class);
            }
        }

        // Setup writers
        JavaScriptContext context = new JavaScriptContext(new StringBufferWriter(), OutputMode.JSON);

        // Setup generator
        FieldValueConverter converter = new JavaToJSValueConverter(
                ConverterMode.NULL_AS_DEFAULT,
                EnumConverter.EnumMode.STRING,
                JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN
        );

        BackboneModelProcessor backboneModelProcessor =
                new BackboneModelProcessor
                    .Builder(context)
                    .setModelProcessors(
                            new ValidatorProcessor.Builder()
                                    .setValidatorWriters(new JavaScriptWriterSet(JSR303Validators.get()))
                                    .build()
                    )
                    .build();
        backboneModelProcessor.setClearWriterBeforeProcessing(true);
        backboneModelProcessor.setValidatorFilter(GroupMode.ANY_OF_REQUIRED, DemoClasses.Group2.class);
        JavaScriptModelCache modelCache = new JavaScriptModelCache(backboneModelProcessor, factory);
        modelCache.addModels(classesToConvert);
        modelCache.addGroup(DemoClasses.Group1.class, "Group1");
        modelCache.addGroup(DemoClasses.Group2.class, "Group2");

        for(String modelName : modelCache.getModelNames() ) {
            System.out.println(modelName);
        }
        System.out.print("Press any key to start generating models");
        //System.in.read();
        System.out.println("Generate models");
        // Generate models
        System.out.println("Model:\n" + modelCache.getModelByNameAndGroupClasses("MyModel"));
        System.out.println("Model:\n" + modelCache.getModelByNameAndGroupNames("BeanPropertyDemo", "Group1"));
        System.out.println("Model:\n" + modelCache.getModelByNameAndGroupClasses("BeanPropertyDemo", DemoClasses.Group2.class));
        System.out.println(" - Done");
    }

}
