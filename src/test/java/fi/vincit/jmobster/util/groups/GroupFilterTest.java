package fi.vincit.jmobster.util.groups;

import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupFilterTest {

    public static class Obj1 implements HasGroups<Class> {
        @Override
        public Class[] getGroups() {
            return new Class[] { String.class, Integer.class };
        }

        @Override
        public boolean hasGroups() {
            return true;
        }
    }

    public static class Obj2 implements HasGroups<Class> {
        @Override
        public Class[] getGroups() {
            return new Class[] { String.class };
        }

        @Override
        public boolean hasGroups() {
            return true;
        }
    }

    @Test
    public void testFilterAnyOfRequired() {
        GroupManager<Class> mgr = new GenericGroupManager<Class>( GroupMode.ANY_OF_REQUIRED, Integer.class );
        GroupFilter<HasGroups<Class>, Class> filter = new GroupFilter<HasGroups<Class>, Class>(mgr);

        Collection<HasGroups<Class>> filtered = filter.filterByGroups( TestUtil.collectionFromObjects(new Obj1(), new Obj2()) );
        assertEquals(1, filtered.size());
    }

    @Test
    public void testFilterExactlyRequired() {
        GroupManager<Class> mgr = new GenericGroupManager<Class>( GroupMode.EXACTLY_REQUIRED, String.class );
        GroupFilter<HasGroups<Class>, Class> filter = new GroupFilter<HasGroups<Class>, Class>(mgr);

        Collection<HasGroups<Class>> filtered = filter.filterByGroups( TestUtil.collectionFromObjects(new Obj1(), new Obj2()) );
        assertEquals(1, filtered.size());
    }
}
