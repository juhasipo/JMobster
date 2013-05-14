package fi.vincit.jmobster.annotation;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for marking JMobster Validators classes that
 * don't take any initialization parameters what are the
 * required annotations.
 *
 * Given annotation classes are the actual annotation classes.
 * E.g. with JSR-303 the real JSR-303 validation annotation.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface RequiresAnnotations {
    public Class[] value();
}
