package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.terms.EntityProxy;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TinkarForge implements Forge {

    private final Logger LOG = LoggerFactory.getLogger(TinkarForge.class);

    private final Configuration configuration;
    private Template template;
    private Writer output;
    private final Map<String, Object> dataModel;
    private final Version configurationVersion = Configuration.VERSION_2_3_34;

    public TinkarForge() {
        this.dataModel = new HashMap<>();
        this.configuration = new Configuration(configurationVersion);
        loadInternalMethodWrappers();
    }

    /**
     *
     */
    private void loadInternalMethodWrappers() {
        ServiceLoader.load(ForgeMethodWrapper.class)
                .stream()
                .forEach(abstractMethodWrapperProvider -> {
                        ForgeMethodWrapper forgeMethodWrapper = abstractMethodWrapperProvider.get();
                        configuration.setSharedVariable(forgeMethodWrapper.methodName(), abstractMethodWrapperProvider.get());
                });
    }

    @Override
    public Forge config(Path templatesDirectory) {
        return config(config -> {
            config.setDirectoryForTemplateLoading(templatesDirectory.toFile());
            config.setDefaultEncoding("UTF-8");
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            config.setWrapUncheckedExceptions(true);
            config.setFallbackOnNullLoopVariable(false);
            config.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
            config.setAPIBuiltinEnabled(true);
        });
    }

    @Override
    public Forge config(ForgeConfig forgeConfig) {
        try {
            forgeConfig.accept(configuration);
            DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(configurationVersion);
            owb.setIterableSupport(true);
            configuration.setObjectWrapper(owb.build());
        } catch (TemplateModelException | IOException e) {
            LOG.error("Issue with configuration of Forge!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge conceptData(Stream<ConceptEntity<? extends ConceptEntityVersion>> conceptEntityStream, Consumer<Integer> progressUpdate) {
        ForgeIterator forgeIterator = new ForgeIterator(conceptEntityStream, progressUpdate);
        dataModel.put("concepts", forgeIterator);
        return this;
    }

    @Override
    public Forge conceptData(List<ConceptEntity<? extends ConceptEntityVersion>> conceptEntities, Consumer<Integer> progressUpdate) {
        return conceptData(conceptEntities.stream(), progressUpdate);
    }

    @Override
    public Forge semanticData(Stream<SemanticEntity<? extends SemanticEntityVersion>> semanticEntityStream, Consumer<Integer> progressUpdate) {
        ForgeIterator<SemanticEntity<? extends SemanticEntityVersion>> forgeIterator = new ForgeIterator(semanticEntityStream, progressUpdate);
        dataModel.put("semantics", forgeIterator);
        return this;
    }

    @Override
    public Forge semanticData(List<SemanticEntity<? extends SemanticEntityVersion>> semanticEntities, Consumer<Integer> progressUpdate) {
        return semanticData(semanticEntities.stream(), progressUpdate);
    }

    @Override
    public Forge patternData(Stream<PatternEntity<? extends PatternEntityVersion>> patternEntityStream, Consumer<Integer> progressUpdate) {
        ForgeIterator<PatternEntity<? extends PatternEntityVersion>> forgeIterator = new ForgeIterator(patternEntityStream, progressUpdate);
        dataModel.put("patterns", forgeIterator);
        return this;
    }

    @Override
    public Forge patternData(List<PatternEntity<? extends PatternEntityVersion>> patternEntities, Consumer<Integer> progressUpdate) {
        return patternData(patternEntities.stream(), progressUpdate);
    }

    @Override
    public Forge stampData(Stream<StampEntity<? extends StampEntityVersion>> stampEntityStream, Consumer<Integer> progressUpdate) {
        ForgeIterator<StampEntity<? extends StampEntityVersion>> forgeIterator = new ForgeIterator(stampEntityStream, progressUpdate);
        dataModel.put("stamps", forgeIterator);
        return this;
    }

    @Override
    public Forge stampData(List<StampEntity<? extends StampEntityVersion>> stampEntities, Consumer<Integer> progressUpdate) {
        return stampData(stampEntities.stream(), progressUpdate);
    }

    @Override
    public Forge entityData(String name, Stream<Entity<? extends EntityVersion>> entities, Consumer<Integer> progressUpdate) {
        ForgeIterator<Entity<? extends EntityVersion>> forgeIterator = new ForgeIterator(entities, progressUpdate);
        dataModel.put(name, forgeIterator);
        return this;
    }

    @Override
    public Forge entityData(String name, List<Entity<? extends EntityVersion>> entities, Consumer<Integer> progressUpdate) {
        return entityData(name, entities.stream(), progressUpdate);
    }

    @Override
    public Forge function(String name, ForgeMethodWrapper forgeMethodWrapper) {
        configuration.setSharedVariable(name, forgeMethodWrapper);
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
            LOG.error("StampCalculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, LanguageCalculator languageCalculator) {
        try {
            configuration.setSharedVariable(name, languageCalculator);
        } catch (TemplateModelException e) {
            LOG.error("LanguageCalculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, NavigationCalculator navigationCalculator) {
        try {
            configuration.setSharedVariable(name, navigationCalculator);
        } catch (TemplateModelException e) {
            LOG.error("NavigationCalculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, String value) {
        try {
            configuration.setSharedVariable(name, value);
        } catch (TemplateModelException e) {
            LOG.error("NavigationCalculator variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge execute() {
        try {
            template.process(dataModel, output);
        } catch (TemplateException | IOException e) {
            LOG.error("Error with executing Forge instance!", e);
            throw new RuntimeException(e);
        }
        return this;
    }
}
