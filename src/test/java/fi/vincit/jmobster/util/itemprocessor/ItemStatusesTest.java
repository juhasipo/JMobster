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

import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemStatusesTest {
    @Test
    public void testFirst() throws Exception {
        ItemStatus status = ItemStatuses.first();

        assertTrue( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertFalse( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testNotFirst() throws Exception {
        ItemStatus status = ItemStatuses.notFirst();

        assertFalse( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testLast() throws Exception {
        ItemStatus status = ItemStatuses.last();

        assertFalse( status.isFirstItem() );
        assertTrue( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertFalse( status.isNotLastItem() );
    }

    @Test
    public void testNotLast() throws Exception {
        ItemStatus status = ItemStatuses.notLast();

        assertFalse( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testFirstAndLast() throws Exception {
        ItemStatus status = ItemStatuses.firstAndLast();

        assertTrue( status.isFirstItem() );
        assertTrue( status.isLastItem() );
        assertFalse( status.isNotFirstItem() );
        assertFalse( status.isNotLastItem() );
    }

    @Test
    public void testNotFirstNotLast() throws Exception {
        ItemStatus status = ItemStatuses.notFirstNorLast();

        assertFalse( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testLastIfTrueWithTrue() throws Exception {
        ItemStatus status = ItemStatuses.lastIfTrue( true );

        assertFalse( status.isFirstItem() );
        assertTrue( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertFalse( status.isNotLastItem() );
    }

    @Test
    public void testLastIfTrueWithFalse() throws Exception {
        ItemStatus status = ItemStatuses.lastIfTrue( false );

        assertFalse( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testLastIfFalseWithTrue() throws Exception {
        ItemStatus status = ItemStatuses.lastIfFalse( true );

        assertFalse( status.isFirstItem() );
        assertFalse( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertTrue( status.isNotLastItem() );
    }

    @Test
    public void testLastIfFalseWithFalse() throws Exception {
        ItemStatus status = ItemStatuses.lastIfFalse( false );

        assertFalse( status.isFirstItem() );
        assertTrue( status.isLastItem() );
        assertTrue( status.isNotFirstItem() );
        assertFalse( status.isNotLastItem() );
    }
}
