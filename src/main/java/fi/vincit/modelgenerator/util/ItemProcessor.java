package fi.vincit.modelgenerator.util;

import java.io.IOException;
import java.util.List;

public abstract class ItemProcessor<T> {
    public void process(List<? extends T> items, ItemProcessor<T> processor) throws IOException {
        for( int i = 0; i < items.size(); ++i ) {
            boolean isLastItem = i == items.size() - 1;
            processor.process( items.get( i ), isLastItem );
        }
    }

    protected abstract void process(T item, boolean isLast) throws IOException;
}
