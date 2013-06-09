package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.util.writer.DataWriter;

/**
 * Writer that does not do anything
 */
public class DummyDataWriter implements DataWriter {
    private static final DummyDataWriter DUMMY_DATA_WRITER = new DummyDataWriter();

    public static DummyDataWriter getInstance() {
        return DUMMY_DATA_WRITER;
    }

    private DummyDataWriter() {}

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public DataWriter write(String modelString) {
        return this;
    }

    @Override
    public DataWriter write(String modelString, String separator, boolean writeSeparator) {
        return this;
    }

    @Override
    public DataWriter writeLine(String modelStringLine) {
        return this;
    }

    @Override
    public DataWriter writeLine(String modelStringLine, String separator, boolean writeSeparator) {
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public void setIndentation(int spaces) {
    }

    @Override
    public DataWriter indent() {
        return this;
    }

    @Override
    public DataWriter indentBack() {
        return this;
    }

    @Override
    public void setIndentationChar(char indentationChar, int characterCount) {
    }

    @Override
    public void setLineSeparator(String lineSeparator) {
    }

    @Override
    public DataWriter write(char c) {
        return this;
    }

    @Override
    public void clear() {
    }
}
