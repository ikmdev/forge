package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.terms.EntityProxy;
import dev.ikm.tinkar.terms.TinkarTerm;
import freemarker.template.TemplateMethodModelEx;

import java.io.Writer;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Forge {

    Forge config(Path templatesDirectory);

    Forge config(ForgeConfig forgeConfig);

    Forge data(String name, Stream<Entity<? extends EntityVersion>> entities);

    Forge function(String name, TemplateMethodModelEx methodWrapper);

    Forge variable(String name, EntityProxy.Concept concept);

    Forge variable(String name, EntityProxy.Pattern pattern);

    Forge variable(String name, EntityProxy.Semantic semantic);

    Forge template(String templateName, Writer writer);

    Forge execute();
}
