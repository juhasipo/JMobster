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

import java.util.Collection;

/**
 * <p>
 *     Utility class for iterating over list. The reason this class
 * exists is that the process method tells which processed item
 * is the last item in the list.
 * </p>
 * <p>
 *     Recommended syntax: ItemProcessor.process(items).with(handler);
 *
 *     This will create a new ItemProcessor instance for each {@link ItemProcessor#process(java.util.Collection)}
 *     call.
 * </p>
 * <p>
 *     Sometimes it may be more efficient not to create new object instances.
 *     For this methods {@link ItemProcessor#process(ItemHandler, Object[])} and
 *     {@link ItemProcessor#process(ItemHandler, java.util.Collection)} can be used.
 *     These static methods don't create unnecessary ItemProcessor objects.
 * </p>
 *
 * <p>
 *     Combine with {@link fi.vincit.jmobster.util.writer.DataWriter}'s {@link fi.vincit.jmobster.util.writer.DataWriter#write(String, String, boolean)} and
 * {@link fi.vincit.jmobster.util.writer.DataWriter#writeLine(String, String, boolean)} to write lists with last separator
 * char left out.
 * </p>
 */
public class ItemProcessor<T> {

    private Collection<? extends T> items;

    protected ItemProcessor( Collection<? extends T> items)  {
        this.items = items;
    }

    public static <T> ItemProcessor<T> process(Collection<? extends T> items) {
        return new ItemProcessor<T>(items);
    }
    public void with(ItemHandler<? super T> itemHandler) {
        ItemProcessor.process(itemHandler, this.items);
    }

    /**
     * Process the given list of items.
     * @param items Items to process. If null nothing is done.
     */
    public static <T> void process(ItemHandler<? super T> itemHandler, Collection<? extends T> items) {
        if( items == null ) {
            return;
        }

        int i = 0;
        final ItemStatus status = new ItemStatus(items.size());
        for( T item : items ) {
            status.update(i);
            itemHandler.process( item, status );
            ++i;
        }
    }

    /**
     * Process the given array of items.
     * @param items Items to process. If null nothing is done.
     */
    public static <T> void process(ItemHandler<? super T> itemHandler, T... items) {
        if( items == null ) {
            return;
        }

        final ItemStatus status = new ItemStatus(items.length);
        for( int i = 0; i < items.length; ++i ) {
            status.update(i);
            itemHandler.process( items[i], status );
        }
    }
}
