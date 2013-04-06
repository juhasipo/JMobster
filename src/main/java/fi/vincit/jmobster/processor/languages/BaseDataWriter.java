package fi.vincit.jmobster.processor.languages;

import fi.vincit.jmobster.util.writer.DataWriter;

public abstract class BaseDataWriter<T extends DataWriter> implements DataWriter {
    private final DataWriter writer;
    private T extendedSelf;

    protected BaseDataWriter(DataWriter writer) {
        this.writer = writer;
        this.extendedSelf = (T)this;
    }

    @Override
    public boolean isOpen() {
        return writer.isOpen();
    }

    @Override
    public T write(char c) {
        writer.write(c);
        return extendedSelf;
    }

    @Override
    public T write(String modelString) {
        writer.write(modelString);
        return extendedSelf;
    }

    @Override
    public T write(String modelString, String separator, boolean writeSeparator) {
        writer.write(modelString, separator, writeSeparator);
        return extendedSelf;
    }

    @Override
    public T writeLine(String modelStringLine) {
        writer.writeLine(modelStringLine);
        return extendedSelf;
    }

    @Override
    public T writeLine(String modelStringLine, String separator, boolean writeSeparator) {
        writer.writeLine(modelStringLine, separator, writeSeparator);
        return extendedSelf;
    }

    @Override
    public void close() {
        writer.close();
    }

    @Override
    public void setIndentation(int spaces) {
        writer.setIndentation(spaces);
    }

    @Override
    public T indent() {
        writer.indent();
        return extendedSelf;
    }

    @Override
    public T indentBack() {
        writer.indentBack();
        return extendedSelf;
    }

    @Override
    public void setIndentationChar(char indentationChar, int characterCount) {
        writer.setIndentationChar(indentationChar, characterCount);
    }

    @Override
    public void setLineSeparator(String lineSeparator) {
        writer.setLineSeparator(lineSeparator);
    }

    @Override
    public String toString() {
        return writer.toString();
    }

}
