package dev.ikm.tinkar.forge.wrapper.calculator.navigation;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ChildrenOfNavigationMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(ChildrenOfNavigationMethodWrapper.class);

    private final NavigationCalculator navigationCalculator;

    public ChildrenOfNavigationMethodWrapper(NavigationCalculator navigationCalculator) {
        this.navigationCalculator = navigationCalculator;
    }

    @Override
    public ImmutableList<Integer> exec(List list) {
        int nid = extractNidFromParameters(list);
        return navigationCalculator.childrenOf(nid).map(value -> value);
    }
}
