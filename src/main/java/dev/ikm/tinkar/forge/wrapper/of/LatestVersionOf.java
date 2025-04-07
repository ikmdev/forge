package dev.ikm.tinkar.forge.wrapper.of;

import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.ext.beans.GenericObjectModel;

import java.util.List;

public class LatestVersionOf extends ForgeMethodWrapper {

    @Override
    public Object exec(List list) {
        GenericObjectModel entityObjectModel = (GenericObjectModel) list.get(0);
        GenericObjectModel stampObjectModel = (GenericObjectModel) list.get(1);
        Entity<? extends EntityVersion> entity = convertGenericObjectModel(entityObjectModel, Entity.class);
        StampCalculator stampCalculator = convertGenericObjectModel(stampObjectModel, StampCalculator.class);
        return switch (entity.entityDataType()) {
            case CONCEPT_CHRONOLOGY -> (Latest<ConceptEntityVersion>) stampCalculator.latest(entity);
            case SEMANTIC_CHRONOLOGY -> (Latest<SemanticEntityVersion>) stampCalculator.latest(entity);
            case PATTERN_CHRONOLOGY -> (Latest<PatternEntityVersion>) stampCalculator.latest(entity);
            case STAMP -> (Latest<StampEntityVersion>) stampCalculator.latest(entity);
            default -> throw new UnsupportedOperationException("Unsupported entity data type: " + entity.entityDataType());
        };
    }
}
