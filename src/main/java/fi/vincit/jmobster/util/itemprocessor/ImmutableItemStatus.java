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
 * Immutable version of {@link ItemStatus}. Main purpose is to
 * use these with {@link ItemStatuses} static factory so that
 * the user won't change the cached item statuses at any point.
 */
public class ImmutableItemStatus extends ItemStatus {
    public ImmutableItemStatus( int numberOfItems ) {
        super( numberOfItems );
    }

    public ImmutableItemStatus( int numberOfItems, int start ) {
        super( numberOfItems, start );
    }

    /**
     * Throws {@link UnsupportedOperationException} when called to prevent modifying the object.
     * @param itemIndex 0 based index.
     */
    @Override
    public void update( int itemIndex ) {
        throw new  UnsupportedOperationException("Cannot update immutable ItemStatus");
    }
}
