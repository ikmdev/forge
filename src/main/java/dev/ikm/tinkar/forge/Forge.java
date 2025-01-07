package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;

import java.io.Writer;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Forge {

    Forge config(Path templatesDirectory);

    Forge config(ForgeConfig forgeConfig);

    Forge data(Stream<Entity<? extends EntityVersion>> entities);

    Forge template(String templateName, Writer writer);

    Forge execute();
}
