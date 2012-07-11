package fi.vincit.jmobster.processor.frameworks.backbone.annotation;

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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;

/**
 * Base validator processor for {@link OverridePattern} annotation. Only extracts
 * groups and tells other components that OverridePattern is a validation annotation.
 */
public class OverridePatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    public OverridePatternAnnotationProcessor() {
        super( OverridePattern.class );
    }

    @Override
    protected Class[] getGroupsInternal( Annotation annotation ) {
        return ((OverridePattern)annotation).groups();
    }

    @Override
    protected void writeValidatorsToStreamInternal( ModelWriter writer ) {
        // Do nothing since this is only a base validator processor
    }
}
