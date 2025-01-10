package dev.ikm.tinkar.forge.wrapper.lookup.chronology;

import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntityGetFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(EntityGetFastMethodWrapper.class);

    @Override
    public Entity<? extends EntityVersion> exec(List list) {
        int nid = extractNidFromParameters(list);
        return Entity.getFast(nid);
    }
}
