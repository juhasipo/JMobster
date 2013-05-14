package fi.vincit.jmobster.processor.defaults;

/*
 * Copyright 2012-2013 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import fi.vincit.jmobster.util.groups.GroupMode;
import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.util.test.TestUtil;
import fi.vincit.jmobster.util.groups.GroupManager;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Collection;

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
