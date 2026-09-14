package intro.java.sum;

import java.util.List;
import java.util.ArrayList;

public class Sum {
    public record Pair<First, Second>(First first, Second second) {
    }

    public static void main(String[] args) {
        String input = String.join(" ", args);
        List<Integer> numbers = parseNumbers(input);

        int result = getNumbersSum(numbers);

        System.out.println(result);
    }

    public static List<Integer> parseNumbers(String input) {
        ArrayList<Integer> numbers = new ArrayList<>();
        int minuses = 0;

        for (int i = 0; i < input.length(); ++i) {
            if (Character.isWhitespace(input.charAt(i))
                    || input.charAt(i) == '+') {
                continue;
            }

            if (input.charAt(i) == '-') {
                ++minuses;
            }

            if (Character.isDigit(input.charAt(i))) {
                Pair<Integer, Integer> pair = parseSingleNumber(input, i);

                i = pair.first();
                int number = pair.second() * ((minuses & 1) == 0 ? 1 : -1);

                numbers.add(number);
                minuses = 0;
            }

            throw new IllegalArgumentException(
                    "Unexpected character at index " + i + " (" + input.charAt(i) + ")");
        }

        if (minuses != 0) {
            throw new IllegalArgumentException("Unexpected minus sign at the end of the input");
        }

        return numbers;
    }

    private static Pair<Integer, Integer> parseSingleNumber(String input, Integer index) {
        int endIndex = index;

        while (endIndex < input.length() && Character.isDigit(input.charAt(endIndex))) {
            ++endIndex;
        }

        if (index == endIndex) {
            // Should be unreachable, but why not?
            throw new IllegalArgumentException("Expected a number at index " + index);
        }

        return new Pair<>(endIndex, Integer.parseInt(input.substring(index, endIndex)));
    }

    public static int getNumbersSum(Iterable<Integer> numbers) {
        int sum = 0;

        for (int number : numbers) {
            sum += number;
        }

        return sum;
    }
}
