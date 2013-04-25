package fi.vincit.jmobster.util.writer;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Data writer that writes to a file.
 */
public class FileDataWriter extends StreamDataWriter {

    private static final Logger LOG = LoggerFactory.getLogger( FileDataWriter.class );

    final private String path;
    final private FileWriter file;

    /**
     * Initializes FileDataWriter and opens file with given path
     * for writing.
     * @param path File path
     * @throws IOException
     */
    public FileDataWriter( String path ) throws IOException {
        this.path = path;
        this.file = new FileWriter(path);
        initializeBuffer(new BufferedWriter(file));
    }

    @Override
    public void close() {
        try {
            super.close();
            if( file != null ) {
                file.close();
            }
        } catch( IOException e ) {
            LOG.error("Error", e);
        }
    }

    /**
     * @return Path
     */
    public String getPath() {
        return this.path;
    }

    @Override
    public void clear() {
        // No-op - can't clear a file
    }
}
