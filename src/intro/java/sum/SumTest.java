package intro.java.sum;

import base.*;

import java.math.BigInteger;
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


    // === 42

    private static <T extends Number> Named<Op<T>> control(final Named<SumTest.Op<T>> inner) {
        //noinspection UnnecessaryUnicodeEscape
        return addSpaces("Control", inner,
                "\r\n\t",
                "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u0009\u000b\u000c\u000e"
                        + "\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d"
                        + "\u001e\u001f\u007f\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b"
                        + "\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009a"
                        + "\u009b\u009c\u009d\u009e\u009f"
        );
    }


    // === 3839

    private static final Named<Supplier<SumTester<BigInteger>>> BIG_INTEGER = Named.of("BigInteger", () -> new SumTester<>(
            BigInteger::add, BigInteger::valueOf, (r, max) -> new BigInteger(max.bitLength(), r.getRandom()), TO_STRING,
            BigInteger.TEN, BigInteger.TEN.pow(10), BigInteger.TEN.pow(100), BigInteger.TWO.pow(1000))
            .test(0, "10000000000000000000000000000000000000000 -10000000000000000000000000000000000000000"));


    private static <T extends Number> Named<Op<T>> brackets(final Named<Op<T>> inner) {
        //noinspection UnnecessaryUnicodeEscape
        return addSpaces("Brackets", inner,
                "([{)]}",
                "\u0F3A\u0F3C\u169B\u201A\u201E\u2045\u207D\u208D\u2308\u230A\u2329\u2768\u276A" +
                        "\u276C\u276E\u2770\u2772\u2774\u27C5\u27E6\u27E8\u27EA\u27EC\u27EE\u2983\u2985" +
                        "\u2987\u2989\u298B\u298D\u298F\u2991\u2993\u2995\u2997\u29D8\u29DA\u29FC\u2E22" +
                        "\u2E24\u2E26\u2E28\u2E42\u3008\u300A\u300C\u300E\u3010\u3014\u3016\u3018\u301A" +
                        "\u301D\uFD3F\uFE17\uFE35\uFE37\uFE39\uFE3B\uFE3D\uFE3F\uFE41\uFE43\uFE47\uFE59" +
                        "\uFE5B\uFE5D\uFF08\uFF3B\uFF5B\uFF5F\uFF62",
                "\u0F3B\u0F3D\u169C\u2046\u207E\u208E\u2309\u230B\u232A\u2769\u276B\u276D\u276F" +
                        "\u2771\u2773\u2775\u27C6\u27E7\u27E9\u27EB\u27ED\u27EF\u2984\u2986\u2988\u298A" +
                        "\u298C\u298E\u2990\u2992\u2994\u2996\u2998\u29D9\u29DB\u29FD\u2E23\u2E25\u2E27" +
                        "\u2E29\u3009\u300B\u300D\u300F\u3011\u3015\u3017\u3019\u301B\u301E\u301F\uFD3E" +
                        "\uFE18\uFE36\uFE38\uFE3A\uFE3C\uFE3E\uFE40\uFE42\uFE44\uFE48\uFE5A\uFE5C\uFE5E" +
                        "\uFF09\uFF3D\uFF5D\uFF60\uFF63"
        );
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
            Named.of("", Runner.packages("", "sum", "intro.java.sum")::args);

    public static final Selector SELECTOR = selector(SumTest.class, RUNNER);

    private SumTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final Named<Function<String, Runner>> runner) {
        return new Selector(owner)
                .variant("Base",            variant("", runner, BASE, plain()))
                .variant("3839",            variant("3839", runner, BIG_INTEGER, brackets(plain())))
                .variant("41",              variant("41"  , runner, SHORT, currency(plain())))
                .variant("42",              variant("42"  , runner, SHORT, control(plain())))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
