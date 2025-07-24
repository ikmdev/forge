package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;

public final class ForgeUtil {

    private ForgeUtil() {
    }

    public static String createBindingDescription(Entity<? extends EntityVersion> entity, LanguageCalculator languageCalculator) {
        String module = formatName(entity.versions().getLast().stamp().module().description());
        String description = formatName(languageCalculator.getDescriptionTextOrNid(entity.nid()));
        if (description.startsWith(module)) {
            return description;
        }
        return module + "_" + description;
    }

    private static String formatName(String name) {
        return name.toUpperCase().replace("MODULE", "").replaceAll("[^A-Z0-9]", " ").trim().replaceAll("\\s+", "_");
    }

}
