package intro.java.wspp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import intro.java.common.IntList;
import intro.java.myscanner.Scanner;

public class Wspp3839 {
  private static class CountingPair {
    public int count;
    public IntList occurences;
    public int lastLineNo;
    public int noInLine = 1;

    public CountingPair(int lineNo) {
      occurences = new IntList();
      count = 1;
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

    Map<String, CountingPair> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static Map<String, CountingPair> analyzeFile(File inputPath)
      throws IOException {
    Map<String, CountingPair> wordStats = new HashMap<>();

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

  private static void countWordIn(Map<String, CountingPair> wordStats, String word, int no, int lineNo) {
    CountingPair pair;

    if (!wordStats.containsKey(word)) {
      wordStats.put(word, new CountingPair(lineNo));
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

  private static void outputWordStats(Map<String, CountingPair> wordStats, File outputPath)
      throws IOException {
    List<Map.Entry<String, CountingPair>> entries = new ArrayList<>(wordStats.entrySet());

    entries.sort((left, right) -> -left.getKey().compareTo(right.getKey()));

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, CountingPair> entry : entries) {
        CountingPair pair = entry.getValue();
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
