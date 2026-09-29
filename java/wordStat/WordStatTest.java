package wordStat;

import base.Named;
import base.Pair;
import base.Selector;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests for <a href="https://www.kgeorgiy.info/courses/prog-intro/homeworks.html#wordstat">Word Statistics</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/prog-intro/">Introduction to Programming</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTest {
    // === Base
    private static final Named<Function<String, Stream<String>>> ID  = Named.of("", Stream::of);
    private static final WordStatTester.Variant BASE = new WordStatTester.Variant("", false, Comparator.comparingInt(p -> 0));


    // === 41
    private static final Comparator<Pair<String, Integer>> COUNT = Comparator.comparingInt(Pair::second);
    private static final Comparator<Pair<String, Integer>> WORDS = Comparator.comparing(Pair::first);
    private static final WordStatTester.Variant ORDER_1 =
            new WordStatTester.Variant("", false, COUNT.reversed().thenComparing(WORDS));
    private static final Named<Function<String, Stream<String>>> PREFIXES = Named.of(
            "Prefixes",
            s -> IntStream.range(0, Math.min(s.length(), 100)).mapToObj(i -> s.substring(0, i + 1))
    );


    // === Common
    public static final Selector SELECTOR = new Selector(WordStatTester.class)
            .variant("Base", BASE.with("", ID))
            .variant("41",   ORDER_1.with("41",   PREFIXES))
            ;

    private WordStatTest() {
        // Utility class
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
