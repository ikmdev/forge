package dev.ikm.tinkar.forge.wrapper.calculator.navigation;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DescendentsOfMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescendentsOfMethodWrapper.class);

    private final NavigationCalculator navigationCalculator;

    public DescendentsOfMethodWrapper(NavigationCalculator navigationCalculator) {
        this.navigationCalculator = navigationCalculator;
    }

    @Override
    public ImmutableSet<Integer> exec(List list) {
        int nid = extractNidFromParameters(list);
        return navigationCalculator.descendentsOf(nid).map(value -> value);
    }
}
