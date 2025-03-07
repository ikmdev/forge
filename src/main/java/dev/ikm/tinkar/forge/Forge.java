package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.terms.EntityProxy;

import java.io.Writer;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Forge {

    Forge config(Path templatesDirectory);

    Forge config(ForgeConfig forgeConfig);

    Forge data(String name, Stream<Entity<? extends EntityVersion>> entities);

    Forge function(String name, ForgeMethodWrapper forgeMethodWrapper);

    Forge variable(String name, EntityProxy.Concept concept);

    Forge variable(String name, EntityProxy.Pattern pattern);

    Forge variable(String name, EntityProxy.Semantic semantic);

    Forge variable(String name, StampCalculator stampCalculator);

    Forge variable(String name, LanguageCalculator languageCalculator);

    Forge variable(String name, NavigationCalculator navigationCalculator);

    Forge template(String templateName, Writer writer);

    Forge execute();
}
