package intro.java.reverse;

import java.util.ArrayList;
import java.util.Scanner;

public class Reverse {
  public static void main(String[] args) {
    ArrayList<int[]> numbers = parseNumbers();

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

  private static ArrayList<int[]> parseNumbers() {
    Scanner scanner = new Scanner(System.in);

    ArrayList<int[]> numbers = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      int[] row = parseSingleLine(line);
      numbers.add(row);
    }

    return numbers;
  }

  private static int[] parseSingleLine(String line) {
    ArrayList<Integer> numbers = new ArrayList<>();

    try (Scanner scanner = new Scanner(line)) {
      while (scanner.hasNext()) {
        numbers.add(scanner.nextInt());
      }
    }

    // Convert into primitive array to reduce memory usage
    int[] result = new int[numbers.size()];

    for (int i = 0; i < result.length; ++i) {
      result[i] = numbers.get(i);
    }

    return result;
  }
}
