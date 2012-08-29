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
import fi.vincit.jmobster.annotation.Model;
import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.defaults.CachedModelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestMain {
    private static final Logger LOG = LoggerFactory
            .getLogger( TestMain.class );

    public static class InnerClass {
        @NotNull
        public String innerString = "Inner string";
    }

    @SuppressWarnings( "MismatchedReadAndWriteOfArray" )
    @Model(name="TestModelRenamed")
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
        Long longValue = 42L;

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

    public static class DirectFieldAccessDemo {
        @Min(1)
        @Max(10)
        private int value = 4;
    }

    public static class BeanPropertyDemo {
        @OverridePattern(regexp = "foo", groups={String.class, Integer.class})
        @Pattern(regexp = "[\\w]*")
        private String firstName = "John";
        @Size(min = 0, max = 255)
        @Pattern(regexp = "[\\w]*")
        private String lastName = "Doe";


        @Size(min = 0, max = 255)
        public String getFullName() {
            return /*firstName + " " +*/ lastName;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //DataWriter modelWriter = new StreamDataWriter("models.js");
        CachedModelProvider provider = new CachedModelProvider( CachedModelProvider.WriteMode.PRETTY );
        final String HTML5 = "html5";
        final String BB = "backbone.js";
        ModelGenerator generator = JMobsterFactory.getInstance(BB, provider);

        generator.process( BeanPropertyDemo.class );

        System.out.println(provider.getModel());
    }
}
