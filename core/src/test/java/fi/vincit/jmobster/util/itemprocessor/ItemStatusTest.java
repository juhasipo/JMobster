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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ItemStatusTest {
    @Test
    public void testFirstOfMany() {
        ItemStatus status = new ItemStatus(10);
        status.update(0);

        assertTrue(status.isFirstItem());
        assertFalse(status.isLastItem());
        assertFalse(status.isNotFirstItem());
        assertTrue(status.isNotLastItem());
    }

    @Test
    public void testFirstOfOne() {
        ItemStatus status = new ItemStatus(1);
        status.update(0);

        assertTrue(status.isFirstItem());
        assertTrue(status.isLastItem());
        assertFalse(status.isNotFirstItem());
        assertFalse(status.isNotLastItem());
    }

    @Test
    public void testLastOfMany() {
        ItemStatus status = new ItemStatus(10);
        status.update(9);

        assertFalse(status.isFirstItem());
        assertTrue(status.isLastItem());
        assertTrue(status.isNotFirstItem());
        assertFalse(status.isNotLastItem());
    }

    @Test
    public void testSecond() {
        ItemStatus status = new ItemStatus(10);
        status.update(1);

        assertFalse(status.isFirstItem());
        assertFalse(status.isLastItem());
        assertTrue(status.isNotFirstItem());
        assertTrue(status.isNotLastItem());
    }

    @Test
    public void testInSecondLast() {
        ItemStatus status = new ItemStatus(10);
        status.update(8);

        assertFalse(status.isFirstItem());
        assertFalse(status.isLastItem());
        assertTrue(status.isNotFirstItem());
        assertTrue(status.isNotLastItem());
    }

    @Test
    public void testInMiddle() {
        ItemStatus status = new ItemStatus(10);
        status.update(5);

        assertFalse(status.isFirstItem());
        assertFalse(status.isLastItem());
        assertTrue(status.isNotFirstItem());
        assertTrue(status.isNotLastItem());
    }

    @Test
    public void testOverTheMax() {
        ItemStatus status = new ItemStatus(10);
        status.update(10);

        assertFalse(status.isFirstItem());
        assertFalse(status.isLastItem());
        assertTrue(status.isNotFirstItem());
        assertTrue(status.isNotLastItem());
    }


    @Test
    public void testEqualsFirst_SameNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(0);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(0);

        assertThat(status1, is(status2));
    }

    @Test
    public void testEqualsFirst_DifferentNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(0);

        ItemStatus status2 = new ItemStatus(100);
        status2.update(0);

        assertThat(status1, is(status2));
    }

    @Test
    public void testEqualsLast_SameNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(9);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(9);

        assertThat(status1, is(status2));
    }

    @Test
    public void testEqualsLast_DifferentNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(9);

        ItemStatus status2 = new ItemStatus(100);
        status2.update(99);

        assertThat(status1, is(status2));
    }

    @Test
    public void testEqualsInMiddle_SameNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(3);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(5);

        assertThat(status1, is(status2));
    }

    @Test
    public void testEqualsInMiddle_DifferentNumberOfItems() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(4);

        ItemStatus status2 = new ItemStatus(100);
        status2.update(54);

        assertThat(status1, is(status2));
    }

    @Test
    public void testNotEqualsInMiddle_FirstAndMiddle() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(0);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(5);

        assertThat(status1, is(not(status2)));
    }

    @Test
    public void testNotEqualsInMiddle_FirstAndLast() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(0);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(9);

        assertThat(status1, is(not(status2)));
    }

    @Test
    public void testNotEqualsInMiddle_LastAndMiddle() {
        ItemStatus status1 = new ItemStatus(10);
        status1.update(9);

        ItemStatus status2 = new ItemStatus(10);
        status2.update(5);

        assertThat(status1, is(not(status2)));
    }

}
