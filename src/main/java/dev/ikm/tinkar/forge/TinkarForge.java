package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.wrapper.lookup.chronology.*;
import dev.ikm.tinkar.forge.wrapper.calculator.navigation.AncestorsOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.navigation.ChildrenOfNavigationMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.language.DescriptionTextMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.navigation.DescendentsOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.navigation.ParentsOfNavigationMethodWrapper;
import dev.ikm.tinkar.terms.EntityProxy;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

public class TinkarForge implements Forge {

    private final Logger LOG = LoggerFactory.getLogger(TinkarForge.class);

    private final StampCalculator stampCalculator;
    private final LanguageCalculator languageCalculator;
    private final NavigationCalculator navigationCalculator;
    private final Configuration configuration;
    private Template template;
    private Writer output;
    private final Map<String, Object> dataModel;

    public TinkarForge(StampCalculator stampCalculator, LanguageCalculator languageCalculator, NavigationCalculator navigationCalculator) {
        this.stampCalculator = stampCalculator;
        this.languageCalculator = languageCalculator;
        this.navigationCalculator = navigationCalculator;
        this.dataModel = new HashMap<>();
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);
    }

    @Override
    public Forge config(Path templatesDirectory) {
        return config(config -> {
            DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_34);
            owb.setIterableSupport(true);
            config.setObjectWrapper(owb.build());
            config.setDirectoryForTemplateLoading(templatesDirectory.toFile());
            config.setDefaultEncoding("UTF-8");
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            config.setWrapUncheckedExceptions(true);
            config.setFallbackOnNullLoopVariable(false);
            config.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
            config.setSharedVariable("description", new DescriptionTextMethodWrapper(languageCalculator));
            config.setSharedVariable("parentsOf", new ParentsOfNavigationMethodWrapper(navigationCalculator));
            config.setSharedVariable("childrenOf", new ChildrenOfNavigationMethodWrapper(navigationCalculator));
            config.setSharedVariable("ancestorsOf", new AncestorsOfMethodWrapper(navigationCalculator));
            config.setSharedVariable("descendantsOf", new DescendentsOfMethodWrapper(navigationCalculator));
            config.setSharedVariable("entityGetFast", new EntityGetFastMethodWrapper());
            config.setSharedVariable("conceptGetFast", new ConceptGetFastMethodWrapper());
            config.setSharedVariable("semanticGetFast", new SemanticGetFastMethodWrapper());
            config.setSharedVariable("patternGetFast", new PatternGetFastMethodWrapper());
            config.setSharedVariable("stampGetFast", new STAMPGetFastMethodWrapper());
        });
    }

    @Override
    public Forge config(ForgeConfig forgeConfig) {
        try {
            forgeConfig.accept(configuration);
        } catch (TemplateModelException | IOException e) {
            LOG.error("Issue with configuration of Forge!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge data(String name, Stream<Entity<? extends EntityVersion>> entities) {
        dataModel.put(name, entities.iterator());
        return this;
    }

    @Override
    public Forge function(String name, TemplateMethodModelEx methodWrapper) {
        configuration.setSharedVariable(name, methodWrapper);
        return this;
    }

    @Override
    public Forge template(String templateName, Writer writer) {
        this.output = writer;
        try {
            this.template = configuration.getTemplate(templateName);
        } catch (IOException e) {
            LOG.error("Template wasn't formatted correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, EntityProxy.Concept concept) {
        try {
            configuration.setSharedVariable(name, concept);
        } catch (TemplateModelException e) {
            LOG.error("Concept variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, EntityProxy.Pattern pattern) {
        try {
            configuration.setSharedVariable(name, pattern);
        } catch (TemplateModelException e) {
            LOG.error("Pattern variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, EntityProxy.Semantic semantic) {
        try {
            configuration.setSharedVariable(name, semantic);
        } catch (TemplateModelException e) {
            LOG.error("Semantic variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge execute() {
        try {
            template.process(dataModel, output);
        } catch (TemplateException | IOException e) {
            LOG.error("Error with Execution of Forge", e);
            throw new RuntimeException(e);
        }
        return this;
    }
}
