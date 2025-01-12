package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.forge.wrapper.lookup.*;
import dev.ikm.tinkar.forge.wrapper.calculator.TextMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.AncestorsOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.ChildrenOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.DescendentsOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.calculator.ParentsOfMethodWrapper;
import dev.ikm.tinkar.forge.wrapper.semantic.DescriptionsForMethodWrapper;
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

    private final Configuration configuration;
    private Template template;
    private Writer output;
    private final Map<String, Object> dataModel;

    public TinkarForge(StampCalculator stampCalculator, LanguageCalculator languageCalculator, NavigationCalculator navigationCalculator) {
        this.dataModel = new HashMap<>();
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);
        setInternalMethodWrappers();
        setInternalCalculatorsVariables(stampCalculator, languageCalculator, navigationCalculator);
    }

    private void setInternalMethodWrappers() {
        configuration.setSharedVariable("textOf", new TextMethodWrapper());
        configuration.setSharedVariable("parentsOf", new ParentsOfMethodWrapper());
        configuration.setSharedVariable("childrenOf", new ChildrenOfMethodWrapper());
        configuration.setSharedVariable("ancestorsOf", new AncestorsOfMethodWrapper());
        configuration.setSharedVariable("descendantsOf", new DescendentsOfMethodWrapper());
        configuration.setSharedVariable("getEntity", new GetEntityFastMethodWrapper());
        configuration.setSharedVariable("getConcept", new GetConceptFastMethodWrapper());
        configuration.setSharedVariable("getSemantic", new GetSemanticFastMethodWrapper());
        configuration.setSharedVariable("getPattern", new GetPatternFastMethodWrapper());
        configuration.setSharedVariable("getSTAMP", new GetSTAMPFastMethodWrapper());
        configuration.setSharedVariable("descriptionsFor", new DescriptionsForMethodWrapper());
    }

    private void setInternalCalculatorsVariables(StampCalculator stampCalculator,
                                                 LanguageCalculator languageCalculator,
                                                 NavigationCalculator navigationCalculator) {
        try {
            configuration.setSharedVariable("defaultSTAMPCalc", stampCalculator);
            configuration.setSharedVariable("defaultLanguageCalc", languageCalculator);
            configuration.setSharedVariable("defaultNavigationCalc", navigationCalculator);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
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
    public Forge variable(String name, StampCalculator stampCalculator) {
        try {
            configuration.setSharedVariable(name, stampCalculator);
        } catch (TemplateModelException e) {
            LOG.error("STAMP Calculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, LanguageCalculator languageCalculator) {
        try {
            configuration.setSharedVariable(name, languageCalculator);
        } catch (TemplateModelException e) {
            LOG.error("STAMP Calculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, NavigationCalculator navigationCalculator) {
        try {
            configuration.setSharedVariable(name, navigationCalculator);
        } catch (TemplateModelException e) {
            LOG.error("STAMP Calculator variable wasn't set correctly!", e);
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
