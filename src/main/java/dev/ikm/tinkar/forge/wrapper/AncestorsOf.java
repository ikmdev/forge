package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AncestorsOf extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(AncestorsOf.class);

    @Override
    public ImmutableSet<Integer> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.get(0);
        GenericObjectModel genericObjectModel = (GenericObjectModel) list.get(1);
        int nid = convertSimpleNumber(simpleNumber);
        NavigationCalculator navigationCalculator = convertGenericObjectModel(genericObjectModel, NavigationCalculator.class);
        return navigationCalculator.ancestorsOf(nid).map(value -> value);
    }
}