package fi.vincit.jmobster.util;

/**
 * Collection of helper method for generating
 * ItemStatus object that are initialized to a certain
 * phase.
 */
public class ItemStatusHelper {

    public static ItemStatus firstOfMany() {
        return new ItemStatus(2);
    }

    public static ItemStatus lastOfMany() {
        ItemStatus status = new ItemStatus(2);
        status.update(1);
        return status;
    }

    public static ItemStatus firstAndLast() {
        return new ItemStatus(1);
    }

    public static ItemStatus inMiddle() {
        ItemStatus status = new ItemStatus(3);
        status.update(1);
        return status;
    }
}
