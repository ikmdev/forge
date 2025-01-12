package dev.ikm.tinkar.forge.wrapper.lookup;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.SemanticEntity;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetSemanticFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetSemanticFastMethodWrapper.class);

    @Override
    public SemanticEntity<? extends SemanticEntityVersion> exec(List list) {
        int nid = extractNid(list);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (SemanticEntity<? extends SemanticEntityVersion>) entity;
    }
}
