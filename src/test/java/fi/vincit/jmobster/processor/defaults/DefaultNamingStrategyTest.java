package fi.vincit.jmobster.processor.defaults;/*
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

import fi.vincit.jmobster.processor.model.Model;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultNamingStrategyTest {
    @Test
    public void testSimple() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        String modelName = dns.getName(String.class);
        assertEquals("String", modelName);
    }

    public static class TestClass1Dto {}

    @Test
    public void testSimpleWithDtoSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        String modelName = dns.getName(TestClass1Dto.class);
        assertEquals("TestClass1", modelName);
    }

    public static class TestClass2DTO {}

    @Test
    public void testSimpleWithDTOSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        String modelName = dns.getName(TestClass2DTO.class);
        assertEquals("TestClass2", modelName);
    }

    public static class TestClass2DtoModel {}

    @Test
    public void testSimpleWithDTOModelSuffix() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        String modelName = dns.getName(TestClass2DtoModel.class);
        assertEquals("TestClass2DtoModel", modelName);
    }

    @fi.vincit.jmobster.annotation.Model(name="NewClassname")
    public static class OverriddenTestClass {}

    @Test
    public void testOverriddenTestClass() {
        DefaultNamingStrategy dns = new DefaultNamingStrategy();
        String modelName = dns.getName(OverriddenTestClass.class);
        assertEquals("NewClassname", modelName);
    }
}
