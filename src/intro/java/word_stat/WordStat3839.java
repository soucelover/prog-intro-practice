package intro.java.word_stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import base.Pair;

public class WordStat3839 {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    try {
      long[] shingles = readFile(inputPath);

      List<Pair<String, Integer>> wordStats = analyzeShingles(shingles);
      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.err.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static final int READER_BUFFER_SIZE = 8192;

  private static long[] readFile(File inputPath)
      throws IOException {
    LongArrayList shingles = new LongArrayList();

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

    return shingles.toArray();
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  private static final long SHINGLE_INVERT_MASK = 0x8000000000000000L;

  private static long getShingle(String word, int start) {
    long shingle = 0;
    int end = Math.min(start + 3, word.length());

    for (int i = start; i < end; ++i) {
      shingle = (shingle << 16) + word.charAt(i);
    }

    shingle <<= 16 * (4 - end + start);
    shingle += end - start;

    return shingle ^ SHINGLE_INVERT_MASK;
  }

  private static String shingleToString(long shingle) {
    shingle ^= SHINGLE_INVERT_MASK;
    char length = (char) shingle;
    shingle >>>= 16 * (4 - length);
    char[] result = new char[length];

    for (int i = length - 1; i >= 0; --i) {
      result[i] = (char) shingle;
      shingle >>>= 16;
    }

    return new String(result);
  }

  /**
   * Counts the word in the words statistics.
   * 
   * Affects content of the passed <code>word</code> parameter. After the call,
   * the object either
   * shouldn't be used, or should be cleared.
   */
  private static void addWord(LongArrayList shingles, StringBuilder word) {
    for (int i = 0; i < word.length(); ++i) {
      word.setCharAt(i, Character.toLowerCase(word.charAt(i)));
    }

    String wordString = word.toString();

    if (wordString.length() < 3) {
      shingles.add(getShingle(wordString, 0));
      return;
    }

    int end = wordString.length() - 2;

    for (int i = 0; i < end; ++i) {
      shingles.add(getShingle(wordString, i));
    }
  }

  private static List<Pair<String, Integer>> analyzeShingles(long[] shingles) {
    ArrayList<Pair<String, Integer>> result = new ArrayList<>();

    if (shingles.length == 0) {
      return result;
    }

    Arrays.sort(shingles);
    long shingle = shingles[0];
    int count = 1;

    for (int i = 1; i < shingles.length; ++i) {
      if (shingle != shingles[i]) {
        result.add(Pair.of(shingleToString(shingle), count));
        shingle = shingles[i];
        count = 1;
        continue;
      }

      ++count;
    }

    result.add(Pair.of(shingleToString(shingle), count));

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
