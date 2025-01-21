package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.entity.StampEntityVersion;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetSTAMP extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetSTAMP.class);

    @Override
    public StampEntity<? extends StampEntityVersion> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (StampEntity<? extends StampEntityVersion>) entity;
    }
}
