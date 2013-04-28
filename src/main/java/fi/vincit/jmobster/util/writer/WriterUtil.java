package fi.vincit.jmobster.util.writer;

import fi.vincit.jmobster.processor.languages.LanguageContext;

public class WriterUtil {
    /**
     * How the model should be written
     */
    public static enum WriteMode {
        /**
         * Compact mode. No additional spaces, indentations or lines. Compact size but, hard to read by human.
         */
        COMPACT,
        /**
         * Pretty mode. Normal spaces, indentations and line changes. Human readable.
         */
        PRETTY
    }

    public static void configureMode(DataWriter writer, WriteMode mode) {
        switch (mode) {
            case COMPACT:
                configureCompactMode(writer);
                break;
            case PRETTY:
                configurePrettyMode(writer);
                break;
        }
    }

    public static void configureMode(LanguageContext<?> writer, WriteMode mode) {
        switch (mode) {
            case COMPACT:
                configureCompactMode(writer.getWriter());
                break;
            case PRETTY:
                configurePrettyMode(writer.getWriter());
                break;
        }
    }

    /**
     * Make necessary configurations for pretty write mode
     * @param dataWriter Model writer to configure
     */
    public static void configurePrettyMode(DataWriter dataWriter ) {
        dataWriter.setIndentationChar(' ', 4);
        dataWriter.setLineSeparator("\n");
    }

    /**
     * Make necessary configurations for compact write mode
     * @param dataWriter Model writer to configure
     */
    public static void configureCompactMode(DataWriter dataWriter ) {
        dataWriter.setIndentation(0);
        dataWriter.setLineSeparator("");
    }
}
