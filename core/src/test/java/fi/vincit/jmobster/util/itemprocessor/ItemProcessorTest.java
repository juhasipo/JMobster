package fi.vincit.jmobster.util.itemprocessor;
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


import fi.vincit.jmobster.util.test.TestUtil;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class ItemProcessorTest {

    public static class LastItemCaller {
        public int calledWithLastTrue;
        public int calledWithLastFalse;
        void callLast( boolean last ) {
            if( last ) {
                ++calledWithLastTrue;
            } else {
                ++calledWithLastFalse;
            }
        }
    }

    public ItemHandler<String> getItemProcessor(final StringBuilder sb, final LastItemCaller lic) {
        ItemHandler<String> itemProcessor = new ItemHandler<String>() {
            @Override
            public void process(String item, ItemStatus status) {
                sb.append(item);
                lic.callLast( status.isLastItem() );
            }
        };
        return itemProcessor;
    }

    @Test
    public void testOneItem() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process( (List)TestUtil.listFromObjects( "item1" ) ).with(itemProcessor);

        assertEquals("item1", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testNoItems() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process((List)TestUtil.listFromObjects()).with(itemProcessor);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testMultipleItems() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process((List)TestUtil.listFromObjects("item1", "item2", "item3", "item4")).with(itemProcessor);

        assertEquals("item1item2item3item4", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(3, lic.calledWithLastFalse);
    }


    @Test
    public void testOneItemInArray() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process(itemProcessor, new String[] {"item1"});

        assertEquals("item1", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testNoItemsInArray() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process(itemProcessor);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testMultipleItemsInArray() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process(itemProcessor, new String[] {"item1", "item2", "item3", "item4"});

        assertEquals("item1item2item3item4", sb.toString());
        assertEquals(1, lic.calledWithLastTrue);
        assertEquals(3, lic.calledWithLastFalse);
    }

    @Test
    public void testNullItems() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        ItemProcessor.process(itemProcessor, (List)null);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }

    @Test
    public void testNullArrayItems() {
        StringBuilder sb = new StringBuilder();
        LastItemCaller lic = new LastItemCaller();
        ItemHandler<String> itemProcessor = getItemProcessor(sb,  lic);

        String[] strings = null;
        ItemProcessor.process(itemProcessor, strings);

        assertEquals("", sb.toString());
        assertEquals(0, lic.calledWithLastTrue);
        assertEquals(0, lic.calledWithLastFalse);
    }
}
