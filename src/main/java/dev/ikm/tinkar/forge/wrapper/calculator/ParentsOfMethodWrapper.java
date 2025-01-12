package dev.ikm.tinkar.forge.wrapper.calculator;

import dev.ikm.tinkar.forge.wrapper.AbstractMethodWrapper;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParentsOfMethodWrapper extends AbstractMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(ParentsOfMethodWrapper.class);

    @Override
    public ImmutableList<Integer> exec(List list) {
        int nid = extractNid(list);
        return extractNavigationCalculator(list).parentsOf(nid).map(value -> value);
    }
}
