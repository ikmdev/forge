package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.PatternEntity;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetPattern extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(GetPattern.class);

    @Override
    public PatternEntity<? extends PatternEntityVersion> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (PatternEntity<? extends PatternEntityVersion>) entity;
    }
}
