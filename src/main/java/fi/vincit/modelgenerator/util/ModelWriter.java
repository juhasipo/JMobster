package fi.vincit.modelgenerator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ModelWriter {

    private static final Logger LOG = LoggerFactory
            .getLogger( ModelWriter.class );

    private FileWriter file;
    private BufferedWriter writer;
    private int indentationInSpaces;
    private int indentationInUnits;
    private boolean isLineIndented;

    public ModelWriter(String path) throws IOException {
        file = new FileWriter(path);
        writer = new BufferedWriter(file);
        indentationInSpaces = 4;
    }

    public ModelWriter(OutputStream outputStream) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        writer = new BufferedWriter(outputStreamWriter);
    }

    public void open() {
    }

    private void indentIfNeeded() throws IOException {
        if( !isLineIndented ) {
            int spacesNeeded = indentationInSpaces * indentationInUnits;
            for( int i = 0; i < spacesNeeded; ++i ) {
                writer.write(" ");
            }
            isLineIndented = true;
        }
    }

    public ModelWriter write( String modelString ) throws IOException {
        indentIfNeeded();
        writer.write(modelString);
        return this;
    }

    public ModelWriter writeLine( String modelStringLine ) throws IOException {
        indentIfNeeded();
        writer.write(modelStringLine);
        writer.write('\n');
        isLineIndented = false;
        return this;
    }

    public void close() {
        try {
            writer.close();
        } catch( IOException e ) {
            LOG.error("Error", e);
        }
        if( file != null ) {
            try {
                file.close();
            } catch( IOException e ) {
                LOG.error("Error", e);
            }
        }
    }

    public void setIndentation(int spaces) {
        this.indentationInSpaces = spaces;
    }

    public ModelWriter indent() {
        ++indentationInUnits;
        return this;
    }
    public ModelWriter indentBack() {
        --indentationInUnits;
        if( isLineIndented ) {
            try {writeLine("");} catch (IOException e) {}
        }
        return this;
    }
}
