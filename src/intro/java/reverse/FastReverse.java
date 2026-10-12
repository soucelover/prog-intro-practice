package intro.java.reverse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import intro.java.common.IntList;
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

      row.add(Integer.parseInt(token));
    }

    if (row.size() > 0) {
      numbers.add(row.toArray());

      if (row.size() > maxRowSize) {
        maxRowSize = row.size();
      }

      scanner.consumeLineBreak();
    }

    return numbers;
  }
}
