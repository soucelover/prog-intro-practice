package intro.java.wspp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;
import java.util.TreeMap;

import intro.java.common.IntList;
import intro.java.myscanner.Scanner;

public class Wspp3839 {
  private static class Counter {
    public int count = 1;
    public IntList occurences;
    public int lastLineNo;
    public int noInLine = 1;

    public Counter(int lineNo) {
      occurences = new IntList();
      lastLineNo = lineNo;
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Excepted 2 arguments, got " + args.length);
      return;
    }

    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    SequencedMap<String, Counter> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static SequencedMap<String, Counter> analyzeFile(File inputPath)
      throws IOException {
    SequencedMap<String, Counter> wordStats = new TreeMap<>((left, right) -> -left.compareTo(right));

    try (Scanner scanner = new Scanner(new FileReader(inputPath, StandardCharsets.UTF_8),
        Wspp3839::characterIsWordPart)) {
      int no = 1;
      int lineNo = 0;

      while (scanner.hasNext(true)) {
        String word = scanner.next(true);

        if (word == null) {
          ++lineNo;
          no = 1;
          scanner.consumeLineBreak();
          continue;
        }

        word = word.toLowerCase(Locale.ROOT);
        countWordIn(wordStats, word, no++, lineNo);
      }
    }

    return wordStats;
  }

  private static boolean characterIsWordPart(char character) {
    int type = Character.getType(character);

    return Character.isLetter(character)
        || type == Character.DASH_PUNCTUATION
        || type == Character.CONNECTOR_PUNCTUATION
        || character == '\'';
  }

  private static void countWordIn(Map<String, Counter> wordStats, String word, int no, int lineNo) {
    Counter pair;

    if (!wordStats.containsKey(word)) {
      wordStats.put(word, new Counter(lineNo));
      return;
    }

    pair = wordStats.get(word);

    if (lineNo != pair.lastLineNo) {
      pair.noInLine = 1;
      pair.lastLineNo = lineNo;
    } else {
      ++pair.noInLine;
    }

    if (pair.noInLine % 2 == 0) {
      pair.occurences.add(no);
    }

    ++pair.count;
  }

  private static void outputWordStats(SequencedMap<String, Counter> wordStats, File outputPath)
      throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, Counter> entry : wordStats.sequencedEntrySet()) {
        Counter pair = entry.getValue();
        IntList occurences = pair.occurences;
        writer.write(entry.getKey() + " " + pair.count);

        for (int to = occurences.size(), i = 0; i < to; ++i) {
          int index = occurences.get(i);

          writer.write(" " + index);
        }

        writer.newLine();
      }
    }
  }
}
