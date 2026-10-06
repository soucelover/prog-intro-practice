package intro.java.my_scanner;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

// UTF-8 is used with all constructors explicitly
public class Scanner implements Closeable, Iterator<String> {
  // Fields related to the characters source
  private final Reader reader;
  private boolean closed = false;
  private boolean readerFinished = false;

  private IOException lastException;

  // Buffers
  private static final int BUFFER_SIZE = 1024;

  private char[] buffer;

  /**
   * Position in the buffer from which all the parsing operations are performed.
   * Characters in buffer before <code>position</code> are never reused.
   */
  private int position = 0;
  /**
   * The end of the buffer part with read characters.
   */
  private int end = 0;

  public Scanner(Reader reader) {
    Objects.requireNonNull(reader);

    this.reader = reader;

    buffer = new char[BUFFER_SIZE];
  }

  public Scanner(String source) {
    this(new StringReader(source));
  }

  public Scanner(InputStream source) {
    this(new InputStreamReader(source, StandardCharsets.UTF_8));
  }

  public Scanner(File file) throws IOException {
    this(new FileReader(file));
  }

  public void close() throws IOException {
    if (closed) {
      return;
    }

    reader.close();
    closed = true;
  }

  // General internal helpers

  private void ensureOpen() {
    if (closed) {
      throw new IllegalStateException("Performing a read operation on an already closed scanner.");
    }
  }

  private boolean readInput() {
    if (end == buffer.length) {
      increaseBuffer();
    }

    try {
      int charsRead = reader.read(buffer, end, buffer.length - end);

      if (charsRead == -1) {
        readerFinished = true;
        return false;
      }

      end += charsRead;
    } catch (IOException exc) {
      // exceptions ignored
      readerFinished = true;
      lastException = exc;
      return false;
    }

    return true;
  }

  private void increaseBuffer() {
    if (position > 0) {
      // StringBuilder?
      // Reclaim space if possible
      System.arraycopy(buffer, position, buffer, 0, end - position);
      end -= position;
      position = 0;
      return;
    }

    buffer = Arrays.copyOf(buffer, buffer.length * 2);
  }

  // Token Parsing

  @FunctionalInterface
  public interface CharPredicate {
    boolean test(char arg);
  }

  public static boolean characterIsTokenPart(char character) {
    return !Character.isWhitespace(character);
  }

  /**
   * How many characters to skip before next token occurs?
   */
  private int nextSkippedCount(CharPredicate predicate) {
    // assume closed == true and can get more than one character
    int count = 0;

    while (true) {
      if (position + count == end && !readInput() || predicate.test(buffer[position + count])) {
        return count;
      }

      ++count;
    }
  }

  public boolean hasNext(CharPredicate predicate) {
    ensureOpen();

    final int startOffset = nextSkippedCount(predicate);

    if (position + startOffset == end && !readInput()) {
      return false;
    }

    return predicate.test(buffer[position + startOffset]);
  }

  public boolean hasNext() {
    return hasNext(Scanner::characterIsTokenPart);
  }

  private record TokenInfo(int startOffset, int length) {
    private int totalOffset() {
      return startOffset + length;
    }
  }

  private String getTokenString(TokenInfo token) {
    return new String(buffer, position + token.startOffset, token.length);
  }

  private TokenInfo findNextToken(CharPredicate predicate) {
    // assume closed == true and can get more than one character
    final int startOffset = nextSkippedCount(predicate);
    int pos = position + startOffset;
    int length = 0;

    while (true) {
      if (pos + length == end) {
        if (!readInput()) {
          return new TokenInfo(startOffset, length);
        }

        pos = position + startOffset; // position might be changed
      }

      if (!predicate.test(buffer[pos + length])) {
        return new TokenInfo(startOffset, length);
      }

      ++length;
    }
  }

  public String next(CharPredicate predicate) {
    ensureOpen();

    if (position == end && readerFinished) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    TokenInfo tokenInfo = findNextToken(predicate);

    if (tokenInfo.length == 0) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    String token = getTokenString(tokenInfo);
    position += tokenInfo.totalOffset();

    return token;
  }

  public String next() {
    return next(Scanner::characterIsTokenPart);
  }

  // Integer parsing

  public boolean hasNextInt() {
    ensureOpen();

    TokenInfo token = findNextToken(Scanner::characterIsTokenPart);

    if (token.length == 0) {
      return false;
    }

    try {
      Integer.parseInt(getTokenString(token));
      return true;
    } catch (NumberFormatException exc) {
      return false;
    }
  }

  public int nextInt() {
    ensureOpen();

    if (position == end && readerFinished) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    TokenInfo token = findNextToken(Scanner::characterIsTokenPart);

    if (token.length == 0) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    try {
      int value = Integer.parseInt(getTokenString(token));

      position += token.totalOffset();
      return value;
    } catch (NumberFormatException exc) {
      throw new InputMismatchException("Couldn't convert token to int.");
    }
  }

  // Line parsing

  public boolean hasNextLine() {
    ensureOpen();

    if (position < end) {
      return true;
    }

    return !readerFinished && readInput();
  }

  private int findLineSeparatorLength(int offset) {
    // Assume position + offset < end
    switch (buffer[position + offset]) {
      case '\n':
      case '\u0085':
      case '\u2028':
      case '\u2029':
        return 1;
      case '\r':
        ++offset;

        if ((position + offset == end && !readInput())) {
          return 1;
        }

        if (buffer[position + offset] == '\n') {
          return 2;
        }

        return 1;
      default:
        return 0;
    }
  }

  private record LineInfo(int length, int separatorLength) {
    int fullLength() {
      return length + separatorLength;
    }
  }

  private LineInfo findNextLine() {
    // assume closed == false and no EOF or IOException occured
    int length = 0;

    while (true) {
      if (position + length == end && !readInput()) {
        if (length == 0) {
          return null;
        }

        return new LineInfo(length, 0);
      }

      int separatorLength = findLineSeparatorLength(length);

      if (separatorLength > 0) {
        return new LineInfo(length, separatorLength);
      }

      ++length;
    }
  }

  public String nextLine() {
    ensureOpen();

    if (position == end && readerFinished) {
      throw new NoSuchElementException("No more lines to read.");
    }

    LineInfo lineInfo = findNextLine();

    if (lineInfo == null) {
      throw new NoSuchElementException("No more lines to read.");
    }

    String line = new String(buffer, position, lineInfo.length);

    position += lineInfo.fullLength();
    return line;
  }
}
