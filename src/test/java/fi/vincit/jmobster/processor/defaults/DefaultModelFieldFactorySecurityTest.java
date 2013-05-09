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

import fi.vincit.jmobster.processor.FieldScanMode;
import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.TestUtil;
import fi.vincit.jmobster.util.groups.GenericGroupManager;
import org.junit.After;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.security.Permission;
import java.util.List;

import static fi.vincit.jmobster.util.TestUtil.assertFieldNotFound;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests with SecurityManager
 */
public class DefaultModelFieldFactorySecurityTest {

    @After
    public void tearDown() {
        disableSecurityManager();
    }

    public static class SecurityTestClass {
        protected Integer protectedIntegerField;
        private String privateStringField;
    }

    public static class AllowChangeSecurityManager extends SecurityManager {
        @Override
        public void checkPermission( Permission permission ) {
            try {
                // Allow changing security manager
                if( permission.getName().equals("setSecurityManager") ) {
                    return;
                }
                super.checkPermission( permission );
            } catch (RuntimeException e) {
                // Only check setAccessible accesses
                if( permission.getName().equals("suppressAccessChecks") ) {
                    throw e;
                }
            }
        }
    }

    private static void enableSecurityManager() {
        if( System.getSecurityManager() == null ) {
            SecurityManager security = new AllowChangeSecurityManager();
            System.setSecurityManager(security);
        }
    }

    private static void disableSecurityManager() {
        System.setSecurityManager( null );
        assert System.getSecurityManager() == null;
    }

    @Test
    public void testGetFields() {
        DefaultModelFieldFactory fs = getFieldScanner( FieldScanMode.DIRECT_FIELD_ACCESS );

        enableSecurityManager();
        List<ModelField> models = fs.getFields( SecurityTestClass.class );
        disableSecurityManager();

        assertFieldNotFound( models, "protectedIntegerField" );
        assertFieldNotFound( models, "privateStringField" );
    }

    private DefaultModelFieldFactory getFieldScanner(FieldScanMode scanMode) {
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        when(validatorScanner.getValidators(any(Field.class)))
                .thenReturn( TestUtil.collectionFromObjects( mock( Validator.class ) ) );
        when(validatorScanner.getValidators(any(PropertyDescriptor.class)))
                .thenReturn(TestUtil.collectionFromObjects(mock(Validator.class)));
        GenericGroupManager fieldGroupManager = mock(GenericGroupManager.class);
        return new DefaultModelFieldFactory(scanMode, validatorScanner, fieldGroupManager);
    }
}
