package fi.vincit.jmobster.util;

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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptionalTest {
    @Test
    public void testIsPresent() throws Exception {
        Optional<Long> optional = new Optional<Long>(10L);
        assertThat(optional.isPresent(), is(true));
    }

    @Test
    public void testNotIsPresent() throws Exception {
        Optional<Long> optional = new Optional<Long>(null);
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void testGetValue() throws Exception {
        Optional<Long> optional = new Optional<Long>(10L);
        assertThat(optional.getValue(), is(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testTryGetNullValue() throws Exception {
        Optional<Long> optional = new Optional<Long>(null);
        optional.getValue();
    }

    @Test
    public void testOptionalEmpty() throws Exception {
        Optional<Long> optional = Optional.empty();
        assertThat(optional.isPresent(), is(false));
    }
}
