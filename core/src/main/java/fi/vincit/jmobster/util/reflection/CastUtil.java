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

import fi.vincit.jmobster.util.Optional;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class CastUtil {
    public static Class castGenericTypeToClass(Type genericType) {
        if( genericType instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = (ParameterizedType)genericType;
            Type actualType = parameterizedType.getActualTypeArguments()[0];
            if( actualType instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType)actualType;
                return (Class)wildcardType.getUpperBounds()[0];
            } else {
                return (Class)actualType;
            }
        } else {
            return (Class)genericType;
        }
    }

    public static class ParamType {
        public static enum Type {
            OPTIONAL,
            REQUIRED
        }
        private Class type;
        private boolean isOptional;

        private ParamType(Class paramType, Type type) {
            this.type = paramType;
            isOptional = type == Type.OPTIONAL;
        }

        public Class getType() {
            return type;
        }

        public boolean isOptional() {
            return isOptional;
        }

        public Object toParameter(Annotation annotation) {
            if( isOptional ) {
                return new Optional(annotation);
            } else {
                return annotation;
            }
        }

        public boolean isOfType(Class clazz) {
            return clazz.isAssignableFrom(type);
        }
    }

    public static ParamType resolveParamType(Type type) {
        if( type instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            if( parameterizedType.getRawType().equals(Optional.class) ) {
                Class<?> paramType = CastUtil.castGenericTypeToClass(type);
                return new ParamType(paramType, ParamType.Type.OPTIONAL);
            } else {
                throw new IllegalArgumentException("Invalid generic parameter type. Optional or Annotation expected.");
            }
        } else {
            return new ParamType((Class)type, ParamType.Type.REQUIRED);
        }
    }
}
