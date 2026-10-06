package intro.java.reverse;

import java.util.ArrayList;
import java.util.Arrays;
// my_scanner
import intro.java.my_scanner.Scanner;

public class FastReverse3839 {
  public static void main(String[] args) {
    int[][] numbers;

    try (Scanner scanner = new Scanner(System.in)) {
      numbers = parseNumbers(scanner);
    }

    processMatrix(numbers);
    printMatrix(numbers);
  }

  private static int[][] parseNumbers(final Scanner scanner) {
    List<int[]> numbers = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine(); // memory

      int[] row = parseSingleLine(line);
      numbers.add(row);
    }

    // No need to pre-allocate any space
    return numbers.toArray(new int[0][]);
  }

  private static int[] parseSingleLine(final String line) {
    int length = 0;
    int[] numbers = new int[1];

    try (Scanner scanner = new Scanner(line)) {
      while (scanner.hasNext(FastReverse3839::isTokenPart)) {
        String token = scanner.next(FastReverse3839::isTokenPart);
        int number = Math.abs(parseDecHexInt(token));

        numbers = addToCappedArray(numbers, length, number);
        ++length;
      }
    }

    return Arrays.copyOf(numbers, length);
  }

  private static int parseDecHexInt(String token) {
    // startsWith
    if (token.length() > 2 && token.charAt(0) == '0') {
      int offset = 1;

      if (token.charAt(1) == 'x' || token.charAt(1) == 'X') {
        ++offset;
      }

      return Integer.parseUnsignedInt(token.substring(offset), 16);
    }

    return Integer.parseInt(token, 10);
  }

  private static boolean isTokenPart(char character) {
    int type = Character.getType(character);
    return !Character.isWhitespace(character)
        && type != Character.START_PUNCTUATION && type != Character.END_PUNCTUATION;
  }

  private static int[] addToCappedArray(int[] array, int length, int item) {
    if (length == array.length) {
      int newLength = Math.max(length + 1, (array.length * 3) / 2);
      array = Arrays.copyOf(array, newLength);
    }

    array[length] = item;
    return array;
  }

  private static void processMatrix(int[][] numbers) {
    int[] columnSums = new int[0];

    for (int i = 0; i < numbers.length; ++i) {
      int[] row = numbers[i];

      // n^3/2
      if (row.length > columnSums.length) {
        columnSums = Arrays.copyOf(columnSums, row.length);
      }

      if (row.length > 0) {
        row[0] += columnSums[0];
        columnSums[0] = row[0];
      }

      for (int j = 1; j < row.length; ++j) {
        columnSums[j] += row[j];
        row[j] = columnSums[j] + row[j - 1];
      }
    }
  }

  private static void printMatrix(int[][] numbers) {
    for (int i = 0; i < numbers.length; ++i) {
      int[] row = numbers[i];

      for (int j = 0; j < row.length - 1; ++j) {
        System.out.print(toMarkedHexString(row[j]) + " ");
      }

      if (row.length > 0) {
        System.out.print(toMarkedHexString(row[row.length - 1]));
      }

      System.out.println();
    }
  }

  private static String toMarkedHexString(int value) {
    return "0x" + Integer.toHexString(value);
  }
}
