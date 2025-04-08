package dev.ikm.tinkar.forge.test;

import dev.ikm.tinkar.common.service.CachingService;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.coordinate.Coordinates;
import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculatorWithCache;
import dev.ikm.tinkar.coordinate.navigation.NavigationCoordinateRecord;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculatorWithCache;
import dev.ikm.tinkar.coordinate.stamp.StampCoordinateRecord;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.entity.load.LoadEntitiesFromProtobufFile;
import dev.ikm.tinkar.forge.Forge;
import dev.ikm.tinkar.forge.TinkarForge;
import org.eclipse.collections.api.factory.Lists;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForgeIT {

    private final Logger LOG = LoggerFactory.getLogger(ForgeIT.class);

    public static final Function<String, File> createFilePathInTarget = (pathName) -> new File("%s/target/%s".formatted(System.getProperty("user.dir"), pathName));
    public static final File PB_STARTER_DATA = createFilePathInTarget.apply("data/tinkar-example-data-1.1.0+1.1.1-reasoned-pb.zip");
    public static final Path TEMPLATES_DIRECTORY = new File(ForgeIT.class.getClassLoader().getResource("templates").getFile()).toPath();

    private StampCalculator STAMP_CALCULATOR;
    private LanguageCalculator LANGUAGE_CALCULATOR;
    private NavigationCalculator NAVIGATION_CALCULATOR;

    private final List<ConceptEntity<? extends ConceptEntityVersion>> concepts = new ArrayList<>();
    private final List<SemanticEntity<? extends SemanticEntityVersion>> semantics = new ArrayList<>();
    private final List<PatternEntity<? extends PatternEntityVersion>> patterns = new ArrayList<>();
    private final List<StampEntity<? extends StampEntityVersion>> stamps = new ArrayList<>();

    private final AtomicInteger conceptCount = new AtomicInteger(0);
    private final AtomicInteger semanticCount = new AtomicInteger(0);
    private final AtomicInteger patternCount = new AtomicInteger(0);
    private final AtomicInteger stampCount = new AtomicInteger(0);

    @BeforeAll
    public void beforeAll() throws IOException {
        Files.createDirectories(createFilePathInTarget.apply("/test").toPath());

        CachingService.clearAll();
        PrimitiveData.selectControllerByName("Load Ephemeral Store");
        PrimitiveData.start();
        LoadEntitiesFromProtobufFile loadEntitiesFromProtobufFile = new LoadEntitiesFromProtobufFile(PB_STARTER_DATA);
        loadEntitiesFromProtobufFile.compute();

        var languageList = Lists.mutable.of(Coordinates.Language.UsEnglishFullyQualifiedName()).toImmutableList();
        StampCoordinateRecord stampCoordinateRecord = Coordinates.Stamp.DevelopmentLatest();
        NavigationCoordinateRecord navigationCoordinateRecord = Coordinates.Navigation.stated().toNavigationCoordinateRecord();

        STAMP_CALCULATOR = stampCoordinateRecord.stampCalculator();
        LANGUAGE_CALCULATOR = LanguageCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList);
        NAVIGATION_CALCULATOR = NavigationCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList, navigationCoordinateRecord);

        PrimitiveData.get().forEachConceptNid(conceptNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(conceptNid);
            concepts.add((ConceptEntity<? extends ConceptEntityVersion>) entity);
            conceptCount.incrementAndGet();
        });

        PrimitiveData.get().forEachSemanticNid(semanticNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(semanticNid);
            semantics.add((SemanticEntity<? extends SemanticEntityVersion>) entity);
            semanticCount.incrementAndGet();
        });

        PrimitiveData.get().forEachPatternNid(patternNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(patternNid);
            patterns.add((PatternEntity<? extends PatternEntityVersion>) entity);
            patternCount.incrementAndGet();
        });

        PrimitiveData.get().forEachStampNid(stampNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(stampNid);
            stamps.add((StampEntity<? extends StampEntityVersion>) entity);
            stampCount.incrementAndGet();
        });
    }

    private Consumer<Integer> progressUpdate(String componentType) {
        return index -> {
            if (index % 100_000 == 0) {
                LOG.info("{}% {}s completed", ((double) index / semanticCount.get()) * 100, componentType);
            } else if (index == semanticCount.get()) {
                LOG.info("{}s completed", componentType);
            }
        };
    }

    @AfterAll
    public void afterAll() {
        PrimitiveData.stop();
    }

    @Order(1)
    @Test
    public void givenConcepts_whenConceptsFTLApplied_thenConceptsTxtFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/concepts_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .conceptData(concepts.stream(), progressUpdate("concept"))
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultNavigationCalc", NAVIGATION_CALCULATOR)
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .template("concepts_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    @Order(2)
    @Test
    public void givenSemantics_whenSemanticsFTLApplied_thenSemanticsTxtFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/semantics_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .semanticData(semantics.stream(), progressUpdate("semantic"))
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .template("semantics_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    @Order(3)
    @Test
    public void givenPatterns_whenPatternsFTLApplied_thenPatternsTxtFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/patterns_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .patternData(patterns.stream(), progressUpdate("pattern"))
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .template("patterns_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    @Order(4)
    @Test
    public void givenSTAMPS_whenSTAMPSFTLApplied_thenSTAMPSTXTFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/stamps_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .stampData(stamps.stream(), progressUpdate("stamp"))
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .template("stamps_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    @Order(5)
    @Test
    public void givenAllComponents_whenAllComponentsFTLApplied_thenAllComponentsTXTFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/all_components_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .conceptData(concepts.stream(), progressUpdate("concept"))
                    .semanticData(semantics.stream(), progressUpdate("semantic"))
                    .patternData(patterns.stream(), progressUpdate("pattern"))
                    .stampData(stamps.stream(), progressUpdate("stamp"))
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultNavigationCalc", NAVIGATION_CALCULATOR)
                    .template("all_components_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    @Order(6)
    @Test
    public void givenStarterData_whenJavaBindingFTLApplied_thenStarterDataJAVAFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/ForgeTestTerm.java"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .conceptData(concepts.stream(), progressUpdate("concept"))
                    .patternData(patterns.stream(), progressUpdate("pattern"))
                    .variable("package", "dev.ikm.tinkar.forge.test")
                    .variable("author", "Forge Test Author")
                    .variable("className", "ForgeTestTerm")
                    .variable("namespace", UUID.randomUUID().toString())
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .template("java_bindings_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }


    @Order(7)
    @Test
    public void givenConcepts_whenDescriptionsFTLApplied_thenDescriptionsTxtFileGenerated() {
        long startTime = System.nanoTime();

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/descriptions_it_output.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .conceptData(concepts.stream(), progressUpdate("concept"))
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultNavigationCalc", NAVIGATION_CALCULATOR)
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .template("descriptions_it.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logElapsedTime(startTime, System.nanoTime());
    }

    private void logElapsedTime(long startTime, long endTime) {
        double elapsedTime = (double) (endTime - startTime) / 1000_000_000;
        LOG.info("Elapsed Time (s): {}", elapsedTime);
    }

}
