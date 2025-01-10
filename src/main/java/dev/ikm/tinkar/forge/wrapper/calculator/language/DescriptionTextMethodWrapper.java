package dev.ikm.tinkar.forge.wrapper.calculator.language;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DescriptionTextMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescriptionTextMethodWrapper.class);

    private final LanguageCalculator languageCalculator;

    public DescriptionTextMethodWrapper(LanguageCalculator languageCalculator) {
        this.languageCalculator = languageCalculator;
    }

    @Override
    public String exec(List list) {
        int nid = extractNidFromParameters(list);
        return languageCalculator.getDescriptionTextOrNid(nid);
    }
}
