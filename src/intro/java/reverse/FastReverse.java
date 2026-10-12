package intro.java.reverse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import intro.java.myscanner.Scanner;

public class FastReverse {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    List<int[]> numbers;

    try {
      numbers = parseNumbers(scanner);
    } catch (IOException exc) {
      System.err.println("Couldn't read from the input.");
      return;
    }

    for (int i = numbers.size() - 1; i >= 0; --i) {
      int[] row = numbers.get(i);

      for (int j = row.length - 1; j > 0; --j) {
        System.out.print(row[j] + " ");
      }

      if (row.length > 0) {
        System.out.print(row[0]);
      }

      System.out.println();
    }
  }

  private static List<int[]> parseNumbers(Scanner scanner) throws IOException {
    List<int[]> numbers = new ArrayList<>();
    int[] row = new int[1];
    int rowLength = 0;
    int maxRowLength = 0;

    while (scanner.hasNext(true)) {
      String token = scanner.next(true);

      // Linebreak was uncountered
      if (token == null) {
        numbers.add(trimCappedArray(row, rowLength));

        if (rowLength > maxRowLength) {
          maxRowLength = rowLength;
        }

        row = new int[1];
        rowLength = 0;
        scanner.consumeLineBreak();
        continue;
      }

      int number = Integer.parseInt(token);

      row = addToCappedArray(row, rowLength, number);
      ++rowLength;
    }

    if (rowLength > 0) {
      numbers.add(trimCappedArray(row, rowLength));

      if (rowLength > maxRowLength) {
        maxRowLength = rowLength;
      }

      scanner.consumeLineBreak();
    }

    return numbers;
  }

  // TODO: Implement capped IntArray class
  private static int[] addToCappedArray(int[] array, int length, int item) {
    if (length == array.length) {
      int newLength = Math.max(length + 1, (array.length * 3) / 2);
      array = Arrays.copyOf(array, newLength);
    }

    array[length] = item;
    return array;
  }

  private static int[] trimCappedArray(int[] array, int length) {
    if (array.length == length) {
      return array;
    }

    return Arrays.copyOf(array, length);
  }
}
