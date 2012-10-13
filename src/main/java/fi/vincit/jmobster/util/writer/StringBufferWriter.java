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

import java.io.ByteArrayOutputStream;

/**
 * Data writer that writes to a String buffer (not Java's StringBuffer).
 */
public class StringBufferWriter extends StreamDataWriter {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public StringBufferWriter() {
        super();
        initializeStream( outputStream );
    }

    /**
     * Flushes written data and returns it as string
     * @return Written data
     */
    @Override
    public String toString() {
        this.flush();
        return outputStream.toString();
    }

    public void clear() {
        this.outputStream.reset();
    }
}
