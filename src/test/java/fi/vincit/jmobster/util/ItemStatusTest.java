package fi.vincit.jmobster.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
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
}
