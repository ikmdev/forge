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
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityCountSummary;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.load.LoadEntitiesFromProtobufFile;
import dev.ikm.tinkar.forge.Forge;
import dev.ikm.tinkar.forge.TinkarForge;
import org.eclipse.collections.api.factory.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

public class ForgeIT {

    private final Logger LOG = LoggerFactory.getLogger(ForgeIT.class);

    public static final Function<String, File> createFilePathInTarget = (pathName) -> new File("%s/target/%s".formatted(System.getProperty("user.dir"), pathName));
    public static final File PB_STARTER_DATA = createFilePathInTarget.apply("data/tinkar-starter-data-1.0.0-pb.zip");
    public static final Path TEMPLATES_DIRECTORY = new File(ForgeIT.class.getClassLoader().getResource("templates").getFile()).toPath();

    private static StampCalculator stampCalculator;
    private static LanguageCalculator languageCalculator;
    private static NavigationCalculator navigationCalculator;


    @BeforeAll
    public static void beforeAll() {
        CachingService.clearAll();
        PrimitiveData.selectControllerByName("Load Ephemeral Store");
        PrimitiveData.start();
        EntityCountSummary entityCountSummary = new LoadEntitiesFromProtobufFile(PB_STARTER_DATA).compute();

        var languageList = Lists.mutable.of(Coordinates.Language.UsEnglishFullyQualifiedName()).toImmutableList();
        StampCoordinateRecord stampCoordinateRecord = Coordinates.Stamp.DevelopmentLatest();
        NavigationCoordinateRecord navigationCoordinateRecord = Coordinates.Navigation.stated().toNavigationCoordinateRecord();

        stampCalculator = stampCoordinateRecord.stampCalculator();
        languageCalculator = LanguageCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList );
        navigationCalculator = NavigationCalculatorWithCache.getCalculator(stampCoordinateRecord, languageList, navigationCoordinateRecord);
    }

    @AfterAll
    public static void afterAll() {
        PrimitiveData.stop();
    }

    @Test
    public void simpleTestTemplate() {
        final String testTemplate = "test.ftl";
        long startTime = System.nanoTime();

        Stream.Builder<Entity<? extends EntityVersion>> conceptStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachConceptNid(conceptNid -> conceptStreamBuilder.add(Entity.getFast(conceptNid)));
        Stream.Builder<Entity<? extends EntityVersion>> semanticStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachSemanticNid(conceptNid -> semanticStreamBuilder.add(Entity.getFast(conceptNid)));
        Stream.Builder<Entity<? extends EntityVersion>> patternStreamBuilder = Stream.builder();
        PrimitiveData.get().forEachPatternNid(conceptNid -> patternStreamBuilder.add(Entity.getFast(conceptNid)));

        Writer out = new OutputStreamWriter(System.out);
        Forge forge = new TinkarForge(stampCalculator, languageCalculator, navigationCalculator)
                .config(TEMPLATES_DIRECTORY)
                .data("concepts", conceptStreamBuilder.build())
                .data("semantics", semanticStreamBuilder.build())
                .data("patterns", patternStreamBuilder.build())
                .template(testTemplate, out);
        forge.execute();


        long endTime = System.nanoTime();
        double elapsedTime = (double) (endTime - startTime) / 1000_000_000;

        LOG.info("Elapsed Time (s): {}", elapsedTime);
    }

}
