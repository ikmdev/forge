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
import dev.ikm.tinkar.coordinate.stamp.StateSet;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.*;
import dev.ikm.tinkar.entity.load.LoadEntitiesFromProtobufFile;
import dev.ikm.tinkar.forge.Forge;
import dev.ikm.tinkar.forge.TinkarForge;
import dev.ikm.tinkar.terms.ConceptFacade;
import dev.ikm.tinkar.terms.TinkarTerm;
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
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForgeIT {

    private final Logger LOG = LoggerFactory.getLogger(ForgeIT.class);

    public static final Function<String, File> createFilePathInTarget = (pathName) -> new File("%s/target/%s".formatted(System.getProperty("user.dir"), pathName));
    public static final File PB_STARTER_DATA = createFilePathInTarget.apply("data/tinkar-example-data-1.1.0+1.1.1-reasoned-pb.zip");
    public static final Path TEMPLATES_DIRECTORY = new File(ForgeIT.class.getClassLoader().getResource("templates").getFile()).toPath();

    private static StampCalculator STAMP_CALCULATOR;
    private static LanguageCalculator LANGUAGE_CALCULATOR;
    private static NavigationCalculator NAVIGATION_CALCULATOR;

    private Stream.Builder<ConceptEntity<? extends ConceptEntityVersion>> conceptStreamBuilder = Stream.builder();
    private Stream.Builder<SemanticEntity<? extends SemanticEntityVersion>> semanticStreamBuilder = Stream.builder();
    private Stream.Builder<PatternEntity<? extends PatternEntityVersion>> patternStreamBuilder = Stream.builder();
    private Stream.Builder<StampEntity<? extends StampEntityVersion>> stampStreamBuilder = Stream.builder();

    private final AtomicInteger conceptCount = new AtomicInteger(0);
    private final AtomicInteger semanticCount = new AtomicInteger(0);
    private final AtomicInteger patternCount = new AtomicInteger(0);
    private final AtomicInteger stampCount = new AtomicInteger(0);

    @BeforeAll
    public static void beforeAll() throws IOException {
        Files.createDirectories(createFilePathInTarget.apply("/test").toPath());

        var languageList = Lists.mutable.of(Coordinates.Language.UsEnglishFullyQualifiedName()).toImmutableList();
        StampCoordinateRecord stampCoordinateRecord = Coordinates.Stamp.DevelopmentLatest();
        NavigationCoordinateRecord navigationCoordinateRecord = Coordinates.Navigation.stated().toNavigationCoordinateRecord();

        STAMP_CALCULATOR = stampCoordinateRecord.stampCalculator();
        LANGUAGE_CALCULATOR = LanguageCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList);
        NAVIGATION_CALCULATOR = NavigationCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList, navigationCoordinateRecord);

        Consumer<Integer> concept
    }

    @BeforeEach
    public void beforeEach() {
        CachingService.clearAll();
        PrimitiveData.selectControllerByName("Load Ephemeral Store");

        PrimitiveData.start();
        LoadEntitiesFromProtobufFile loadEntitiesFromProtobufFile = new LoadEntitiesFromProtobufFile(PB_STARTER_DATA);
        loadEntitiesFromProtobufFile.compute();

        PrimitiveData.get().forEachConceptNid(conceptNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(conceptNid);
            conceptStreamBuilder.add((ConceptEntity<? extends ConceptEntityVersion>) entity);
            conceptCount.incrementAndGet();
        });

        PrimitiveData.get().forEachSemanticNid(semanticNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(semanticNid);
            semanticStreamBuilder.add((SemanticEntity<? extends SemanticEntityVersion>) entity);
            semanticCount.incrementAndGet();
        });

        PrimitiveData.get().forEachPatternNid(patternNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(patternNid);
            patternStreamBuilder.add((PatternEntity<? extends PatternEntityVersion>) entity);
            patternCount.incrementAndGet();
        });

        PrimitiveData.get().forEachStampNid(stampNid -> {
            Entity<? extends EntityVersion> entity = Entity.getFast(stampNid);
            stampStreamBuilder.add((StampEntity<? extends StampEntityVersion>) entity);
            stampCount.incrementAndGet();
        });
    }

    @AfterEach
    public void afterEach() {
        PrimitiveData.stop();

        conceptStreamBuilder = Stream.builder();
        semanticStreamBuilder = Stream.builder();
        patternStreamBuilder = Stream.builder();
        stampStreamBuilder = Stream.builder();

        conceptCount.set(0);
        semanticCount.set(0);
        patternCount.set(0);
        stampCount.set(0);
    }

    @Order(4)
    @Test
    public void givenConceptSemanticPatternStampStream_whenForgeExecuted_thenCombineTXTFileGenerated() {
        long startTime = System.nanoTime();

        //Make Pre-mundane STAMP Calculator
        var primordialSTAMPCoordinate = StampCoordinateRecord.make(StateSet.ACTIVE,
                TinkarTerm.PRIMORDIAL_PATH.nid(),
                Set.of(ConceptFacade.make(TinkarTerm.PRIMORDIAL_MODULE.nid())));

        try (FileWriter fw = new FileWriter(createFilePathInTarget.apply("/test/combine.txt"))) {
            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .conceptData(conceptStreamBuilder.build(), index -> {
                        if (index % 100_000 == 0) {
                            LOG.info("{}% concepts completed", ((double) index / conceptCount.get()) * 100);
                        } else if (index == conceptCount.get()) {
                            LOG.info("100% concepts completed");
                        }
                    })
                    .semanticData(semanticStreamBuilder.build(), index -> {
                        if (index % 100_000 == 0) {
                            LOG.info("{}% semantics completed", ((double) index / semanticCount.get()) * 100);
                        } else if (index == semanticCount.get()) {
                            LOG.info("Semantics completed");
                        }
                    })
                    .patternData(patternStreamBuilder.build(), index -> {
                        if (index % 100_000 == 0) {
                            LOG.info("{}% patterns completed", ((double) index / patternCount.get()) * 100);
                        } else if (index == patternCount.get()) {
                            LOG.info("Patterns completed");
                        }
                    })
                    .stampData(stampStreamBuilder.build(), index -> {
                        if (index % 100_000 == 0) {
                            LOG.info("{}% stamps completed", ((double) index / stampCount.get()) * 100);
                        } else if (index == stampCount.get()) {
                            LOG.info("Stamps completed");
                        }

                    })
                    .variable("stringFieldConcept", TinkarTerm.STRING_FIELD)
                    .variable("textPattern", TinkarTerm.DESCRIPTION_PATTERN)
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultNavigationCalc", NAVIGATION_CALCULATOR)
                    .variable("primordialSTAMPCalc", primordialSTAMPCoordinate.stampCalculator())
                    .template("combine.ftl", new BufferedWriter(fw))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000_000_000;

        LOG.info("Elapsed Time (s): {}", elapsedTime);
    }

    @Order(1)
    @Test
    public void givenConceptStream_whenForgeExecuted_thenConceptTXTFileGenerated() {

    }

}
