package dev.ikm.tinkar.forge.wrapper.lookup;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.PatternEntity;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetPatternFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetPatternFastMethodWrapper.class);

    @Override
    public PatternEntity<? extends PatternEntityVersion> exec(List list) {
        int nid = extractNid(list);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (PatternEntity<? extends PatternEntityVersion>) entity;
    }
}
