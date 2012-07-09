package fi.vincit.jmobster.annotation;

/*
 * Copyright 2012 Juha Siponen
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

import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Annotation for overriding default generated JSR-303 Pattern
 * annotation regexp. The purpose of this annotation is to add flexibility
 * to pattern annotations. In some cases the default pattern converter
 * many not give the wanted target model pattern. E.g. target regexp doesn't
 * support some Java feature. In these cases this annotation can be used to
 * override the pattern with a correct pattern.
 * </p>
 * <p>
 *     This annotation is always used with the JSR-303 Pattern annotation.
 * </p>
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface OverridePattern {
    /**
     * @return The regular expression to override the generated one.
     */
    String regexp();

    /**
     * @return The error message template.
     */
    String message() default "{javax.validation.constraints.Pattern.message}";

    /**
     * @return The payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return The groups the constraint belongs to.
     */
    Class<?>[] groups() default { };
}
