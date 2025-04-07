package dev.ikm.tinkar.forge.wrapper.on;

import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.record.Description;
import dev.ikm.tinkar.terms.TinkarTerm;
import freemarker.ext.beans.GenericObjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.ikm.tinkar.terms.EntityProxy.Concept;

public class DescriptionsOn extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescriptionsOn.class);

    @Override
    public List<Description> exec(List list) {
        GenericObjectModel entityObjectModel = (GenericObjectModel) list.get(0);
        GenericObjectModel stampObjectModel = (GenericObjectModel) list.get(1);
        Entity<? extends EntityVersion> entity = convertGenericObjectModel(entityObjectModel, Entity.class);
        StampCalculator stampCalculator = convertGenericObjectModel(stampObjectModel, StampCalculator.class);
        int languageIndex = stampCalculator.getIndexForMeaning(
                        TinkarTerm.DESCRIPTION_PATTERN.nid(),
                        TinkarTerm.LANGUAGE_CONCEPT_NID_FOR_DESCRIPTION.nid())
                .orElse(0);
        int textIndex = stampCalculator.getIndexForMeaning(
                        TinkarTerm.DESCRIPTION_PATTERN.nid(),
                        TinkarTerm.TEXT_FOR_DESCRIPTION.nid())
                .orElse(0);
        int caseSignificanceIndex = stampCalculator.getIndexForMeaning(
                        TinkarTerm.DESCRIPTION_PATTERN.nid(),
                        TinkarTerm.DESCRIPTION_CASE_SIGNIFICANCE.nid())
                .orElse(0);
        int typeIndex = stampCalculator.getIndexForMeaning(
                        TinkarTerm.DESCRIPTION_PATTERN.nid(),
                        TinkarTerm.DESCRIPTION_TYPE.nid())
                .orElse(0);

        return calculateLatestSemanticVersions(entity, stampCalculator, TinkarTerm.DESCRIPTION_PATTERN.nid())
                .stream().map(semanticEntityVersion -> new Description(
                        (Concept) semanticEntityVersion.fieldValues().get(languageIndex),
                        (String) semanticEntityVersion.fieldValues().get(textIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(caseSignificanceIndex),
                        (Concept) semanticEntityVersion.fieldValues().get(typeIndex)
                ))
                .toList();
    }
}
