package fi.vincit.jmobster.util.itemprocessor;

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
