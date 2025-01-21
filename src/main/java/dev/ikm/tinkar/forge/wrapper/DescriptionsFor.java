package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import dev.ikm.tinkar.terms.TinkarTerm;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.OptionalInt;

import static dev.ikm.tinkar.terms.EntityProxy.Concept;

public class DescriptionsFor extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescriptionsFor.class);

    public record Description(Concept language, String text, Concept caseSignificance, Concept descriptionType){}

    @Override
    public List<Description> exec(List list) {
        SimpleNumber componentSimpleNumber = (SimpleNumber) list.get(0);
        GenericObjectModel stampObjectModel = (GenericObjectModel) list.get(1);
        int descriptionPattern = TinkarTerm.DESCRIPTION_PATTERN.nid();
        StampCalculator stampCalculator = convertGenericObjectModel(stampObjectModel, StampCalculator.class);
        Latest<PatternEntityVersion> latest = stampCalculator.latest(TinkarTerm.DESCRIPTION_PATTERN);
        int languageIndex =  latest.get().indexForMeaning(TinkarTerm.LANGUAGE_CONCEPT_NID_FOR_DESCRIPTION);
        int textIndex =  latest.get().indexForMeaning(TinkarTerm.TEXT_FOR_DESCRIPTION);
        int caseSignificanceIndex =  latest.get().indexForMeaning(TinkarTerm.DESCRIPTION_CASE_SIGNIFICANCE);
        int typeIndex =  latest.get().indexForMeaning(TinkarTerm.DESCRIPTION_TYPE);
        return calculateLatestSemanticVersions(componentSimpleNumber, stampObjectModel, descriptionPattern).stream()
                .map(semanticEntityVersion -> new Description(
                        (Concept) semanticEntityVersion.fieldValues().get(languageIndex),
                        (String) semanticEntityVersion.fieldValues().get(textIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(caseSignificanceIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(typeIndex)
                ))
                .toList();
    }
}
