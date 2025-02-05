package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.entity.ConceptEntity;
import dev.ikm.tinkar.entity.ConceptEntityVersion;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetConcept extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetConcept.class);

    @Override
    public ConceptEntity<? extends ConceptEntityVersion> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (ConceptEntity<? extends ConceptEntityVersion>) entity;
    }
}
