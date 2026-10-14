package wspp;

import base.Named;
import base.Selector;
import base.TestCounter;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WsppTest {
    // === 41
    private static final Named<WsppTester.Cmp> SORTED = cmp("Sorted", Map.Entry.comparingByKey());
    private static final Named<IntFunction<IntStream>> ODD = Named.of("Odd", size -> IntStream.range(0, size).filter(i -> (i & 1) == 0));
    private static final Named<WsppTester.Extractor<Integer>> LOCAL = Named.of("L", (r, l, L, g, G) -> l);
    private static final Named<String> LET_NUM = Named.of("LetNum", "XHB7Tmx9");


    // === 42
    private static final Named<IntFunction<IntStream>> EVEN = Named.of("Even", size -> IntStream.range(0, size).filter(i -> (i & 1) == 1));


    // === 3839
    private static final Named<String> CONNECT = Named.of("Connect", "XHB7UGN9");
    private static final Named<WsppTester.Cmp> SORTED_R = cmp("SortedR",
            Map.Entry.<String, Integer>comparingByKey().reversed());


    // === 3637
    private static final Named<String> CURRENCY = Named.of("Currency", "XHB7U2N9");


    // === 48
    private static final Named<WsppTester.Extractor<String>> POSITION_LB = Named.of("PositionLB", (r, l, L, g, G) -> r + ":" + l);
    

    // === Base
    public static Named<WsppTester.Cmp> cmp(final String name, final Comparator<Map.Entry<String, Integer>> comparator) {
        return Named.of(name, new WsppTester.Cmp(comparator, false));
    }

    private static final Named<WsppTester.Cmp> INPUT = cmp("", Comparator.comparingInt(e -> 0));
    private static final Named<IntFunction<IntStream>> ALL = Named.of("", size -> IntStream.range(0, size));
    private static final Named<WsppTester.Extractor<Object>> WSPP = Named.of("", (r, l, L, g, G) -> g);
    private static final Named<String> NONE = Named.of("", "");


    // === Common
    public static <T> Consumer<TestCounter> variant(
            final String name,
            final Named<WsppTester.Cmp> comparator,
            final Named<IntFunction<IntStream>> selector,
            final Named<WsppTester.Extractor<T>> extractor,
            final Named<String> extra
    ) {
        return WsppTester.variant(name, comparator.value(), extractor.value(), selector.value(), extra.value());
    }

    public static final Selector SELECTOR = new Selector(WsppTester.class)
            .variant("Base",            variant("", INPUT, ALL, WSPP, NONE))
            .variant("3637",            variant("3637", SORTED_R, ODD, LOCAL, CURRENCY))
            .variant("3839",            variant("3839", SORTED_R, EVEN, LOCAL, CONNECT))
            .variant("41",              variant("41",   SORTED, ODD, LOCAL, LET_NUM))
            .variant("42",              variant("42",   SORTED, EVEN, LOCAL, LET_NUM))
            .variant("48",              variant("48",   SORTED, ALL, POSITION_LB, LET_NUM))
            ;

    private WsppTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
