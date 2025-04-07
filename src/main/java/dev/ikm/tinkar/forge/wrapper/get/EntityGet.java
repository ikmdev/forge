package dev.ikm.tinkar.forge.wrapper.get;

import dev.ikm.tinkar.component.FieldDataType;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class EntityGet extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(EntityGet.class);

    @Override
    public Object exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Optional<Entity<EntityVersion>> optionalEntity = Entity.get(nid);
        FieldDataType fieldDataType;
        Entity<? extends EntityVersion> entity;
        if (optionalEntity.isPresent()) {
            fieldDataType = optionalEntity.get().entityDataType();
            entity = optionalEntity.get();
        } else {
            throw new RuntimeException("Could not find entity with id " + nid);
        }
        return switch(fieldDataType) {
            case CONCEPT_CHRONOLOGY -> (ConceptEntity<? extends ConceptEntityVersion>) entity;
            case SEMANTIC_CHRONOLOGY -> (SemanticEntity<? extends SemanticEntityVersion>) entity;
            case PATTERN_CHRONOLOGY -> (PatternEntity<? extends PatternEntityVersion>) entity;
            case STAMP -> (StampEntity<? extends StampEntityVersion>) entity;
            default -> throw new UnsupportedOperationException("Not supported entity data type: " + fieldDataType);
        };
    }
}
