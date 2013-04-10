package fi.vincit.jmobster.processor.frameworks.html5;

import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.languages.html5.writer.HTML5Writer;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;

import java.io.IOException;

public class HTML5FormProcessor extends BaseModelProcessor<HTML5Writer> {
    public HTML5FormProcessor() {
        super("");
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
    }

    @Override
    public void processModel(Model model, ItemStatus status) {
    }

    @Override
    public void endProcessing(ItemStatus status) throws IOException {
    }
}
