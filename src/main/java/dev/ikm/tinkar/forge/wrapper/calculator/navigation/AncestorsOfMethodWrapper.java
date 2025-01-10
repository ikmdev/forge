package dev.ikm.tinkar.forge.wrapper.calculator.navigation;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AncestorsOfMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(AncestorsOfMethodWrapper.class);

    private final NavigationCalculator navigationCalculator;

    public AncestorsOfMethodWrapper(NavigationCalculator navigationCalculator) {
        this.navigationCalculator = navigationCalculator;
    }

    @Override
    public ImmutableSet<Integer> exec(List list) {
        int nid = extractNidFromParameters(list);
        return navigationCalculator.ancestorsOf(nid).map(value -> value);
    }
}