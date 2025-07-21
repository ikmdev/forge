package dev.ikm.tinkar.forge.wrapper.of;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.ForgeMethodWrapper;
import freemarker.ext.beans.GenericObjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TextOf extends ForgeMethodWrapper {

    private final Logger LOG = LoggerFactory.getLogger(TextOf.class);

    @Override
    public String exec(List arguments) {
        GenericObjectModel entityObjectModel = (GenericObjectModel) arguments.get(0);
        GenericObjectModel languageCalcObjectModel = (GenericObjectModel) arguments.get(1);
        Entity<? extends EntityVersion> entity = convertGenericObjectModel(entityObjectModel, Entity.class);
        LanguageCalculator languageCalculator = convertGenericObjectModel(languageCalcObjectModel, LanguageCalculator.class);
        return createDescription(entity, languageCalculator);
    }

    public String createDescription(Entity<? extends EntityVersion> entity, LanguageCalculator languageCalculator) {
        String module = formatName(entity.versions().getAny().stamp().module().description());
        String description = formatName(languageCalculator.getDescriptionTextOrNid(entity.nid()));
        if (description.startsWith(module)) {
            return description;
        }
        return module + "_" + description;
    }

    private String formatName(String name) {
        return name.toUpperCase().replace("MODULE", "").replaceAll("[^A-Z0-9]", " ").trim().replaceAll("\\s+", "_");
    }

}
