package intro.java.word_stat;

import base.Named;
import base.Selector;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Tests for <a href=
 * "https://www.kgeorgiy.info/courses/prog-intro/homeworks.html#wordstat">Word
 * Statistics</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/prog-intro/">Introduction to
 * Programming</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTest {
    // === Base
    private static final Named<Function<String, Stream<String>>> ID = Named.of("", Stream::of);
    private static final WordStatTester.Variant BASE = new WordStatTester.Variant("", false,
            Comparator.comparingInt(p -> 0));

    // === Common
    public static final Selector SELECTOR = new Selector(WordStatTester.class)
            .variant("Base", BASE.with("", ID));

    private WordStatTest() {
        // Utility class
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
