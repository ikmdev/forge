package dev.ikm.tinkar.forge;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

import java.io.IOException;

@FunctionalInterface
public interface ForgeConfig {
    void accept(Configuration configuration) throws TemplateModelException, IOException;
}
