package fi.vincit.jmobster.util.reflection;

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

import fi.vincit.jmobster.util.test.TypeUtils;
import org.junit.Test;

import java.lang.reflect.Type;

import static fi.vincit.jmobster.util.test.Matchers.isClass;
import static org.junit.Assert.assertThat;

public class CastUtilTest {

    @Test
    public void testType() {
        Class c = CastUtil.castGenericTypeToClass(TypeUtils.getNormalType());
        assertThat(c, isClass(Number.class));
    }

    @Test
    public void testWildcardType() {
        try {
            Type wildCardType = TypeUtils.getWildCardType();
            Class c = CastUtil.castGenericTypeToClass(wildCardType);
            assertThat(c, isClass(Number.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParametrizedType() {
        Class c = CastUtil.castGenericTypeToClass(TypeUtils.getParametrizedType());
        assertThat(c, isClass(Number.class));
    }
}
