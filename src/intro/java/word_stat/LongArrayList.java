package intro.java.word_stat;

import java.util.Arrays;

public class LongArrayList {
  private long[] array;
  private int length;

  LongArrayList() {
    array = new long[1];
    length = 0;
  }

  void add(long value) {
    if (length == array.length) {
      int newLength = Math.max(length + 1, (array.length * 3) / 2);
      array = Arrays.copyOf(array, newLength);
    }

    array[length] = value;
    ++length;
  }

  long[] toArray() {
    if (array.length == length) {
      return array;
    }

    return Arrays.copyOf(array, length);
  }
}
