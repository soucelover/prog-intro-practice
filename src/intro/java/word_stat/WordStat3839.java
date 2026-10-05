package intro.java.word_stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordStat3839 {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    Map<Long, Integer> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static final int READER_BUFFER_SIZE = 8192;

  private static Map<Long, Integer> analyzeFile(File inputPath)
      throws IOException {
    HashMap<Long, Integer> wordStats = new HashMap<>();

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

            countWordIn(wordStats, word);
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
        countWordIn(wordStats, word);
      }
    }

    return wordStats;
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  /**
   * Counts the word in the words statistics.
   * 
   * Affects content of the passed <code>word</code> parameter. After the call,
   * the object either
   * shouldn't be used, or should be cleared.
   */
  private static void countWordIn(Map<Long, Integer> wordStats, StringBuilder word) {
    for (int i = 0; i < word.length(); ++i) {
      word.setCharAt(i, Character.toLowerCase(word.charAt(i)));
    }

    String wordString = word.toString();

    if (word.length() < 3) {
      countShingleIn(wordStats, getShingle(wordString, 0));
      return;
    }

    int end = word.length() - 2;

    for (int i = 0; i < end; ++i) {
      countShingleIn(wordStats, getShingle(wordString, i));
    }
  }

  private static long getShingle(String word, int start) {
    long shingle = 0;
    int end = Math.min(start + 3, word.length());

    for (int i = start; i < end; ++i) {
      shingle = (shingle << 16) + word.charAt(i);
    }

    shingle <<= 16 * (4 - end + start);
    shingle += end - start;

    return shingle;
  }

  private static String shingleToString(long shingle) {
    char length = (char) shingle;
    shingle >>>= 16 * (4 - length);
    char[] result = new char[length];

    for (int i = length - 1; i >= 0; --i) {
      result[i] = (char) shingle;
      shingle >>>= 16;
    }

    return new String(result);
  }

  private static void countShingleIn(Map<Long, Integer> wordStats, long shingle) {
    Integer count = wordStats.get(shingle);

    if (count == null) {
      wordStats.put(shingle, 1);
    } else {
      wordStats.put(shingle, count + 1);
    }
  }

  private static void outputWordStats(Map<Long, Integer> wordStats, File outputPath)
      throws IOException {
    ArrayList<Map.Entry<Long, Integer>> entries = new ArrayList<>(wordStats.entrySet());

    entries.sort((left, right) -> {
      int comp = Integer.compare(left.getValue(), right.getValue());

      if (comp != 0) {
        return comp;
      }

      return Long.compareUnsigned(left.getKey(), right.getKey());
    });

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<Long, Integer> entry : entries) {
        writer.write(shingleToString(entry.getKey()) + " " + entry.getValue());
        writer.newLine();
      }
    }
  }
}
