package fi.vincit.jmobster.util.groups;

import fi.vincit.jmobster.processor.GroupMode;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassGroupManagerTest {
    @Test
    public void testConstructAndMatch() throws Exception {
        ClassGroupManager groupManager = new ClassGroupManager( GroupMode.AT_LEAST_REQUIRED, String.class );
        final HasGroups<Class> m = mock(HasGroups.class);
        when(m.getGroups()).thenReturn(new Class[] {String.class});
        assertTrue(groupManager.match(m));
    }
}
