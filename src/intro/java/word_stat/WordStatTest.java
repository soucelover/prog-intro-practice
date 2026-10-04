package intro.java.word_stat;

import base.Named;
import base.Pair;
import base.Selector;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;
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


    // === 3839
    private static final WordStatTester.Variant ORDER_2 =
            new WordStatTester.Variant("", false, COUNT.thenComparing(WORDS));
    private static final Named<Function<String, Stream<String>>> SHINGLES = Named.of(
            "Shingles",
            s -> s.length() >= 3 
                    ? IntStream.rangeClosed(0, s.length() - 3).mapToObj(i -> s.substring(i, i + 3)) 
                    : Stream.of(s)
    );


    // == 3637
    private static final Named<Function<String, Stream<String>>> SUFFIXES = Named.of(
            "Suffixes",
            s -> IntStream.range(Math.max(0, s.length() - 100), s.length()).mapToObj(s::substring)
    );


    // === 48
    private static final Named<Function<String, Stream<String>>> DEDUP_A = Named.of(
            "DedupA",
            s -> dedup(s.chars().sorted()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString())
    );

    private static Stream<String> dedup(final String s) {
        final int[] prev = new int[]{-1};
        return Stream.of(s.chars()
                .filter(c -> {
                    final boolean r = prev[0] != c;
                    prev[0] = c;
                    return r;
                })
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString());
    }


    // === 4749
    private static final Named<Function<String, Stream<String>>> DEDUP_D = Named.of(
            "DedupD",
            s -> dedup(s.chars().sorted()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .reverse()
                    .toString())
    );


    // === 3233
    private static final Named<Function<String, Stream<String>>> DEDUP_C = Named.of(
            "DedupC",
            WordStatTest::dedup
    );


    // == 3435
    private static final Named<Function<String, Stream<String>>> DEDUP_I = Named.of(
            "DedupI",
            s -> Stream.of(s.chars().boxed()
                    .collect(Collectors.toCollection(LinkedHashSet::new))
                    .stream()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString())
    );

    // === Common
    public static final Selector SELECTOR = new Selector(WordStatTester.class)
            .variant("Base", BASE.with("", ID))
            .variant("41",   ORDER_1.with("41",   PREFIXES))
            .variant("42",   ORDER_1.with("42",   SHINGLES))
            .variant("3839", ORDER_2.with("3839", SHINGLES))
            .variant("3536", ORDER_2.with("3637", SUFFIXES))
            .variant("48",   ORDER_1.with("48",   DEDUP_A))
            .variant("4749", ORDER_1.with("4749", DEDUP_D))
            .variant("3233", ORDER_2.with("3233", DEDUP_C))
            .variant("3435", ORDER_2.with("3435", DEDUP_I))
            ;

    private WordStatTest() {
        // Utility class
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
