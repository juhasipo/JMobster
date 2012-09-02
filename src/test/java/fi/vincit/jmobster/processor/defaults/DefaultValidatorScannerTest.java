package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.defaults.validator.SizeValidator;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.TestUtil;
import fi.vincit.jmobster.util.groups.GroupManager;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Collection;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultValidatorScannerTest {
    @Test
    public void testGetValidatorsFromField() throws Exception {
        ValidatorFactory validatorFactory = mock(ValidatorFactory.class);
        GroupManager<Class> groupManager = mock(GroupManager.class);
        DefaultValidatorScanner validatorScanner = new DefaultValidatorScanner(validatorFactory, groupManager);

        class T {@NotNull int field;}
        validatorScanner.getValidators(T.class.getDeclaredFields()[0]);

        verify(validatorFactory,times(1)).createValidators(any(Collection.class));
    }

    @Test
    public void testGetValidatorsFromProperty() throws Exception {
        ValidatorFactory validatorFactory = mock(ValidatorFactory.class);
        GroupManager<Class> groupManager = mock(GroupManager.class);
        DefaultValidatorScanner validatorScanner = new DefaultValidatorScanner(validatorFactory, groupManager);

        class T {int field; @NotNull public int getField() { return field; }}
        final BeanInfo beanInfo = Introspector.getBeanInfo( T.class );
        validatorScanner.getValidators(beanInfo.getPropertyDescriptors()[0]);

        verify(validatorFactory,times(1)).createValidators(any(Collection.class));
    }

    @Test
    public void testSetFilterGroups() throws Exception {
        ValidatorFactory validatorFactory = mock(ValidatorFactory.class);
        GroupManager<Class> groupManager = mock(GroupManager.class);
        DefaultValidatorScanner validatorScanner = new DefaultValidatorScanner(validatorFactory, groupManager);

        Collection<Class> groups = (Collection)TestUtil.listFromObjects( String.class );
        validatorScanner.setFilterGroups( GroupMode.EXACTLY_REQUIRED, groups );
        verify( groupManager, times(1) ).setGroups( eq( GroupMode.EXACTLY_REQUIRED ), any( Collection.class ) );
    }
}
