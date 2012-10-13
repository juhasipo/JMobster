package fi.vincit.jmobster;
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

import fi.vincit.jmobster.annotation.IgnoreDefaultValue;
import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.defaults.validator.JSR303ValidatorFactory;
import fi.vincit.jmobster.processor.frameworks.backbone.ValidatorProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.type.BackboneFieldTypeConverterManager;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.EnumConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.util.groups.GroupMode;
import fi.vincit.jmobster.util.writer.*;
import fi.vincit.jmobster.processor.model.Model;

import javax.validation.constraints.*;
import java.io.IOException;
import java.util.*;

@SuppressWarnings( "ALL" )
public class TestMain {

    @SuppressWarnings( { "MismatchedReadAndWriteOfArray", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration" } )
    public static class InnerClass {
        @NotNull
        public String innerString = "Inner string";
    }

    @SuppressWarnings( { "MismatchedReadAndWriteOfArray", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration" } )
    public static class MyModelDto {

        @IgnoreDefaultValue
        @Size(min = 5)
        private String ignoredDefaultValue = "test string";

        @NotNull
        @Size(min = 5, max = 255)
        private String string = "test string";

        @Size(min = 5)
        @Pattern(regexp = "[a-z ]+")
        private String string2 = "test string";

        @Min(10)
        @Max(100)
        Long longValue = 42L;

        @Min(10)
        Long longValue2 = 42L;


        @Size(min = 5, max = 255)
        private long[] longArray = {1L, 2L};

        @Size()
        public String[] stringArray = {"Foo", "Bar", "FooBar", "123"};

        @NotNull
        protected List<Long> longList = new ArrayList<Long>();

        private Map<Integer, String> intStringMap = new TreeMap<Integer, String>();

        public MyModelDto() {
            longList.add(1L);
            longList.add(100L);

            intStringMap.put(1, "1 value");
            intStringMap.put(2, "2 value");
            intStringMap.put(100, "100 value");
        }

    }

    @SuppressWarnings( { "MismatchedReadAndWriteOfArray", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration" } )
    public static class DirectFieldAccessDemo {
        @Min(1)
        @Max(10)
        private int value = 4;
    }

    @SuppressWarnings( { "MismatchedReadAndWriteOfArray", "MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration", "HardcodedFileSeparator" } )
    public static class BeanPropertyDemo {
        @OverridePattern(regexp = "foo", groups={Integer.class})
        @Pattern(regexp = "[\\w]*", groups={String.class, Integer.class})
        private String firstName = "John";
        @Size(min = 0, max = 255)
        @Pattern(regexp = "[\\w]*")
        private String lastName = "Doe";

        @NotNull
        private String password;

        @Size(min = 0, max = 255)
        public String getFullName() {
            return /*firstName + " " +*/ lastName;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        //DataWriter modelWriter = new FileDataWriter("static/models.js");
        DataWriter modelWriter = new StringBufferWriter();
        CachedModelProvider provider1 = new CachedModelProvider( CachedModelProvider.WriteMode.PRETTY, modelWriter );

        final String BB = "backbone.js";

        ModelFactory factory = JMobsterFactory.getModelFactoryBuilder()
                .setFieldScanMode( FieldScanMode.DIRECT_FIELD_ACCESS )
                .setFieldGroups( GroupMode.ANY_OF_REQUIRED )
                .setValidatorGroups( GroupMode.ANY_OF_REQUIRED, String.class, Integer.class )
                .setValidatorFactory( new JSR303ValidatorFactory() )
                .build();
        Collection<Model> models = factory.createAll( BeanPropertyDemo.class, MyModelDto.class );

        JavaScriptWriter jsWriter = new JavaScriptWriter(provider1.getDataWriter());
        ModelProcessor mp = new ValidatorProcessor(
                jsWriter,
                new JavaToJSValueConverter(
                    ConverterMode.NULL_AS_DEFAULT,
                    EnumConverter.EnumMode.STRING,
                    JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN),
               new BackboneValidatorWriterManager(jsWriter));
        ModelGenerator generator = JMobsterFactory.getModelGeneratorBuilder( BB, provider1 )
                .setModelProcessor(mp)
                .build();
        //generator.setWriter(provider1.getDataWriter());
        generator.processAll( models );
/*
        generator.setWriter(provider1.getDataWriter());
        factory.setValidatorFilterGroups( GroupMode.EXACTLY_REQUIRED, String.class, Integer.class );
        Collection<Model> models2 = factory.createAll( BeanPropertyDemo.class );
        generator.processAll( models2 );

        System.out.println( provider1.getModel() );
        System.out.println( "=====================================================" );
        System.out.println( provider1.getModel() );
        */
        System.out.println(modelWriter.toString());
    }
}
