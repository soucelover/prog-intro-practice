package intro.java.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Reverse3839 {
  public static void main(String[] args) {
    int[][] numbers;

    try (Scanner scanner = new Scanner(System.in)) {
      numbers = parseNumbers(scanner);
    }

    processMatrix(numbers);
    printMatrix(numbers);
  }

  private static int[][] parseNumbers(final Scanner scanner) {
    ArrayList<int[]> numbers = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      int[] row = parseSingleLine(line);
      numbers.add(row);
    }

    // No need to pre-allocate any space
    return numbers.toArray(new int[0][]);
  }

  private static int[] parseSingleLine(final String line) {
    int length = 0;
    int[] numbers = new int[1]; // :NOTE: memory usage

    try (Scanner scanner = new Scanner(line)) {
      while (scanner.hasNext()) {
        String token = scanner.next();
        int number = Integer.parseUnsignedInt(token, 16);
        
        numbers = addToCappedArray(numbers, length, number);
        ++length;
      }
    }
    
    return Arrays.copyOf(numbers, length);
  }

  private static int[] addToCappedArray(int[] array, int length, int item) {
    if (length + 1 >= array.length) {
      array = Arrays.copyOf(array, (length * 3) / 2);
    }

    array[length] = item;
    return array;
  }

  private static void processMatrix(int[][] numbers) {
    int[] columnSums = new int[0]; // :NOTE: naming

    for (int i = 0; i < numbers.length; ++i) {
      int[] row = numbers[i];

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
        System.out.print(Integer.toHexString(row[j]) + " ");
      }

      if (row.length > 0) {
        System.out.print(Integer.toHexString(row[row.length - 1]));
      }

      System.out.println();
    }
  }
}
