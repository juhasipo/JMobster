package fi.vincit.jmobster.processor.frameworks.backbone;
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

import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     Backbone.js implementation of ModelProcessor. It will put
 *     all the given models inside a single namespace called "Models".
 * </p>
 */
public class BackboneModelProcessor extends BaseModelProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( BackboneModelProcessor.class );
    private static final String NAMESPACE_START = "{";
    private static final String MODEL_EXTEND_START = ": Backbone.Model.extend({";
    private static final String MODEL_EXTEND_END = "})";
    private static final String VARIABLE = "var";
    private static final String NAMESPACE_END = "};";
    private static final String DEFAULT_START_COMMENT = "/*\n * Auto-generated file\n */";
    private static final String DEFAULT_NAMESPACE = "Models";

    private String startComment;
    private String namespaceName;

    private BackboneModelWriter backboneModelWriter;

    /**
     * Constructs backbone model processor with a model writer that writes to the given file.
     * @param modelFilePath File path
     */
    public BackboneModelProcessor( String modelFilePath ) {
        super(modelFilePath);
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.backboneModelWriter = new BackboneModelWriter(getWriter());
    }

    /**
     * Construct slightly customized model processor with custom writer, naming strategy and annotation writer.
     * @param writer Writer
     */
    public BackboneModelProcessor(DataWriter writer) {
        super(writer);
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.backboneModelWriter = new BackboneModelWriter(writer);
    }

    /**
     * Constructs fully customized model processor.
     * @param writer Model writer to use
     * @param startComment Start command
     * @param namespaceName Namespace name
     * @param backboneModelWriter Backbone model writer
     */
    public BackboneModelProcessor(
            DataWriter writer,
            String startComment,
            String namespaceName,
            BackboneModelWriter backboneModelWriter ) {
        super(writer);
        this.startComment = startComment;
        this.namespaceName = namespaceName;
        this.backboneModelWriter = backboneModelWriter;
    }

    @Override
    public void startProcessing() throws IOException {
        getWriter().open();

        getWriter().writeLine( startComment );
        getWriter().writeLine(VARIABLE + " " + namespaceName + " = " + NAMESPACE_START);
        getWriter().indent();
    }

    @Override
    public void processModel( Model model, ItemStatus status ) {
        String modelName = model.getName();

        getWriter().write(modelName).writeLine( MODEL_EXTEND_START ).indent();

        backboneModelWriter.write(model);

        getWriter().indentBack();
        getWriter().writeLine( MODEL_EXTEND_END, ",", status.isNotLastItem() );
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing() throws IOException {
        getWriter().indentBack();
        getWriter().writeLine( NAMESPACE_END );
        getWriter().close();
    }

    /**
     * Set start commend for the model file. Note that you have include
     * comment start and end marks and wrap the comment appropriately.
     * @param startComment Start comment
     */
    public void setStartComment( String startComment ) {
        this.startComment = startComment;
    }

    /**
     * Set namespace in which the models are created
     * @param namespaceName Namespace
     */
    public void setNamespaceName( String namespaceName ) {
        this.namespaceName = namespaceName;
    }
}
