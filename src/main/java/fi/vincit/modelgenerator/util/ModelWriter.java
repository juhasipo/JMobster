package fi.vincit.modelgenerator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Class that writes strings to file or given OutputStream. Also
 * handles indentation.
 */
public class ModelWriter {

    private static final Logger LOG = LoggerFactory
            .getLogger( ModelWriter.class );
    public static final String LINE_SEPARATOR = "\n";

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

    @SuppressWarnings( "EmptyMethod" )
    public void open() {
        // Nothing to do
    }

    private void indentIfNeeded() {
        if( !isLineIndented ) {
            int spacesNeeded = indentationInSpaces * indentationInUnits;
            for( int i = 0; i < spacesNeeded; ++i ) {
                writeInternal( " " );
            }
            isLineIndented = true;
        }
    }

    private void writeInternal(String string) {
        try {
            writer.write(string);
        } catch (IOException e) {
            LOG.error("Could not write", e);
        }
    }

    public ModelWriter write( String modelString ) {
        indentIfNeeded();
        writeInternal( modelString );
        return this;
    }

    public ModelWriter write( String modelString, String separator, boolean writeSeparator ) {
        write(modelString);
        if( writeSeparator ) {
            write(separator);
        }
        return this;
    }

    public ModelWriter writeLine( String modelStringLine ) {
        indentIfNeeded();
        writeInternal( modelStringLine );
        writeInternal( LINE_SEPARATOR );
        isLineIndented = false;
        return this;
    }

    public ModelWriter writeLine( String modelStringLine, String separator, boolean writeSeparator ) {
        write(modelStringLine);
        if( writeSeparator ) {
            writeLine(separator);
        } else {
            writeLine("");
        }
        return this;
    }

    /**
     * Close the stream and file if necessary.
     */
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

    /**
     * Sets indentation size in spaces
     * @param spaces Indentation size in spaces
     */
    public void setIndentation(int spaces) {
        this.indentationInSpaces = spaces;
    }

    /**
     * Indent (right)
     * @return ModelWriter to enable chaining calls.
     */
    public ModelWriter indent() {
        ++indentationInUnits;
        return this;
    }
    /**
     * Indent back (left). If not new line, will change line.
     * @return ModelWriter to enable chaining calls.
     */
    public ModelWriter indentBack() {
        if( indentationInUnits > 0 ) {
            --indentationInUnits;
        }
        if( isLineIndented ) {
            writeLine("");
        }
        return this;
    }
}
