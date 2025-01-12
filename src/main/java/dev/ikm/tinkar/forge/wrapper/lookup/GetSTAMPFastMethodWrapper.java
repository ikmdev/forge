package dev.ikm.tinkar.forge.wrapper.lookup;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.entity.StampEntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetSTAMPFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetSTAMPFastMethodWrapper.class);

    @Override
    public StampEntity<? extends StampEntityVersion> exec(List list) {
        int nid = extractNid(list);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (StampEntity<? extends StampEntityVersion>) entity;
    }
}
