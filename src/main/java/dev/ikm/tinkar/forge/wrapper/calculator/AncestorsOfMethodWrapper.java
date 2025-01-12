package dev.ikm.tinkar.forge.wrapper.calculator;

import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AncestorsOfMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(AncestorsOfMethodWrapper.class);

    @Override
    public ImmutableSet<Integer> exec(List list) {
        int nid = extractNid(list);
        return extractNavigationCalculator(list).ancestorsOf(nid).map(value -> value);
    }
}