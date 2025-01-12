package dev.ikm.tinkar.forge.wrapper.lookup;

import dev.ikm.tinkar.entity.ConceptEntity;
import dev.ikm.tinkar.entity.ConceptEntityVersion;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetConceptFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetConceptFastMethodWrapper.class);

    @Override
    public ConceptEntity<? extends ConceptEntityVersion> exec(List list) {
        int nid = extractNid(list);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (ConceptEntity<? extends ConceptEntityVersion>) entity;
    }
}
