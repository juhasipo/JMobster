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

import java.util.List;

/**
 * Utility class for iterating over list. The reason this class
 * exists is that the process method tells which processed item
 * is the last item in the list.
 * @param <T> Type of objects in the item list
 */
public abstract class ItemProcessor<T> {
    /**
     * Process the given list.
     * @param items Items to process
     */
    public void process(List<? extends T> items) {
        for( int i = 0; i < items.size(); ++i ) {
            boolean isLastItem = i == items.size() - 1;
            this.process( items.get( i ), isLastItem );
        }
    }

    /**
     * Method that processes the items.
     * @param item Item to process
     * @param isLast Is the given item last item in the list
     */
    protected abstract void process(T item, boolean isLast);
}
