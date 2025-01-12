package dev.ikm.tinkar.forge.wrapper.lookup;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetEntityFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetEntityFastMethodWrapper.class);

    @Override
    public Entity<? extends EntityVersion> exec(List list) {
        int nid = extractNid(list);
        return Entity.getFast(nid);
    }
}
