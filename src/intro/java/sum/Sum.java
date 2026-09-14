package intro.java.sum;

import java.util.List;
import java.util.ArrayList;

public class Sum {
    public static void main(String[] args) {
        String input = String.join(" ", args);
        List<Integer> numbers = parseNumbers(input);

        int result = getNumbersSum(numbers);

        System.out.println(result);
    }

    public static List<Integer> parseNumbers(String input) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        byte sign = 1;

        for (int i = 0; i < input.length(); ++i) {
            if (Character.isWhitespace(input.charAt(i))
                    || input.charAt(i) == '+') {
                continue;
            }

            if (input.charAt(i) == '-') {
                sign *= -1;
            }

            if (Character.isDigit(input.charAt(i))) {
                int number = parseSingleNumber(input, i) * sign;

                numbers.add(number);
                sign = 1;
            }
        }

        return numbers;
    }

    private static int parseSingleNumber(String input, int index) {
        int endIndex = index;

        while (endIndex < input.length() && Character.isDigit(input.charAt(endIndex))) {
            ++endIndex;
        }

        if (index == endIndex) {
            // Throw an error, maybe.
        }

        return Integer.parseInt(input.substring(index, endIndex));
    }

    public static int getNumbersSum(Iterable<Integer> numbers) {
        int sum = 0;

        for (int number : numbers) {
            sum += number;
        }

        return sum;
    }
}
