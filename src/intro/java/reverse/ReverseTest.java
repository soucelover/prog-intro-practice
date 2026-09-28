package intro.java.reverse;

import base.ExtendedRandom;
import base.Named;
import base.Selector;
import base.TestCounter;
import intro.java.reverse.ReverseTester.Op;

import java.util.Arrays;
import java.util.function.*;
import java.util.stream.IntStream;

/**
 * Tests for {@code Reverse} homework.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    // === Base
    public static final Named<Op> REVERSE = Named.of("", ReverseTester::transform);


    // === 41

    private static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX = Named.of("Hex", (r, i) -> Integer.toHexString(i));
    private static final Named<LongUnaryOperator> NONE = Named.of("", n -> n);
    private static final Monoid SUM = new Monoid("Sum", 0, (a, b) -> (int) (a + b));
    private static final Named<Op> SUM_41 = sum1(NONE);

    private static Named<Op> sum1(final Named<LongUnaryOperator> lift) {
        return cross(lift);
    }

    private static Named<Op> cross(final Named<LongUnaryOperator> lift) {
        // This code is intentionally obscure
        return Named.of(
                "Sum1" + lift.name(),
                ints -> {
                    final long[] rt = Arrays.stream(ints)
                            .map(Arrays::stream)
                            .mapToLong(row -> row.mapToLong(lift.value()::applyAsLong).reduce(ReverseTest.SUM.zero(), Long::sum))
                            .toArray();
                    final long[] ct = new long[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
                    Arrays.fill(ct, 0);
                    Arrays.stream(ints).forEach(r -> range(r.length, false)
                            .forEach(i -> ct[i] = ct[i] + lift.value().applyAsLong(r[i])));
                    return range(ints.length, false)
                            .mapToObj(r -> range(ints[r].length, false)
                                    .mapToLong(c -> rt[r] + ct[c] - lift.value().applyAsLong(ints[r][c]))
                                    .toArray())
                            .toArray(long[][]::new);
                }
        );
    }


    // === 42

    private static final Named<LongUnaryOperator> ABS = Named.of("Abs", Math::abs);
    private static final Named<Op> SUM_42 = sum1(ABS);


    // === 3839

    private static final Named<Op> SUM_3839 = sum2(ABS);

    private static Named<Op> sum2(final Named<LongUnaryOperator> lift) {
        return Named.of("Sum2" + lift.name(), Order.DIRECT.scan2(0, lift.value()::applyAsLong, Long::sum, Long::sum));
    }

    private enum Order {
        DIRECT("B", false, false);

        final String name;
        final boolean reverse;
        final boolean accumulate;

        Order(final String name, final boolean reverse, final boolean accumulate) {
            this.name = name;
            this.reverse = reverse;
            this.accumulate = accumulate;
        }

        Op scan2(
                final int zero,
                final IntToLongFunction map,
                final LongBinaryOperator reduceC,
                final LongBinaryOperator reduceR
        ) {
            return ints -> {
                // This code is intentionally obscure
                final int length = Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0);
                final long[] cs = new long[length];
                final long[] cc = new long[length + 1];
                Arrays.fill(cs, zero);
                Arrays.fill(cc, zero);
                final int d = reverse ? +1 : -1;
                //noinspection NestedAssignment
                final long[][] rows = range(ints.length, reverse).mapToObj(i -> {
                            range(ints[i].length, reverse).forEachOrdered(j -> cc[j] = reduceR.applyAsLong(
                                    0 <= j + d && j + d < ints[i].length || accumulate ? cc[j + d] : zero,
                                    cs[j] = reduceC.applyAsLong(cs[j], map.applyAsLong(ints[i][j]))
                            ));
                            return Arrays.copyOf(cc, ints[i].length);
                        })
                        .toArray(long[][]::new);
                return range(ints.length, reverse).mapToObj(i -> rows[i]).toArray(long[][]::new);
            };
        }
    }

    private static IntStream range(final int length, final  boolean reverse) {
        return reverse ? IntStream.iterate(length - 1, i -> i >= 0, i -> i - 1) : IntStream.range(0, length);
    }


    // === 3637

    private static final Named<Op> SUM_3637 = sum2(NONE);


    // === 48

    static final int M = 1_000_000;
    private static final Named<LongUnaryOperator> MOD = Named.of("Mod", a -> (a % M + M) % M);
    private static final Named<Op> SUM_48 = sum1(MOD);


    // === 4749

    private static final Named<LongUnaryOperator> ABS_MOD = Named.of("AbsMod", a -> Math.abs(a) % M);
    private static final Named<Op> SUM_4749 = sum1(ABS_MOD);


   // === 3233

    public static final Named<Op> SUM_3233 = SUM.row(ABS);
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> DEC = Named.of("", (r, i) -> Integer.toString(i));

    record Monoid(String name, int zero, LongBinaryOperator op) {
        private Named<Op> row(final Named<LongUnaryOperator> lift) {
            return Named.of(
                    this.name() + "R" + Order.DIRECT.name + lift.name(),
                    Order.DIRECT.scan2(zero, lift.value()::applyAsLong, (a, b) -> b, this.op())
            );
        }
    }


    // === 3435
    private static final Named<Op> SUM_3435 = SUM.row(NONE);


    // === Common

    public static final int MAX_SIZE = 10_000 / TestCounter.DENOMINATOR;

    public static final Selector SELECTOR = selector(ReverseTest.class, MAX_SIZE);

    private ReverseTest() {
        // Utility class
    }

    public static Consumer<TestCounter> variant(
            final int maxSize,
            final String name,
            final Named<Op> op,
            final Named<BiFunction<ExtendedRandom, Integer, String>> input
    ) {
        return ReverseTester.variant(maxSize, () -> new ReverseTester(
                "Reverse" + name,
                op.value(),
                ReverseTester.SPACE.value(),
                input.value(),
                input.value()
        ));
    }

    public static Selector selector(final Class<?> owner, final int maxSize) {
        return new Selector(owner)
                .variant("Base", ReverseTester.variant(maxSize, REVERSE))
                .variant("3233", variant(maxSize, "3233", SUM_3233, DEC))
                .variant("3435", variant(maxSize, "3435", SUM_3435, HEX))
                .variant("3637", variant(maxSize, "3637", SUM_3637, HEX))
                .variant("3839", variant(maxSize, "3839", SUM_3839, HEX))
                .variant("41"  , variant(maxSize, "41",   SUM_41, HEX))
                .variant("42"  , variant(maxSize, "42",   SUM_42, HEX))
                .variant("4749", variant(maxSize, "4749", SUM_4749, DEC))
                .variant("48"  , variant(maxSize, "48",   SUM_48, DEC))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
