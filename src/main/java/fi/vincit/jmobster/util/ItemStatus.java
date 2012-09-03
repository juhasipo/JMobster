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

public class ItemStatus {
    final private int numberOfItems;
    boolean firstItem;
    boolean lastItem;

    public ItemStatus(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void update(int itemIndex) {
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
}
