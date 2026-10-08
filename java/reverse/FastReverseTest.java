package reverse;

import base.ExtendedRandom;
import base.Named;
import base.Selector;
import base.TestCounter;
import wordStat.WordStatTest;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class FastReverseTest {
    // === 41

    public static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_DEC = Named.of("HexDec", (r, i) -> {
        if (r.nextBoolean()) {
            return Integer.toString(i);
        } else {
            final String hex = Integer.toHexString(i);
            return (r.nextBoolean() ? "0x" : "0X") + (r.nextBoolean() ? hex.toUpperCase(Locale.ROOT) : hex);
        }
    });

    public static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_OUT = Named.of("Hex", (r, i) -> "0x" + Integer.toHexString(i));

    public static final Named<String> CURRENCY = spaces("Currency", ch -> ch == ' ' || Character.getType(ch) == Character.CURRENCY_SYMBOL);

    public static Named<String> spaces(final String name, final IntPredicate isSpace) {
        return Named.of(
                name,
                IntStream.range(0, Character.MAX_VALUE)
                        .filter(isSpace)
                        .filter(ch -> ch != 13 && ch != 10)
                        .mapToObj(Character::toString)
                        .collect(Collectors.joining())
        );
    }
    
    public static Consumer<TestCounter> variant(
            final String name,
            final Named<ReverseTester.Op> op,
            final Named<String> spaces,
            final Named<BiFunction<ExtendedRandom, Integer, String>> input,
            final Named<BiFunction<ExtendedRandom, Integer, String>> output
    ) {
        return ReverseTester.variant(MAX_SIZE, () -> new ReverseTester(
                "FastReverse" + name,
                op.value(),
                spaces.value(),
                input.value(),
                output.value()
        ));
    }


    // === 42

    public static final Named<String> CONTROL = spaces("Control", ch -> ch == ' ' || Character.getType(ch) == Character.CONTROL && ch != '\u0013' && ch != '\u001b');


    // === 3839

    public static final Named<String> BRACKETS = spaces("Brackets", ch -> ch == ' ' || Character.getType(ch) == Character.START_PUNCTUATION || Character.getType(ch) == Character.END_PUNCTUATION);


    // === 48

    public static final Named<String> QUOTE = spaces("Quote", ch -> ch == ' ' || ch == '\'' || ch == '"' || Character.getType(ch) == Character.INITIAL_QUOTE_PUNCTUATION || Character.getType(ch) == Character.FINAL_QUOTE_PUNCTUATION);

    
    // === 3233
    
    public static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX = Named.of("Hex", (r, i) -> Integer.toHexString(i));
    public static final Named<String> WHITESPACE = spaces("Whitespace", Character::isWhitespace);


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
            .variant("3233", variant("3233", ReverseTest.SUM_3233, WHITESPACE, HEX, HEX))
            .variant("3435", variant("3435", ReverseTest.SUM_3435, WHITESPACE, HEX, HEX))
            .variant("3637", variant("3637", ReverseTest.SUM_3637, BRACKETS, HEX_DEC, HEX_OUT))
            .variant("3839", variant("3839", ReverseTest.SUM_3839, BRACKETS, HEX_DEC, HEX_OUT))
            .variant("41"  , variant("41",   ReverseTest.SUM_41, CURRENCY, HEX_DEC, HEX_OUT))
            .variant("42"  , variant("42",   ReverseTest.SUM_42, CONTROL, HEX_DEC, HEX_OUT))
            .variant("4749", variant("4749", ReverseTest.SUM_4749, QUOTE, HEX_DEC, HEX_OUT))
            .variant("48"  , variant("48",   ReverseTest.SUM_48, QUOTE, HEX_DEC, HEX_OUT))
            ;

    private FastReverseTest() {
    }

    public static void main(final String... args) {
        WordStatTest.main(args);
        SELECTOR.main(args);
    }
}
