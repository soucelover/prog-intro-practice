package sum;

import base.*;

import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class SumTest {
    // === Base

    @FunctionalInterface
    /* package-private */ interface Op<T extends Number> extends UnaryOperator<SumTester<T>> {}

    private static final BiConsumer<Number, String> TO_STRING = (expected, out) -> Asserts.assertEquals("Sum", expected.toString(), out);

    private static final Named<Supplier<SumTester<Integer>>> BASE = Named.of("", () -> new SumTester<>(
            Integer::sum, n -> (int) n, (r, max) -> r.nextInt() % max, TO_STRING,
            10, 100, Integer.MAX_VALUE
    ));

    /* package-private */ static <T extends Number> Named<Op<T>> plain() {
        return Named.of("", test -> test);
    }


    // === 41

    private static final Named<Supplier<SumTester<Short>>> SHORT = Named.of("Short", () -> new SumTester<>(
            (a, b) -> (short) (a + b), n -> (short) n, (r, max) -> (short) (r.getRandom().nextLong() % max), TO_STRING,
            (short) 10, (short) 100, (short) Byte.MAX_VALUE, Short.MAX_VALUE));

    private static <T extends Number> Named<Op<T>> currency(final Named<SumTest.Op<T>> inner) {
        //noinspection UnnecessaryUnicodeEscape
        return addSpaces("Currency", inner,
                "$",
                "\u00a2\u00a3\u00a4\u00a5\u058f\u060b\u07fe\u07ff\u09f2\u09f3\u09fb\u0af1\u0bf9\u0e3f"
                        + "\u17db\u20a0\u20a1\u20a2\u20a3\u20a4\u20a5\u20a6\u20a7\u20a8\u20a9\u20aa\u20ab\u20ac\u20ad"
                        + "\u20ae\u20af\u20b0\u20b1\u20b2\u20b3\u20b4\u20b5\u20b6\u20b7\u20b8\u20b9\u20ba\u20bb\u20bc"
                        + "\u20bd\u20be\u20bf\ua838\ufdfc\ufe69\uff04\uffe0\uffe1\uffe5\uffe6"
        );
    }

    private static <T extends Number> Named<Op<T>> addSpaces(
            final String prefix,
            final Named<Op<T>> inner,
            final String... spaces
    ) {
        return Named.of(prefix + inner.name(), t -> inner.value().apply(t).addSpaces(spaces));
    }


    // === Common

    /* package-private */ static <T extends Number> Consumer<TestCounter> variant(
            final String suffix,
            final Named<Function<String, Runner>> runner,
            final Named<Supplier<SumTester<T>>> test,
            final Named<? extends Function<? super SumTester<T>, ? extends SumTester<?>>> modifier
    ) {
        return counter -> modifier.value().apply(test.value().get())
                .test("Sum" + suffix, counter, runner.value());
    }

    /* package-private */ static <T extends Number> Consumer<TestCounter> variant(
            final Named<Function<String, Runner>> runner,
            final Named<Supplier<SumTester<T>>> test,
            final Named<? extends Function<? super SumTester<T>, ? extends SumTester<?>>> modifier
    ) {
        return variant(test.name() + modifier.name() + runner.name(), runner, test, modifier);
    }


    /* package-private */ static final Named<Function<String, Runner>> RUNNER =
            Named.of("", Runner.packages("", "sum")::args);

    public static final Selector SELECTOR = selector(SumTest.class, RUNNER);

    private SumTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final Named<Function<String, Runner>> runner) {
        return new Selector(owner)
                .variant("Base",            variant("", runner, BASE, plain()))
                .variant("41",              variant("41"  , runner, SHORT, currency(plain())))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
