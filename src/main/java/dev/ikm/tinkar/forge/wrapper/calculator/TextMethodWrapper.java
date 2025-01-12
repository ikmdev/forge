package dev.ikm.tinkar.forge.wrapper.calculator;

import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TextMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(TextMethodWrapper.class);

    @Override
    public String exec(List list) {
        int nid = extractNid(list);
        return extractLanguageCalculator(list).getDescriptionTextOrNid(nid);
    }
}
