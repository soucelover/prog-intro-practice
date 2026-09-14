package intro.java.sum;

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
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
