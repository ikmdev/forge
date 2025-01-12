package dev.ikm.tinkar.forge.wrapper.semantic;

import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import dev.ikm.tinkar.terms.TinkarTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.ikm.tinkar.terms.EntityProxy.Concept;

public class DescriptionsForMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescriptionsForMethodWrapper.class);

    public record Description(Concept language, String text, Concept caseSignificance, Concept descriptionType){}

    @Override
    public List<Description> exec(List list) {
        return calculateLatestSemanticVersion(list, TinkarTerm.DESCRIPTION_PATTERN).stream()
                .map(semanticEntityVersion -> {
                    StampCalculator stampCalculator = extractSTAMPCalculator(list);
                    int langIndex = stampCalculator.getIndexForMeaning(
                            TinkarTerm.DESCRIPTION_PATTERN.nid(),
                            TinkarTerm.LANGUAGE_CONCEPT_NID_FOR_DESCRIPTION.nid())
                            .getAsInt();
                    int textIndex = stampCalculator.getIndexForMeaning(
                                    TinkarTerm.DESCRIPTION_PATTERN.nid(),
                                    TinkarTerm.TEXT_FOR_DESCRIPTION.nid())
                            .getAsInt();
                    int caseSigIndex = stampCalculator.getIndexForMeaning(
                                    TinkarTerm.DESCRIPTION_PATTERN.nid(),
                                    TinkarTerm.DESCRIPTION_CASE_SIGNIFICANCE.nid())
                            .getAsInt();
                    int typeIndex = stampCalculator.getIndexForMeaning(
                                    TinkarTerm.DESCRIPTION_PATTERN.nid(),
                                    TinkarTerm.DESCRIPTION_TYPE.nid())
                            .getAsInt();

                    return new Description(
                        (Concept) semanticEntityVersion.fieldValues().get(langIndex),
                        (String) semanticEntityVersion.fieldValues().get(textIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(caseSigIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(typeIndex));
                })
                .toList();
    }
}
