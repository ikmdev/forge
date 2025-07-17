package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ForgeMethodWrapper implements TemplateMethodModelEx {

    private final Logger LOG = LoggerFactory.getLogger(ForgeMethodWrapper.class);

    protected  <T> T convertGenericObjectModel(GenericObjectModel genericObjectModel, Class<T> clazz) {
        return (T) genericObjectModel.getAdaptedObject(clazz);
    }

    protected int convertSimpleNumber(SimpleNumber simpleNumber) {
        return simpleNumber.getAsNumber().intValue();
    }

    public List<SemanticEntityVersion> calculateLatestSemanticVersions(Entity<? extends  EntityVersion> entity,
                                                                       StampCalculator stampCalculator,
                                                                       int patternNid) {
        List<SemanticEntityVersion> semanticEntityVersions = new ArrayList<>();
        PrimitiveData.get().forEachSemanticNidForComponentOfPattern(entity.nid(), patternNid, semanticNid ->{
            Latest<SemanticEntityVersion> semanticEntityVersionLatest = stampCalculator.latest(semanticNid);
            if (semanticEntityVersionLatest.isPresent()) {
                semanticEntityVersions.add(semanticEntityVersionLatest.get());
            }
        });
        return semanticEntityVersions;
    }
}
