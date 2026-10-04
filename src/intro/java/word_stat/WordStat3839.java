package intro.java.word_stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import base.Pair;

public class WordStat3839 {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    List<String> shingles;

    try {
      shingles = readFile(inputPath);

      List<Pair<String, Integer>> wordStats = analyzeShingles(shingles);
      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static final int READER_BUFFER_SIZE = 8192;

  private static List<String> readFile(File inputPath)
      throws IOException {
    ArrayList<String> shingles = new ArrayList<>();

    try (Reader reader = new FileReader(inputPath, StandardCharsets.UTF_8)) {
      char[] buf = new char[READER_BUFFER_SIZE];
      int charsRead;

      StringBuilder word = new StringBuilder();
      boolean pointingAtWord = false;
      int wordPartStart = 0;

      while ((charsRead = reader.read(buf)) != -1) {
        for (int i = 0; i < charsRead; ++i) {
          // Word end or between words
          if (!characterIsWordPart(buf[i])) {
            if (!pointingAtWord) {
              continue;
            }

            pointingAtWord = false;

            if (i != 0) {
              word.append(buf, wordPartStart, i - wordPartStart);
            }

            addWord(shingles, word);
            word.setLength(0);
            continue;
          }

          if (!pointingAtWord) {
            wordPartStart = i;
            pointingAtWord = true;
          } else if (i == 0) { // After new buffer read
            wordPartStart = 0;
          }
        }

        if (pointingAtWord) {
          word.append(buf, wordPartStart, charsRead - wordPartStart);
        }
      }

      if (pointingAtWord) {
        addWord(shingles, word);
      }
    }

    return shingles;
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  private static void addWord(List<String> shingles, StringBuilder word) {
    String wordString = word.toString().toLowerCase(Locale.ROOT);

    if (wordString.length() < 3) {
      shingles.add(wordString);
      return;
    }

    int end = wordString.length() - 2;

    for (int i = 0; i < end; ++i) {
      shingles.add(wordString.substring(i, i + 3));
    }
  }

  private static List<Pair<String, Integer>> analyzeShingles(List<String> shingles) {
    ArrayList<Pair<String, Integer>> result = new ArrayList<>();

    if (shingles.isEmpty()) {
      return result;
    }

    shingles.sort(String::compareTo);
    String shingle = shingles.get(0);
    int count = 1;

    for (int i = 1; i < shingles.size(); ++i) {
      if (!shingle.equals(shingles.get(i))) {
        result.add(Pair.of(shingle, count));
        shingle = shingles.get(i);
        count = 1;
        continue;
      }

      ++count;
    }

    result.add(Pair.of(shingle, count));

    return result;
  }

  private static void outputWordStats(List<Pair<String, Integer>> wordStats, File outputPath)
      throws IOException {
    wordStats.sort((left, right) -> Integer.compare(left.second(), right.second()));

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Pair<String, Integer> entry : wordStats) {
        writer.write(entry.first() + " " + entry.second());
        writer.newLine();
      }
    }
  }
}
