package dev.ikm.tinkar.forge.wrapper.of;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.ext.beans.GenericObjectModel;
import freemarker.template.SimpleNumber;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DescendentsOf extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescendentsOf.class);

    @Override
    public ImmutableSet<Integer> exec(List list) {
        SimpleNumber simpleNumber = (SimpleNumber) list.get(0);
        GenericObjectModel genericObjectModel = (GenericObjectModel) list.get(1);
        int nid = convertSimpleNumber(simpleNumber);
        NavigationCalculator navigationCalculator = convertGenericObjectModel(genericObjectModel, NavigationCalculator.class);
        return navigationCalculator.descendentsOf(nid).map(value -> value);
    }
}
