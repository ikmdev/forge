package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import dev.ikm.tinkar.forge.wrapper.MethodWrapper;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ForgeMethodWrapper implements MethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(ForgeMethodWrapper.class);

    protected  <T> T convertGenericObjectModel(GenericObjectModel genericObjectModel, Class<T> clazz) {
        return (T) genericObjectModel.getAdaptedObject(clazz);
    }

    protected int convertSimpleNumber(SimpleNumber simpleNumber) {
        return simpleNumber.getAsNumber().intValue();
    }

    public List<SemanticEntityVersion> calculateLatestSemanticVersions(SimpleNumber simpleNumber,
                                                                       StampCalculator stampCalculator,
                                                                       int patternNid) {
        List<SemanticEntityVersion> semanticEntityVersions = new ArrayList<>();
        int componentNid = convertSimpleNumber(simpleNumber);
        PrimitiveData.get().forEachSemanticNidForComponentOfPattern(componentNid, patternNid, semanticNid ->{
            Latest<SemanticEntityVersion> semanticEntityVersionLatest = stampCalculator.latest(semanticNid);
            if (semanticEntityVersionLatest.isPresent()) {
                semanticEntityVersions.add(semanticEntityVersionLatest.get());
            }
        });
        return semanticEntityVersions;
    }
}
