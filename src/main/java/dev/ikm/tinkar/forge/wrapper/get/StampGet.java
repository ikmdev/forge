package dev.ikm.tinkar.forge.wrapper.get;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.entity.StampEntityVersion;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StampGet extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(StampGet.class);

    @Override
    public StampEntity<? extends StampEntityVersion> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (StampEntity<? extends StampEntityVersion>) entity;
    }
}
