package dev.ikm.tinkar.forge.wrapper.lookup.chronology;

import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SemanticGetFastMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(SemanticGetFastMethodWrapper.class);

    @Override
    public SemanticEntity<? extends SemanticEntityVersion> exec(List list) {
        int nid = extractNidFromParameters(list);
        Entity<? extends EntityVersion> entity = Entity.getFast(nid);
        return (SemanticEntity<? extends SemanticEntityVersion>) entity;
    }
}
