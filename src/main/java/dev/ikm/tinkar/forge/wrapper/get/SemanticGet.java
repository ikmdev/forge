package dev.ikm.tinkar.forge.wrapper.get;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.SemanticEntity;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SemanticGet extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(SemanticGet.class);

    @Override
    public SemanticEntity<? extends SemanticEntityVersion> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.getFirst();
        int nid = convertSimpleNumber(simpleNumber);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (SemanticEntity<? extends SemanticEntityVersion>) entity;
    }
}
