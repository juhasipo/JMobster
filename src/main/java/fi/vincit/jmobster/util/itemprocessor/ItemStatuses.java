package fi.vincit.jmobster.util.itemprocessor;

/**
 * Collection of helper method for generating
 * ItemStatus object that are initialized to a certain
 * phase.
 */
@SuppressWarnings( "UtilityClassWithoutPrivateConstructor" )
public class ItemStatuses {

    /**
     * @return Item status which is first (but not last)
     */
    public static ItemStatus first() {
        return new ItemStatus(2);
    }

    /**
     * @return Item status which is last (but not first)
     */
    public static ItemStatus last() {
        return new ItemStatus(2, 1);
    }

    /**
     * @return Same as {@link ItemStatuses#notFirstNorLast()}
     */
    public static ItemStatus notLast() {
        return notFirstNorLast();
    }

    /**
     * @return Same as {@link ItemStatuses#notFirstNorLast()}
     */
    public static ItemStatus notFirst() {
        return notFirstNorLast();
    }

    /**
     * @return Item status which is first and last.
     */
    public static ItemStatus firstAndLast() {
        return new ItemStatus(1);
    }

    /**
     * @return Item status which is not first nor last
     */
    public static ItemStatus notFirstNorLast() {
        return new ItemStatus(3, 1);
    }

    /**
     * If given value is true, the returned item status is last but not first. If false is
     * given, the returned item status is not either last or first.
     * @param value Value
     * @return Item status
     */
    public static ItemStatus lastIfTrue(boolean value) {
        if( value ) {
            return last();
        } else {
            return notFirstNorLast();
        }
    }

    /**
     * If given value is false, the returned item status is last but not first. If true is
     * given, the returned item status is not either last or first.
     * @param value Value
     * @return Item status
     */
    public static ItemStatus lastIfFalse(boolean value) {
        return lastIfTrue(!value);
    }
}
