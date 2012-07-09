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

import fi.vincit.jmobster.processor.defaults.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.RequiredTypes;
import fi.vincit.jmobster.util.ModelWriter;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;

public class SizeAnnotationProcessor extends BaseValidationAnnotationProcessor {

    public SizeAnnotationProcessor() {
        super( RequiredTypes.get( Size.class ) );
        setBaseValidatorForClass(Size.class);
    }

    @Override
    public void writeValidatorsToStreamInternal( ModelWriter writer ) {
        if( containsAnnotation(Size.class) ) {
            Size size = findAnnotation(Size.class);
            boolean minAvailable = size.min() > 0;
            boolean maxAvailable = size.max() > 0 && size.max() < Integer.MAX_VALUE;
            if( minAvailable ) {
                writer.write( "minlength: " ).write( "" + size.min());
            }
            if( minAvailable && maxAvailable ) {
                writer.writeLine(",");
            }
            if( maxAvailable ) {
                writer.write( "maxlength: " ).write( "" + size.max() );
            }
        }
    }

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((Size)annotation).groups();
    }

}
