package fi.vincit.jmobster;/*
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

import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.StreamModelWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class JMobsterFactoryTest {

    private ModelWriter getModelWriter() {
        return new StreamModelWriter(new ByteArrayOutputStream());
    }

    @Test
    public void testCreateBackboneInstance() throws IOException {
        ModelGenerator generator = JMobsterFactory.getInstance("Backbone", getModelWriter());
        assertNotNull(generator);
    }

    @Test
    public void testCreateBackboneJSInstance() throws IOException {
        ModelGenerator generator = JMobsterFactory.getInstance("Backbone.js", getModelWriter());
        assertNotNull(generator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUnsupportedInstance() throws IOException {
        JMobsterFactory.getInstance("Invalid framework", getModelWriter());
    }
}
