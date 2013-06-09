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

/**
 * Item status is a helper method for iterating items with
 * {@link ItemProcessor} class. Item status tells in which part
 * the processing is. Initialize the Item status with the item
 * count and then call {@link ItemStatus#update(int)} method with
 * the current index.
 */
public class ItemStatus {
    final private int numberOfItems;
    private boolean firstItem;
    private boolean lastItem;

    /**
     * Initializes with given number of items and
     * assumes to start from index 0.
     * @param numberOfItems Number of items to process
     */
    public ItemStatus(int numberOfItems) {
        this(numberOfItems, 0);
    }

    /**
     * Initializes with given number of items and
     * assumes to start from index 0.
     * @param numberOfItems Number of items to process
     * @param start 0 based start index
     */
    public ItemStatus(int numberOfItems, int start) {
        this.numberOfItems = numberOfItems;
        updateInternal(start);
    }

    /**
     * Update the processing
     * @param itemIndex 0 based index.
     */
    public void update(int itemIndex) {
        updateInternal(itemIndex);
    }

    /**
     * Update the processing
     * @param itemIndex 0 based index.
     */
    private void updateInternal(int itemIndex) {
        firstItem = itemIndex == 0;
        lastItem = itemIndex == (numberOfItems-1);
    }

    public boolean isFirstItem() {
        return firstItem;
    }

    public boolean isLastItem() {
        return lastItem;
    }

    public boolean isNotLastItem() {
        return !lastItem;
    }
    public boolean isNotFirstItem() {
        return !firstItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStatus)) return false;

        ItemStatus that = (ItemStatus) o;

        if (firstItem != that.firstItem) return false;
        if (lastItem != that.lastItem) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (firstItem ? 1 : 0);
        result = 31 * result + (lastItem ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ItemStatus{" +
                "numberOfItems=" + numberOfItems +
                ", firstItem=" + firstItem +
                ", lastItem=" + lastItem +
                '}';
    }
}
