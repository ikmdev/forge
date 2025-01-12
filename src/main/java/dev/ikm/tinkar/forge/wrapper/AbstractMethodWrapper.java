package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import dev.ikm.tinkar.terms.EntityProxy;
import dev.ikm.tinkar.terms.EntityProxy.Pattern;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMethodWrapper implements TemplateMethodModelEx {

    private final Logger LOG = LoggerFactory.getLogger(AbstractMethodWrapper.class);

    public StampCalculator extractSTAMPCalculator(List params) {
        if (!(params.get(1) instanceof GenericObjectModel)) {
            throw new RuntimeException("Invalid parameters: expected a single non-null GenericObjectModel.");
        }
        GenericObjectModel genericObjectModel = ((GenericObjectModel) params.get(1));
        return (StampCalculator) genericObjectModel.getAdaptedObject(StampCalculator.class);
    }

    public NavigationCalculator extractNavigationCalculator(List params) {
        if (!(params.get(1) instanceof GenericObjectModel)) {
            throw new AssertionError("Parameter at index 1 is not an instance of GenericObjectModel");
        }
        GenericObjectModel genericObjectModel = ((GenericObjectModel) params.get(1));
        return (NavigationCalculator) genericObjectModel.getAdaptedObject(NavigationCalculator.class);
    }

    public LanguageCalculator extractLanguageCalculator(List params) {
        if (!(params.get(1) instanceof GenericObjectModel)) {
            throw new AssertionError("Parameter at index 1 is not an instance of GenericObjectModel");
        }
        GenericObjectModel genericObjectModel = ((GenericObjectModel) params.get(1));
        return (LanguageCalculator) genericObjectModel.getAdaptedObject(LanguageCalculator.class);
    }

    public int extractNid(List params) {
        if (!(params.get(0) instanceof SimpleNumber)) {
            throw new AssertionError("Expected a SimpleNumber at index 0");
        }
        return ((SimpleNumber) params.getFirst()).getAsNumber().intValue();
    }

    public List<SemanticEntityVersion> calculateLatestSemanticVersion(List params, Pattern pattern) {
        if (!(params.getFirst() instanceof SimpleNumber) || !(params.get(1) instanceof GenericObjectModel)) {
            throw new RuntimeException("Invalid parameters: The list must contain exactly two elements - " +
                    "the first being a non-null SimpleNumber and the second a GenericObjectModel.");
        }
        List<SemanticEntityVersion> semanticEntityVersions = new ArrayList<>();
        StampCalculator stampCalculator = extractSTAMPCalculator(params);
        int componentNid = extractNid(params);
        PrimitiveData.get().forEachSemanticNidForComponentOfPattern(componentNid, pattern.nid(), semanticNid ->{
            Latest<SemanticEntityVersion> semanticEntityVersionLatest = stampCalculator.latest(componentNid);
            if (!semanticEntityVersions.isEmpty()) {
                semanticEntityVersions.add(semanticEntityVersionLatest.get());
            }
        });
        return semanticEntityVersions;
    }
}
