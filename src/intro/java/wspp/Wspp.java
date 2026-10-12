package intro.java.wspp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;

import intro.java.common.IntList;
import intro.java.myscanner.Scanner;

public class Wspp {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Excepted 2 arguments, got " + args.length);
      return;
    }

    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    SequencedMap<String, IntList> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static SequencedMap<String, IntList> analyzeFile(File inputPath)
      throws IOException {
    LinkedHashMap<String, IntList> wordStats = new LinkedHashMap<>();

    try (Scanner scanner = new Scanner(new FileReader(inputPath, StandardCharsets.UTF_8),
        Wspp::characterIsWordPart)) {
      int no = 1;

      while (scanner.hasNext()) {
        String word = scanner.next().toLowerCase(Locale.ROOT);
        countWordIn(wordStats, word, no++);
      }
    }

    return wordStats;
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  private static void countWordIn(Map<String, IntList> wordStats, String word, int no) {
    if (!wordStats.containsKey(word)) {
      wordStats.put(word, IntList.of(no));
    } else {
      wordStats.get(word).add(no);
    }
  }

  private static void outputWordStats(SequencedMap<String, IntList> wordStats, File outputPath)
      throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, IntList> entry : wordStats.sequencedEntrySet()) {
        IntList occurences = entry.getValue();
        writer.write(entry.getKey() + " " + occurences.size());

        for (int to = occurences.size(), i = 0; i < to; ++i) {
          writer.write(" " + occurences.get(i));
        }

        writer.newLine();
      }
    }
  }
}
