package dev.ikm.tinkar.forge.wrapper;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ChildOfNavigationMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(ChildOfNavigationMethodWrapper.class);

    private final NavigationCalculator navigationCalculator;

    public ChildOfNavigationMethodWrapper(NavigationCalculator navigationCalculator) {
        this.navigationCalculator = navigationCalculator;
    }

    @Override
    public ImmutableList<Integer> exec(List list) {
        int nid = extractNidFromParameters(list);
        return navigationCalculator.childrenOf(nid).map(value -> value);
    }
}
