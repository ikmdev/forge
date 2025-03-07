package dev.ikm.tinkar.forge.wrapper.of;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TextOf extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(TextOf.class);

//    @Override
//    public String methodName() {
//        return "textOf";
//    }

    @Override
    public String exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.get(0);
        GenericObjectModel genericObjectModel = (GenericObjectModel) list.get(1);
        LanguageCalculator languageCalculator = convertGenericObjectModel(genericObjectModel, LanguageCalculator.class);
        int nid = convertSimpleNumber(simpleNumber);
        return languageCalculator.getDescriptionTextOrNid(nid);
    }
}
