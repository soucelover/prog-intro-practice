package intro.java.reverse;

import base.Named;
import base.Selector;
import base.TestCounter;
import intro.java.word_stat.WordStatTest;

import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class FastReverseTest {
    // === Common
    public static final int MAX_SIZE = 1_000_000 / TestCounter.DENOMINATOR / TestCounter.DENOMINATOR;

    public static Consumer<TestCounter> variant(
            final String name,
            final Named<ReverseTester.Op> op
    ) {
        return ReverseTester.variant(MAX_SIZE, () -> new ReverseTester(
                "FastReverse" + name,
                op.value(),
                ReverseTester.SPACE.value()
        ));
    }

    public static final Selector SELECTOR = new Selector(FastReverseTest.class)
            .variant("Base", variant("", ReverseTest.REVERSE))
            ;

    private FastReverseTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
        WordStatTest.main(args);
    }
}
