package dev.ikm.tinkar.forge;

import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.ConceptEntity;
import dev.ikm.tinkar.entity.ConceptEntityVersion;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.PatternEntity;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import dev.ikm.tinkar.entity.SemanticEntity;
import dev.ikm.tinkar.entity.SemanticEntityVersion;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.entity.StampEntityVersion;
import dev.ikm.tinkar.forge.wrapper.get.EntityGet;
import dev.ikm.tinkar.forge.wrapper.of.AncestorsOf;
import dev.ikm.tinkar.forge.wrapper.of.ChildrenOf;
import dev.ikm.tinkar.forge.wrapper.of.DescendentsOf;
import dev.ikm.tinkar.forge.wrapper.of.LatestVersionOf;
import dev.ikm.tinkar.forge.wrapper.of.MemberOf;
import dev.ikm.tinkar.forge.wrapper.of.ParentsOf;
import dev.ikm.tinkar.forge.wrapper.of.TextOf;
import dev.ikm.tinkar.forge.wrapper.on.DescriptionsOn;
import dev.ikm.tinkar.terms.EntityProxy;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
		this.configuration.setSharedVariable("entityGet", new EntityGet());
		this.configuration.setSharedVariable("ancestorsOf", new AncestorsOf());
		this.configuration.setSharedVariable("childrenOf", new ChildrenOf());
		this.configuration.setSharedVariable("descendentsOf", new DescendentsOf());
		this.configuration.setSharedVariable("latestVersionOf", new LatestVersionOf());
		this.configuration.setSharedVariable("memberOf", new MemberOf());
		this.configuration.setSharedVariable("parentsOf", new ParentsOf());
		this.configuration.setSharedVariable("textOf", new TextOf());
		this.configuration.setSharedVariable("descriptionsOn", new DescriptionsOn());
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
            LOG.error("String variable wasn't set correctly!", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Forge variable(String name, Object value) {
        try {
            configuration.setSharedVariable(name, value);
        } catch (TemplateModelException e) {
            LOG.error("Object variable wasn't set correctly!", e);
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
