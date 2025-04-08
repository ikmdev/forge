package dev.ikm.tinkar.forge.wrapper.of;

import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.entity.ConceptEntity;
import dev.ikm.tinkar.entity.ConceptEntityVersion;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.ext.beans.GenericObjectModel;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DescendentsOf extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(DescendentsOf.class);

    @Override
    public ImmutableSet<ConceptEntity<? extends ConceptEntityVersion>> exec(List list) {
        GenericObjectModel conceptObjectModel = (GenericObjectModel) list.get(0);
        GenericObjectModel navigationCalcObjectModel = (GenericObjectModel) list.get(1);
        ConceptEntity<ConceptEntityVersion> conceptEntity = convertGenericObjectModel(conceptObjectModel, ConceptEntity.class);
        NavigationCalculator navigationCalculator = convertGenericObjectModel(navigationCalcObjectModel, NavigationCalculator.class);
        return navigationCalculator.descendentsOf(conceptEntity.nid()).map(value -> {
            Optional<ConceptEntity<ConceptEntityVersion>> optionalConcept = Entity.get(value);
            if (optionalConcept.isPresent()) {
                return optionalConcept.get();
            } else {
                throw new RuntimeException("Could not find concept " + value);
            }
        });
    }
}
