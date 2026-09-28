package intro.java.word_stat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedMap;

public class WordStat {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    SequencedMap<String, Integer> wordStats;

    try {
      wordStats = analyzeFile(inputPath);
    } catch (IOException exc) {
      System.out.println(exc.getLocalizedMessage());
      return;
    }

    outputWordStats(wordStats, outputPath);
  }

  private static int READER_BUFFER_SIZE = 256;

  public static LinkedHashMap<String, Integer> analyzeFile(File inputPath)
      throws IOException {
    LinkedHashMap<String, Integer> wordStats = new LinkedHashMap<>();

    try (Reader reader = new FileReader(inputPath, StandardCharsets.UTF_8)) {
      char[] buf = new char[READER_BUFFER_SIZE];
      int charsRead;

      StringBuilder word = new StringBuilder();
      boolean pointingAtWord = false;
      int wordPartStart = 0;

      while ((charsRead = reader.read(buf, 0, READER_BUFFER_SIZE)) != -1) {
        for (int i = 0; i < charsRead; ++i) {
          // Word end or between words
          if (!characterIsWordPart(buf[i])) {
            if (!pointingAtWord) {
              continue;
            }

            if (i != 0) {
              word.append(buf, wordPartStart, i - wordPartStart);
            }

            wordStats.compute(word.toString(), (k, v) -> (v == null) ? 0 : v + 1);
          }

          if (!pointingAtWord) {
            wordPartStart = i;
            pointingAtWord = true;
          } else if (i == 0) { // After new buffer read
            wordPartStart = 0;
          }
        }

        if (pointingAtWord) {
          word.append(buf, wordPartStart, READER_BUFFER_SIZE - wordPartStart);
        }
      }
    }

    return wordStats;
  }

  private static boolean characterIsWordPart(char character) {
    return Character.isLetter(character)
        || Character.getType(character) == Character.DASH_PUNCTUATION
        || character == '\'';
  }

  public static void outputWordStats(SequencedMap<String, Integer> wordStats, File outputPath) {
    for (Map.Entry<String, Integer> entry : wordStats.sequencedEntrySet()) {
      System.out.println(entry.getKey() + " " + entry.getValue());
    }
  }
}
