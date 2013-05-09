package fi.vincit.jmobster.processor.builder;

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

import fi.vincit.jmobster.exception.BuildingError;
import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.defaults.DefaultModelFactory;
import fi.vincit.jmobster.processor.defaults.DefaultModelFieldFactory;
import fi.vincit.jmobster.processor.defaults.DefaultNamingStrategy;
import fi.vincit.jmobster.processor.defaults.DefaultValidatorScanner;
import fi.vincit.jmobster.util.groups.GenericGroupManager;
import fi.vincit.jmobster.util.groups.GroupMode;

/**
 * Builder that constructs new ModelFactory using default implementations. If you want
 * to customize ModelFactory more, you will have to manually build a suitable ModelFactory.
 */
public class ModelFactoryBuilder {
    // These classes may have some default values since there are no external dependencies
    private FieldScanMode scanMode = FieldScanMode.DIRECT_FIELD_ACCESS;
    private GenericGroupManager validatorGroupManager = new GenericGroupManager(GroupMode.ANY_OF_REQUIRED);
    private GenericGroupManager fieldGroupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED);

    // Following classes have dependencies so they cannot have any default values at this point
    private ModelFieldFactory modelFieldFactory;
    private ModelNamingStrategy modelNamingStrategy;
    private ValidatorScanner validatorScanner;
    private ValidatorFactory validatorFactory;

    public ModelFactoryBuilder setFieldScanMode( FieldScanMode scanMode ) {
        this.scanMode = scanMode;
        return this;
    }

    /**
     * Overrides {@link ModelFactoryBuilder#setModelFieldFactory(fi.vincit.jmobster.processor.ModelFieldFactory)}
     * @param scanMode
     * @param validatorScanner
     * @return
     */
    public ModelFactoryBuilder setFieldScanMode( FieldScanMode scanMode, ValidatorScanner validatorScanner ) {
        this.scanMode = scanMode;
        this.validatorScanner = validatorScanner;
        this.modelFieldFactory = null;
        return this;
    }

    /**
     * Overrides {@link ModelFactoryBuilder#setFieldScanMode(fi.vincit.jmobster.processor.FieldScanMode, fi.vincit.jmobster.processor.ValidatorScanner)} and
     * {@link ModelFactoryBuilder#setFieldScanMode(fi.vincit.jmobster.processor.FieldScanMode)}.
     * @param modelFieldFactory
     * @return
     */
    public ModelFactoryBuilder setModelFieldFactory(ModelFieldFactory modelFieldFactory) {
        this.modelFieldFactory = modelFieldFactory;
        return this;
    }

    public ModelFactoryBuilder setModelNamingStrategy( ModelNamingStrategy modelNamingStrategy ) {
        this.modelNamingStrategy = modelNamingStrategy;
        return this;
    }
    public ModelFactoryBuilder setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
        return this;
    }

    public ModelFactoryBuilder setValidatorGroupManager( GenericGroupManager validatorGroupManager ) {
        this.validatorGroupManager = validatorGroupManager;
        return this;
    }
    public ModelFactoryBuilder setValidatorGroups( GroupMode groupMode, Class... groups ) {
        this.validatorGroupManager = new GenericGroupManager(groupMode, groups);
        return this;
    }

    public ModelFactoryBuilder setFieldGroupManager( GenericGroupManager fieldGroupManager ) {
        this.fieldGroupManager = fieldGroupManager;
        return this;
    }
    public ModelFactoryBuilder setFieldGroups( GroupMode groupMode, Class... groups ) {
        this.fieldGroupManager = new GenericGroupManager(groupMode, groups);
        return this;
    }

    public DefaultModelFactory build() {
        if( modelNamingStrategy == null ) {
            modelNamingStrategy = new DefaultNamingStrategy();
        }

        if( this.validatorScanner == null ) {
            throwIfNull(validatorFactory, "ValidatorFactory");
            throwIfNull(validatorGroupManager, "GroupManager");
            this.validatorScanner = new DefaultValidatorScanner(this.validatorFactory, this.validatorGroupManager );
        }

        throwIfNull(fieldGroupManager, "FieldGroupManager");

        if( this.modelFieldFactory == null ) {
            if( this.scanMode == null ) {
                throw new BuildingError("Either ModelFieldFactory or ScanMode (+ optionally ValidatorScanner) has to be set.");
            }
            this.modelFieldFactory = new DefaultModelFieldFactory(this.scanMode, this.validatorScanner, fieldGroupManager);
        }
        return new DefaultModelFactory( modelFieldFactory, modelNamingStrategy );
    }

    private void throwIfNull(Object object, String name) {
        if( object == null ) {
            throw new BuildingError(name + " must be set");
        }
    }
}