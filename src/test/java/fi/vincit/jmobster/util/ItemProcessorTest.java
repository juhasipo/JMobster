package fi.vincit.jmobster.util;
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


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemProcessorTest {

    public static class LastItemCaller {
        public int calledWithLastTrue;
        public int calledWithLastFalse;
        void call(boolean last) {
            if( last ) {
                ++calledWithLastTrue;
            } else {
                ++calledWithLastFalse;
            }
        }
    }

    public ItemProcessor<String> getItemProcessor(final StringBuilder sb, final LastItemCaller lic) {
        ItemProcessor<String> itemProcessor = new ItemProcessor<String>() {
            @Override
            protected void process(String item, boolean isLast) {
                sb.append(item);
                lic.call(isLast);
            }
        };
        return itemProcessor;
    }

    @Test
    public void testOneItem() {
        List<String> list = new ArrayList<String>();
        list.add("item1");

        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemProcessor<String> itemProcessor = getItemProcessor(sb,  lic);

        itemProcessor.process(list);

        assertEquals("item1", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testNoItems() {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemProcessor<String> itemProcessor = getItemProcessor(sb,  lic);

        itemProcessor.process(list);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testMultipleItems() {
        List<String> list = new ArrayList<String>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        list.add("item4");

        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemProcessor<String> itemProcessor = getItemProcessor(sb,  lic);

        itemProcessor.process(list);

        assertEquals("item1item2item3item4", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(3, lic.calledWithLastFalse);
    }

    @Test
    public void testNullItems() {
        List<String> list = null;

        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemProcessor<String> itemProcessor = getItemProcessor(sb,  lic);

        itemProcessor.process(list);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }
}
