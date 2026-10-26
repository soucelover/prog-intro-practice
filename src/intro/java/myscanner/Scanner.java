// NOTE: Lowercase only for package names
// https://google.github.io/styleguide/javaguide.html#s5.2.1-package-names
// https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html
package intro.java.myscanner;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;

// UTF-8 is used with all constructors explicitly
public class Scanner implements Closeable {
  private enum State {
    READING,
    EOF,
    FAILED,
    CLOSED;
  }

  // Fields related to the characters source
  private final Reader reader;
  private State state = State.READING;

  // Buffers
  private static final int CHUNK_SIZE = 1024;

  private StringBuilder buffer;

  /**
   * Position in the buffer from which all the parsing operations are performed.
   * Characters in buffer before <code>position</code> are never reused.
   */
  private int position = 0;

  @FunctionalInterface
  public interface CharPredicate {
    boolean test(char arg);
  }

  private final CharPredicate tokenPredicate;

  public Scanner(Reader reader, CharPredicate tokenPredicate) {
    Objects.requireNonNull(reader);

    this.reader = reader;

    buffer = new StringBuilder();
    this.tokenPredicate = tokenPredicate;
  }

  public Scanner(Reader reader) {
    this(reader, Scanner::characterIsTokenPart);
  }

  public void close() throws IOException {
    if (state == State.CLOSED) {
      return;
    }

    reader.close();
    state = State.CLOSED;
  }

  public boolean error() {
    return state == State.FAILED;
  }

  public boolean eof() {
    return state == State.EOF;
  }

  public boolean canRead() {
    return state == State.READING;
  }

  // General internal helpers

  private void ensureGoodState() {
    if (state == State.CLOSED) {
      throw new IllegalStateException("Performing a read operation on an already closed scanner.");
    }

    if (state == State.FAILED) {
      throw new IllegalStateException("Performing a read operation on a scanner in a failed state.");
    }
  }

  private char charAt(int offset) {
    return buffer.charAt(position + offset);
  }

  private void readInput() throws IOException {
    if (position > 0) {
      // Reclaim space
      buffer.delete(0, position);
      position = 0;
    }

    char[] chunk = new char[CHUNK_SIZE];
    final int charsRead;

    try {
      charsRead = reader.read(chunk);
    } catch (IOException exc) {
      state = State.FAILED;
      throw exc;
    }

    if (charsRead == -1) {
      state = State.EOF;
      return;
    }

    buffer.append(chunk, 0, charsRead);
  }

  private boolean ensureAvailable(int offset) throws IOException {
    while (position + offset >= buffer.length()
        && state != State.EOF) {
      readInput();
    }

    return position + offset < buffer.length();
  }

  private boolean ensureAvailable() throws IOException {
    return ensureAvailable(0);
  }

  // Token Parsing

  public static boolean characterIsTokenPart(char character) {
    return !Character.isWhitespace(character);
  }

  /**
   * How many characters to skip before next token occurs?
   */
  private int nextSkippedCount(boolean untilLineBreak) throws IOException {
    // assume state is 'good'
    int count = 0;

    while (true) {
      if (!ensureAvailable(count) || tokenPredicate.test(charAt(count))) {
        return count;
      }

      if (untilLineBreak && findLineBreakLength(count) > 0) {
        return -1;
      }

      ++count;
    }
  }

  public boolean hasNext(boolean untilLineBreak) throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      return false;
    }

    final int startOffset = nextSkippedCount(untilLineBreak);

    if (untilLineBreak && startOffset == -1) {
      return true;
    }

    if (!ensureAvailable(startOffset)) {
      return false;
    }

    return tokenPredicate.test(charAt(startOffset));
  }

  public boolean hasNext() throws IOException {
    return hasNext(false);
  }

  private record TokenInfo(int startOffset, int length) {
    private int totalOffset() {
      return startOffset + length;
    }
  }

  private String getTokenString(TokenInfo token) {
    return buffer.substring(position + token.startOffset, position + token.totalOffset());
  }

  private TokenInfo findNextToken(boolean untilLineBreak) throws IOException {
    // assume state is 'good'
    final int startOffset;

    startOffset = nextSkippedCount(untilLineBreak);

    if (startOffset == -1) {
      return null;
    }

    int length = 0;

    while (true) {
      if (!ensureAvailable(startOffset + length)
          || !tokenPredicate.test(charAt(startOffset + length))) {
        return new TokenInfo(startOffset, length);
      }

      ++length;
    }
  }

  private TokenInfo findNextToken() throws IOException {
    return findNextToken(false);
  }

  public String next(boolean untilLineBreak) throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    TokenInfo tokenInfo = findNextToken(untilLineBreak);

    // Line break was encountered
    if (tokenInfo == null) {
      return null;
    }

    if (tokenInfo.length == 0) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    String token = getTokenString(tokenInfo);
    position += tokenInfo.totalOffset();

    return token;
  }

  public String next() throws IOException {
    return next(false);
  }

  // Integer parsing

  public boolean hasNextInt() throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      return false;
    }

    TokenInfo token = findNextToken();

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

  public int nextInt() throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      throw new NoSuchElementException("No more tokens to read.");
    }

    TokenInfo token = findNextToken();

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

  public boolean hasNextLine() throws IOException {
    ensureGoodState();

    if (position < buffer.length()) {
      return true;
    }

    return ensureAvailable();
  }

  private int findLineBreakLength(int offset) throws IOException {
    // Assume position + offset < buffer.length() and state is 'good'
    char c = buffer.charAt(position + offset);

    if (c == '\n') {
      return 1;
    }

    if (c == '\r') {
      ++offset;

      if (ensureAvailable(offset) && buffer.charAt(position + offset) == '\n') {
        return 2;
      }

      return 1;
    }

    return 0;
  }

  private record LineInfo(int length, int lineBreakLength) {
    int fullLength() {
      return length + lineBreakLength;
    }
  }

  private LineInfo findNextLine() throws IOException {
    // assume state is 'good'
    int length = 0;

    while (true) {
      if (!ensureAvailable(length)) {
        if (length == 0) {
          return null;
        }

        return new LineInfo(length, 0);
      }

      int lineBreakLength = findLineBreakLength(length);

      if (lineBreakLength > 0) {
        return new LineInfo(length, lineBreakLength);
      }

      ++length;
    }
  }

  public String nextLine() throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      throw new NoSuchElementException("No more lines to read.");
    }

    LineInfo lineInfo = findNextLine();

    if (lineInfo == null) {
      throw new NoSuchElementException("No more lines to read.");
    }

    String line = buffer.substring(position, position + lineInfo.length);

    position += lineInfo.fullLength();
    return line;
  }

  public void consumeLineBreak() throws IOException {
    ensureGoodState();

    if (!ensureAvailable()) {
      return;
    }

    int offset = 0;

    while (true) {
      if (!ensureAvailable(offset)) {
        position += offset;
        return;
      }

      int lineBreakLength = findLineBreakLength(offset);

      if (lineBreakLength > 0) {
        position += offset + lineBreakLength;
        return;
      }

      ++offset;
    }
  }
}
