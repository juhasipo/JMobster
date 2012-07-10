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
import fi.vincit.jmobster.util.OptionalTypes;
import fi.vincit.jmobster.util.RequiredTypes;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.JavaToJSPatternConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
import java.lang.annotation.Annotation;

/**
 * Processor for JSR-303 Pattern validator. Also supports optional OverridePattern annotation.
 */
public class PatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( PatternAnnotationProcessor.class );

    public PatternAnnotationProcessor() {
        super( RequiredTypes.get( Pattern.class ), OptionalTypes.get(OverridePattern.class) );
        setBaseValidatorForClass(Pattern.class);
    }

    @Override
    public void writeValidatorsToStreamInternal( ModelWriter writer ) {
        if( containsAnnotation(Pattern.class) ) {
            String javaScriptRegExp;
            if( containsAnnotation(OverridePattern.class) ) {
                OverridePattern pattern = findAnnotation(OverridePattern.class);
                javaScriptRegExp = pattern.regexp();
            } else {
                Pattern pattern = findAnnotation(Pattern.class);
                javaScriptRegExp = JavaToJSPatternConverter.convertFromJava( pattern.regexp(), pattern.flags() );
            }
            writer.write( "pattern: " ).write( javaScriptRegExp );
        }
    }

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((Pattern)annotation).groups();
    }

}
