package intro.java.reverse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import intro.java.common.IntList;
import intro.java.myscanner.Scanner;

public class FastReverse3839 {
  record InputMatrix(int[][] numbers, int maxRowLength) {
  }

  public static void main(String[] args) {
    InputMatrix matrix;

    Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8),
        FastReverse3839::isTokenPart);

    try {
      matrix = parseNumbers(scanner);
    } catch (IOException exc) {
      System.err.println("Couldn't read from the input.");
      return;
    }

    processMatrix(matrix);
    printMatrix(matrix.numbers);
  }

  private static final int[] EMPTY_INT_ARRAY = new int[0];

  private static InputMatrix parseNumbers(final Scanner scanner) throws IOException {
    // NOTE: Memory issue with per-line reading avoided
    List<int[]> numbers = new ArrayList<>();
    IntList row = new IntList();
    int maxRowSize = 0;

    while (scanner.hasNext(true)) {
      String token = scanner.next(true);

      // Linebreak was uncountered
      if (token == null) {
        numbers.add(row.toArray());

        if (row.size() > maxRowSize) {
          maxRowSize = row.size();
        }

        row.clear();
        scanner.consumeLineBreak();
        continue;
      }

      row.add(Math.abs(parseDecHexInt(token)));
    }

    if (row.size() > 0) {
      numbers.add(row.toArray());

      if (row.size() > maxRowSize) {
        maxRowSize = row.size();
      }

      scanner.consumeLineBreak();
    }

    // Empty lines at the end of the input
    while (scanner.hasNextLine()) {
      numbers.add(EMPTY_INT_ARRAY);
      scanner.consumeLineBreak();
    }

    // No need to pre-allocate any space
    return new InputMatrix(numbers.toArray(new int[0][]), maxRowSize);
  }

  private static int parseDecHexInt(String token) {
    int offset = 0;

    if (token.startsWith("0x", offset) || token.startsWith("0X", offset)) {
      offset = 2;
    } else if (token.startsWith("0", offset) && token.length() > 1 + offset) {
      offset = 1;
    } else { // Decimal
      return Integer.parseInt(token, 10);
    }

    return Integer.parseUnsignedInt(token, offset, token.length(), 16);
  }

  private static boolean isTokenPart(char character) {
    int type = Character.getType(character);
    return !Character.isWhitespace(character)
        && type != Character.START_PUNCTUATION && type != Character.END_PUNCTUATION;
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
