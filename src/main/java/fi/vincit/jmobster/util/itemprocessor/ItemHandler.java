package fi.vincit.jmobster.util.itemprocessor;

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

/**
 * Interface for processing a single item with {@link ItemProcessor}
 * @param <T> Type of items to process
 */
public interface ItemHandler<T> {
    /**
     * Method that processes the items. Implement item processing
     * logic here.
     * @param item Item to process
     * @param status Item processing status. Item status will change its state between calls in order to avoid
     *               unnecessary allocations so don't store the reference. Instead store the values from the methods
     *               if you really need to store them.
     */
    void process(T item, ItemStatus status);
}
