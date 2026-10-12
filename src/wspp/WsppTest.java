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
            ;

    private WsppTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
