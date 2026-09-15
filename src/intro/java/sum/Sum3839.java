package intro.java.sum;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;

public class Sum3839 {
  public record Pair<First, Second>(First first, Second second) {
  }

  public static void main(String[] args) {
    String input = String.join(" ", args);
    List<BigInteger> numbers = parseNumbers(input);

    BigInteger result = getNumbersSum(numbers);

    System.out.println(result);
  }

  private static final BigInteger N_ONE = new BigInteger("-1");

  public static List<BigInteger> parseNumbers(String input) {
    ArrayList<BigInteger> numbers = new ArrayList<>();
    int minuses = 0;

    for (int i = 0; i < input.length(); ++i) {
      int char_type = Character.getType(input.charAt(i));

      if (Character.isWhitespace(input.charAt(i))
          || input.charAt(i) == '+'
          || char_type == Character.START_PUNCTUATION
          || char_type == Character.END_PUNCTUATION) {
        continue;
      }

      if (input.charAt(i) == '-') {
        ++minuses;
        continue;
      }
      // ababa1234ababa

      if (Character.isDigit(input.charAt(i))) {
        Pair<Integer, BigInteger> pair = parseSingleNumber(input, i);

        i = pair.first();
        BigInteger number = pair.second()
            .multiply((minuses & 1) == 0 ? BigInteger.ONE : N_ONE);

        numbers.add(number);
        minuses = 0;
        continue;
      }

      throw new IllegalArgumentException(
          "Unexpected character at index " + i + " (" + input.charAt(i) + ")");
    }

    if (minuses != 0) {
      throw new IllegalArgumentException("Unexpected minus sign at the end of the input");
    }

    return numbers;
  }

  private static Pair<Integer, BigInteger> parseSingleNumber(String input, Integer index) {
    int endIndex = index;

    while (endIndex < input.length() && Character.isDigit(input.charAt(endIndex))) {
      ++endIndex;
    }

    if (index == endIndex) {
      // Should be unreachable, but why not?
      throw new IllegalArgumentException("Expected a number at index " + index);
    }

    return new Pair<>(endIndex, new BigInteger(input.substring(index, endIndex)));
  }

  public static BigInteger getNumbersSum(Iterable<BigInteger> numbers) {
    BigInteger sum = BigInteger.ZERO;

    for (BigInteger number : numbers) {
      sum = sum.add(number);
    }

    return sum;
  }
}
