package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.terms.EntityProxy;

import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Forge {

    Forge config(Path templatesDirectory);

    Forge config(ForgeConfig forgeConfig);

    //Concept Data Loading
    Forge conceptData(Stream<ConceptEntity<? extends ConceptEntityVersion>> conceptEntities, Consumer<Integer> progressUpdate);

    Forge conceptData(List<ConceptEntity<? extends ConceptEntityVersion>> conceptEntities, Consumer<Integer> progressUpdate);
    //Semantic Data loading
    Forge semanticData(Stream<SemanticEntity<? extends SemanticEntityVersion>> semanticEntities, Consumer<Integer> progressUpdate);

    Forge semanticData(List<SemanticEntity<? extends SemanticEntityVersion>> semanticEntities, Consumer<Integer> progressUpdate);
    //Pattern Data loading
    Forge patternData(Stream<PatternEntity<? extends PatternEntityVersion>> patternEntities, Consumer<Integer> progressUpdate);

    Forge patternData(List<PatternEntity<? extends PatternEntityVersion>> patternEntities, Consumer<Integer> progressUpdate);
    //STAMP Data Loading
    Forge stampData(Stream<StampEntity<? extends StampEntityVersion>> stampEntities, Consumer<Integer> progressUpdate);

    Forge stampData(List<StampEntity<? extends StampEntityVersion>> stampEntities, Consumer<Integer> progressUpdate);
    //Entity Data Loading
    Forge entityData(String name, Stream<Entity<? extends EntityVersion>> entities, Consumer<Integer> progressUpdate);

    Forge entityData(String name, List<Entity<? extends EntityVersion>> entities, Consumer<Integer> progressUpdate);

    //Variables
    Forge variable(String name, EntityProxy.Concept concept);

    Forge variable(String name, EntityProxy.Pattern pattern);

    Forge variable(String name, EntityProxy.Semantic semantic);

    Forge variable(String name, StampCalculator stampCalculator);

    Forge variable(String name, LanguageCalculator languageCalculator);

    Forge variable(String name, NavigationCalculator navigationCalculator);

    Forge variable(String name, String value);

    Forge variable(String name, Object value);

    //Functions
    Forge function(String name, ForgeMethodWrapper forgeMethodWrapper);

    //Templates
    Forge template(String templateName, Writer writer);

    Forge execute();
}
