package intro.java.reverse;

import base.ExtendedRandom;
import base.Named;
import base.Selector;
import base.TestCounter;
import intro.java.reverse.ReverseTester.Op;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Tests for {@code Reverse} homework.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    // === Base
    public static final Named<Op> REVERSE = Named.of("", ReverseTester::transform);


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
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
