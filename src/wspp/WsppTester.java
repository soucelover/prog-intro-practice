package wspp;

import base.ExtendedRandom;
import base.Named;
import base.Pair;
import base.TestCounter;
import wordStat.WordStatChecker;
import wordStat.WordStatTester;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WsppTester {
    private WsppTester() {
    }

    public record Cmp(
            Comparator<Map.Entry<String, Integer>> comparator,
            boolean reverse
    ) implements Function<Map<String, Integer>, Stream<Map.Entry<String, Integer>>> {
        @Override
        public Stream<Map.Entry<String, Integer>> apply(final Map<String, Integer> totals) {
            final List<Map.Entry<String, Integer>> entries = new ArrayList<>(totals.entrySet());
            if (reverse) {
                Collections.reverse(entries);
            }
            return entries.stream().sorted(comparator);
        }

        public Cmp b() {
            return new Cmp(comparator, !reverse);
        }
    }

    public static <T> Consumer<TestCounter> variant(
            final Named<Cmp> comparator,
            final Named<IntFunction<IntStream>> selector,
            final Named<Extractor<T>> extractor,
            final Named<String> extra
    ) {
        final String name = comparator.name() + selector.name() + extractor.name() + extra.name();
        return variant(name, comparator.value(), extractor.value(), selector.value(), extra.value());
    }

    public static <T> Consumer<TestCounter> variant(
            final String name,
            final Cmp comparator,
            final Extractor<T> extractor,
            final IntFunction<IntStream> selector,
            final String extra
    ) {
        // Stream "magic" code. You do not expect to understand it
        return counter -> WordStatChecker.test(
                counter,
                "Wspp" + name,
                text -> {
                    final Map<String, Integer> totals = Arrays.stream(text)
                            .flatMap(Arrays::stream)
                            .map(word -> word.toLowerCase(Locale.ROOT))
                            .collect(Collectors.toMap(
                                    Function.identity(), k -> 1, Integer::sum,
                                    () -> new LinkedHashMap<>(16, 0.75f, comparator.reverse())
                            ));
                    final int[] lengths = Arrays.stream(text).mapToInt(a -> a.length).toArray();
                    final int[] sizes = new int[lengths.length + 1];
                    int start = 0;
                    for (int i = 0; i < lengths.length; i++) {
                        sizes[i] = start;
                        start += lengths[i];
                    }
                    sizes[lengths.length] = start;

                    final Map<String, String> selected = IntStream.range(0, text.length).boxed()
                            .flatMap(r -> IntStream.range(0, text[r].length).boxed()
                                    .collect(Collectors.groupingBy(
                                            w -> text[r][w].toLowerCase(Locale.ROOT),
                                            Collectors.collectingAndThen(
                                                    Collectors.mapping(
                                                            w -> extractor.select(
                                                                    r + 1,
                                                                    w + 1,
                                                                    text[r].length,
                                                                    sizes[r] + w + 1,
                                                                    sizes[lengths.length]
                                                            ),
                                                            Collectors.toUnmodifiableList()
                                                    ),
                                                    list -> selector
                                                            .apply(list.size())
                                                            .mapToObj(list::get)
                                                            .toList()
                                            )
                                    ))
                                    .entrySet().stream()
                            )
                            .collect(Collectors.groupingBy(
                                    Map.Entry::getKey,
                                    Collectors.flatMapping(
                                            e -> e.getValue().stream(),
                                            Collectors.mapping(
                                                    String::valueOf,
                                                    Collectors.mapping(" "::concat, Collectors.joining())
                                            )
                                    )
                            ));
                    return comparator.apply(totals)
                            .map(e -> Pair.of(e.getKey(), e.getValue() + selected.get(e.getKey())))
                            .collect(Collectors.toList());
                },
                checker -> {
                    final Pattern pattern = Pattern.compile(new String(
                            Base64.getDecoder().decode("W15ccHtJc0xldHRlcn0nXHB7UGR9" + extra),
                            StandardCharsets.US_ASCII
                    ) + "]+");
                    final String good = String.join("", pattern.split(WordStatTester.POST_LOWER));

                    checker.test(pattern, "To be, or not to be, that is the question:");
                    checker.test(
                            pattern,
                            "Monday's child is fair of face.",
                            "Tuesday's child is full of grace."
                    );
                    checker.test(
                            pattern,
                            "Шалтай-Болтай",
                            "Сидел на стене.",
                            "Шалтай-Болтай",
                            "Свалился во сне."
                    );

                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.ENGLISH, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(10, 3, 5, 5, ExtendedRandom.RUSSIAN, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.GREEK, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, WordStatChecker.DASH, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.ENGLISH, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(10, 3, 5, 5, ExtendedRandom.RUSSIAN, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.GREEK, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, WordStatChecker.DASH, WordStatChecker.ADVANCED_DELIMITERS);

                    checker.randomTest(10, 20, 10, 3, good, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(10, 20, 10, 3, good, WordStatChecker.ADVANCED_DELIMITERS);

                    final int d = TestCounter.DENOMINATOR;
                    final int d2 = TestCounter.DENOMINATOR2;
                    checker.randomTest(
                            100,
                            1000 / d,
                            1000 / d2,
                            1000 / d2,
                            WordStatChecker.ALL,
                            WordStatChecker.ADVANCED_DELIMITERS
                    );
                    checker.randomTest(10, 1000 / d, 1000 / d2, 1000 / d2, good, WordStatChecker.ADVANCED_DELIMITERS);

                    checker.randomTest(10000 / d, 20, 10, 5, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(1000000 / d, 2, 2, 1, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);

                    checker.test(pattern, WordStatTester.PRE_LOWER);
                    checker.test(pattern, WordStatTester.POST_LOWER);
                }
        );
    }

    @FunctionalInterface
    public interface Extractor<T> {
        T select(int l, int li, int lt, int gi, int gt);
    }
}
