package intro.java.reverse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import intro.java.myscanner.Scanner;

public class FastReverse3839 {
  record InputMatrix(int[][] numbers, int maxRowLength) {
  }

  public static void main(String[] args) {
    InputMatrix matrix;

    Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    try {
      matrix = parseNumbers(scanner);
    } catch (IOException exc) {
      System.err.println("Couldn't read from the input.");
      return;
    }

    processMatrix(matrix);
    printMatrix(matrix.numbers);
  }

  private static InputMatrix parseNumbers(final Scanner scanner) throws IOException {
    List<int[]> numbers = new ArrayList<>();
    int maxRowLength = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine(); // memory

      int[] row = parseSingleLine(line);
      numbers.add(row);

      if (row.length > maxRowLength) {
        maxRowLength = row.length;
      }
    }

    // No need to pre-allocate any space
    return new InputMatrix(numbers.toArray(new int[0][]), maxRowLength);
  }

  private static int[] parseSingleLine(final String line) throws IOException {
    int length = 0;
    int[] numbers = new int[1];

    try (Scanner scanner = new Scanner(new StringReader(line), FastReverse3839::isTokenPart)) {
      while (scanner.hasNext()) {
        String token = scanner.next();
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

  private static void processMatrix(InputMatrix matrix) {
    // NOTE: O(n^2) avoided
    int[] columnSums = new int[matrix.maxRowLength];

    for (int i = 0; i < matrix.numbers.length; ++i) {
      int[] row = matrix.numbers[i];

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
