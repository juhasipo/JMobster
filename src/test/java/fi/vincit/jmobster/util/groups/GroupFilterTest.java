package fi.vincit.jmobster.util.groups;

import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class GroupFilterTest {

    public static interface Group1 {}
    public static interface Group2 {}
    
    public static class Obj1 implements HasGroups<Class> {
        @Override
        public Class[] getGroups() {
            return new Class[] { Group2.class, Group1.class };
        }

        @Override
        public boolean hasGroups() {
            return true;
        }
    }

    public static class Obj2 implements HasGroups<Class> {
        @Override
        public Class[] getGroups() {
            return new Class[] { Group2.class };
        }

        @Override
        public boolean hasGroups() {
            return true;
        }
    }
    
    

    @Test
    public void testFilterAnyOfRequired() {
        GroupManager<Class> mgr = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class );
        GroupFilter<HasGroups<Class>, Class> filter = new GroupFilter<HasGroups<Class>, Class>(mgr);

        Collection<HasGroups<Class>> filtered = filter.filterByGroups( TestUtil.collectionFromObjects(new Obj1(), new Obj2()) );
        assertEquals(1, filtered.size());
    }

    @Test
    public void testFilterExactlyRequired() {
        GroupManager<Class> mgr = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group2.class );
        GroupFilter<HasGroups<Class>, Class> filter = new GroupFilter<HasGroups<Class>, Class>(mgr);

        Collection<HasGroups<Class>> filtered = filter.filterByGroups( TestUtil.collectionFromObjects(new Obj1(), new Obj2()) );
        assertEquals(1, filtered.size());
    }
}
