package intro.java.word_stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;

import intro.java.my_scanner.Scanner;

public class WordStat {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    SequencedMap<String, Integer> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static SequencedMap<String, Integer> analyzeFile(File inputPath)
      throws IOException {
    LinkedHashMap<String, Integer> wordStats = new LinkedHashMap<>();

    try (Scanner scanner = new Scanner(inputPath)) {
      while (scanner.hasNext(WordStat::characterIsWordPart)) {
        countWordIn(wordStats, scanner.next(WordStat::characterIsWordPart));
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
    wordStats.compute(word.toLowerCase(Locale.ROOT), (k, v) -> (v == null) ? 1 : v + 1);
  }

  private static void outputWordStats(SequencedMap<String, Integer> wordStats, File outputPath)
      throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, Integer> entry : wordStats.sequencedEntrySet()) {
        writer.write(entry.getKey() + " " + entry.getValue());
        writer.newLine();
      }
    }
  }
}
