package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.terms.EntityProxy;

import java.io.Writer;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Forge {

    Forge config(Path templatesDirectory);

    Forge config(ForgeConfig forgeConfig);

    //Concept Data Loading
    Forge conceptData(Stream<ConceptEntity<? extends ConceptEntityVersion>> conceptEntityStream, Consumer<Integer> progressUpdate);

    //Semantic Data loading
    Forge semanticData(Stream<SemanticEntity<? extends SemanticEntityVersion>> semanticEntityStream, Consumer<Integer> progressUpdate);

    //Pattern Data loading
    Forge patternData(Stream<PatternEntity<? extends PatternEntityVersion>> patternEntityStream, Consumer<Integer> progressUpdate);

    //STAMP Data Loading
    Forge stampData(Stream<StampEntity<? extends StampEntityVersion>> stampEntityStream, Consumer<Integer> progressUpdate);

    //Entity Data Loading
    Forge entityData(String name, Stream<Entity<? extends EntityVersion>> entities, Consumer<Integer> progressUpdate);

    //Variables
    Forge variable(String name, EntityProxy.Concept concept);

    Forge variable(String name, EntityProxy.Pattern pattern);

    Forge variable(String name, EntityProxy.Semantic semantic);

    Forge variable(String name, StampCalculator stampCalculator);

    Forge variable(String name, LanguageCalculator languageCalculator);

    Forge variable(String name, NavigationCalculator navigationCalculator);

    //Functions
    Forge function(String name, ForgeMethodWrapper forgeMethodWrapper);

    //Templates
    Forge template(String templateName, Writer writer);

    Forge execute();
}
