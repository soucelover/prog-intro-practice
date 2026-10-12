package intro.java.wordstat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import intro.java.myscanner.Scanner;

public class WordStat3839 {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    Map<String, Integer> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static Map<String, Integer> analyzeFile(File inputPath)
      throws IOException {
    HashMap<String, Integer> wordStats = new HashMap<>();

    try (Scanner scanner = new Scanner(new FileReader(inputPath, StandardCharsets.UTF_8),
        WordStat3839::characterIsWordPart)) {
      while (scanner.hasNext()) {
        countWordIn(wordStats, scanner.next());
      }
    }

    return wordStats;
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  private static void countWordIn(Map<String, Integer> wordStats, String word) {
    word = word.toLowerCase(Locale.ROOT);

    if (word.length() < 3) {
      countShingleIn(wordStats, word);
      return;
    }

    int end = word.length() - 2;

    for (int i = 0; i < end; ++i) {
      countShingleIn(wordStats, word.substring(i, i + 3));
    }
  }

  private static void countShingleIn(Map<String, Integer> wordStats, String shingle) {
    Integer count = wordStats.get(shingle);

    if (count == null) {
      wordStats.put(shingle, 1);
    } else {
      wordStats.put(shingle, count + 1);
    }
  }

  private static void outputWordStats(Map<String, Integer> wordStats, File outputPath)
      throws IOException {
    ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(wordStats.entrySet());

    entries.sort((left, right) -> {
      int comp = Integer.compare(left.getValue(), right.getValue());

      if (comp != 0) {
        return comp;
      }

      return left.getKey().compareTo(right.getKey());
    });

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, Integer> entry : entries) {
        writer.write(entry.getKey() + " " + entry.getValue());
        writer.newLine();
      }
    }
  }
}
