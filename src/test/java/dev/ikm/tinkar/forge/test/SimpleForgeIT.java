package dev.ikm.tinkar.forge.test;

import dev.ikm.tinkar.common.service.CachingService;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.service.ServiceKeys;
import dev.ikm.tinkar.common.service.ServiceProperties;
import dev.ikm.tinkar.coordinate.Coordinates;
import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculator;
import dev.ikm.tinkar.coordinate.language.calculator.LanguageCalculatorWithCache;
import dev.ikm.tinkar.coordinate.navigation.NavigationCoordinateRecord;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculator;
import dev.ikm.tinkar.coordinate.navigation.calculator.NavigationCalculatorWithCache;
import dev.ikm.tinkar.coordinate.stamp.StampCoordinateRecord;
import dev.ikm.tinkar.coordinate.stamp.StateSet;
import dev.ikm.tinkar.coordinate.stamp.calculator.StampCalculator;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityCountSummary;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.load.LoadEntitiesFromProtobufFile;
import dev.ikm.tinkar.forge.Forge;
import dev.ikm.tinkar.forge.TinkarForge;
import dev.ikm.tinkar.terms.ConceptFacade;
import dev.ikm.tinkar.terms.TinkarTerm;
import org.eclipse.collections.api.factory.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleForgeIT {

    private final Logger LOG = LoggerFactory.getLogger(SimpleForgeIT.class);

    public static final Function<String, File> createFilePathInTarget = (pathName) -> new File("%s/target/%s".formatted(System.getProperty("user.dir"), pathName));
    public static final File PB_STARTER_DATA = createFilePathInTarget.apply("data/tinkar-starter-data-1.0.0-pb.zip");
    public static final Path TEMPLATES_DIRECTORY = new File(SimpleForgeIT.class.getClassLoader().getResource("templates").getFile()).toPath();

    private static StampCalculator STAMP_CALCULATOR;
    private static LanguageCalculator LANGUAGE_CALCULATOR;
    private static NavigationCalculator NAVIGATION_CALCULATOR;


    @BeforeAll
    public static void beforeAll() {
        CachingService.clearAll();
//        PrimitiveData.selectControllerByName("Load Ephemeral Store");
        ServiceProperties.set(ServiceKeys.DATA_STORE_ROOT, new File("/Users/aks8m/Solor/snomedct-international"));
        PrimitiveData.selectControllerByName("Open SpinedArrayStore");
        PrimitiveData.start();

//        LoadEntitiesFromProtobufFile loadEntitiesFromProtobufFile = new LoadEntitiesFromProtobufFile(PB_STARTER_DATA);
//        loadEntitiesFromProtobufFile.compute();

        var languageList = Lists.mutable.of(Coordinates.Language.UsEnglishFullyQualifiedName()).toImmutableList();
        StampCoordinateRecord stampCoordinateRecord = Coordinates.Stamp.DevelopmentLatest();
        NavigationCoordinateRecord navigationCoordinateRecord = Coordinates.Navigation.stated().toNavigationCoordinateRecord();

        STAMP_CALCULATOR = stampCoordinateRecord.stampCalculator();
        LANGUAGE_CALCULATOR = LanguageCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList );
        NAVIGATION_CALCULATOR = NavigationCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList, navigationCoordinateRecord);
    }

    @AfterAll
    public static void afterAll() {
        PrimitiveData.stop();
    }

    @Test
    public void simpleTestTemplate() {
        final String testTemplate = "simple.ftl";
        long startTime = System.nanoTime();

        Stream.Builder<Entity<? extends EntityVersion>> conceptStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachConceptNid(conceptNid -> conceptStreamBuilder.add(Entity.getFast(conceptNid)));
        Stream.Builder<Entity<? extends EntityVersion>> semanticStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachSemanticNid(conceptNid -> semanticStreamBuilder.add(Entity.getFast(conceptNid)));
        Stream.Builder<Entity<? extends EntityVersion>> patternStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachPatternNid(conceptNid -> patternStreamBuilder.add(Entity.getFast(conceptNid)));

        //Make Pre-mundane STAMP Calculator
        var primordialSTAMPCoordinate = StampCoordinateRecord.make(StateSet.ACTIVE,
                TinkarTerm.PRIMORDIAL_PATH.nid(),
                Set.of(ConceptFacade.make(TinkarTerm.PRIMORDIAL_MODULE.nid())));

        try (FileOutputStream fos = new FileOutputStream(createFilePathInTarget.apply("simpleTestTemplate.txt"))) {

            Forge simpleForge = new TinkarForge();
            simpleForge.config(TEMPLATES_DIRECTORY)
                    .data("concepts", conceptStreamBuilder.build())
                    .data("semantics", semanticStreamBuilder.build())
                    .data("patterns", patternStreamBuilder.build())
                    .variable("stringFieldConcept", TinkarTerm.STRING_FIELD)
                    .variable("textPattern", TinkarTerm.DESCRIPTION_PATTERN)
                    .variable("defaultSTAMPCalc", STAMP_CALCULATOR)
                    .variable("defaultLanguageCalc", LANGUAGE_CALCULATOR)
                    .variable("defaultNavigationCalc", NAVIGATION_CALCULATOR)
                    .variable("primordialSTAMPCalc", primordialSTAMPCoordinate.stampCalculator())
                    .template(testTemplate, new OutputStreamWriter(fos))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000_000_000;

        LOG.info("Elapsed Time (s): {}", elapsedTime);
    }

}
