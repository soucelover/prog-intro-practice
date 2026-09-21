package intro.java.reverse;

import java.util.ArrayList;
import java.util.Scanner;

public class Reverse {
  public static void main(String[] args) {
    ArrayList<int[]> numbers = parseNumbers();

    for (int i = numbers.size() - 1; i >= 0; --i) {
      for (int j = numbers.get(i).length - 1; j >= 0; --j) {
        System.out.println(numbers.get(i)[j]);
      }
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

    // TODO: Write number parsing algorithm

    // Convert into primitive array to reduce memory usage
    int[] result = new int[numbers.size()];

    for (int i = 0; i < result.length; ++i) {
      result[i] = numbers.get(i);
    }

    return result;
  }
}
