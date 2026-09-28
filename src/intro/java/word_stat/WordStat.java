package intro.java.word_stat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;

public class WordStat {
  public static void main(String[] args) {
    File inputPath = new File(args[0]);
    File outputPath = new File(args[1]);

    SequencedMap<String, Integer> wordStats;

    try {
      wordStats = analyzeFile(inputPath);

      outputWordStats(wordStats, outputPath);
    } catch (IOException exc) {
      System.out.println(exc.getLocalizedMessage());
      return;
    }
  }

  private static final int READER_BUFFER_SIZE = 8192;

  private static SequencedMap<String, Integer> analyzeFile(File inputPath)
      throws IOException {
    LinkedHashMap<String, Integer> wordStats = new LinkedHashMap<>();

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

  private static void countWordIn(Map<String, Integer> wordStats, StringBuilder word) {
    wordStats.compute(word.toString().toLowerCase(Locale.ROOT), (k, v) -> (v == null) ? 1 : v + 1);
  }

  private static void outputWordStats(SequencedMap<String, Integer> wordStats, File outputPath)
      throws IOException {
    try (Writer writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      for (Map.Entry<String, Integer> entry : wordStats.sequencedEntrySet()) {
        writer.write(entry.getKey() + " " + entry.getValue() + "\n");
      }
    }
  }
}
