package fi.vincit.jmobster.util;

import java.util.List;

/**
 * Utility class for iterating over list. The reason this class
 * exists is that the process method tells which processed item
 * is the last item in the list.
 * @param <T> Type of objects in the item list
 */
public abstract class ItemProcessor<T> {
    /**
     * Process the given list.
     * @param items Items to process
     */
    public void process(List<? extends T> items) {
        for( int i = 0; i < items.size(); ++i ) {
            boolean isLastItem = i == items.size() - 1;
            this.process( items.get( i ), isLastItem );
        }
    }

    /**
     * Method that processes the items.
     * @param item Item to process
     * @param isLast Is the given item last item in the list
     */
    protected abstract void process(T item, boolean isLast);
}
