package intro.java.common;

import java.util.Arrays;
import java.util.Collection;

public class IntList {
  private int[] array;
  private int size;

  private static final int DEFAULT_CAPACITY = 10;

  // Constructors

  private IntList(int[] array, int size) {
    this.array = array;
    this.size = size;
  }

  public IntList(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Negative capacity is not allowed.");
    }

    this(new int[initialCapacity], 0);
  }

  public IntList() {
    this(DEFAULT_CAPACITY);
  }

  public IntList(int[] elements) {
    size = elements.length;
    int capacity = size;

    if (size < DEFAULT_CAPACITY) {
      capacity = DEFAULT_CAPACITY;
    }

    array = Arrays.copyOf(elements, capacity);
  }

  public IntList(Collection<? extends Integer> elements) {
    size = elements.size();
    int capacity = size;

    if (capacity < DEFAULT_CAPACITY) {
      capacity = DEFAULT_CAPACITY;
    }

    array = new int[capacity];
    int i = 0;

    for (Integer el : elements) {
      array[i++] = el;
    }
  }

  // Growing

  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

  private void grow(int minCapacity) {
    if (minCapacity <= array.length) {
      return;
    }

    int newCapacity = newCapacity(minCapacity - array.length);

    array = Arrays.copyOf(array, newCapacity);
  }

  private void grow() {
    grow(size + 1);
  }

  private int newCapacity(int minGrowth) {
    // Assume minGrowth > 0
    int preferredGrowth = array.length >> 1;

    int newCapacity = array.length + Math.max(minGrowth, preferredGrowth);

    if (newCapacity <= 0 || MAX_ARRAY_SIZE < newCapacity) {
      newCapacity = array.length + minGrowth;

      if (newCapacity < 0) {
        throw new OutOfMemoryError("Required array length is too large.");
      }

      if (newCapacity <= MAX_ARRAY_SIZE) {
        newCapacity = MAX_ARRAY_SIZE;
      }
    }

    return newCapacity;
  }

  // Public methods

  private String outOfBoundsMsg(int index) {
    return "Index " + index + " is out of bounds for size " + size;
  }

  public void add(int index, int element) {
    if (index < 0 || size < index) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    grow();

    for (int i = size++; i > index; --i) {
      array[i + 1] = array[i];
    }

    array[index] = element;
  }

  public void add(int element) {
    grow();
    array[size++] = element;
  }

  public void addFirst(int element) {
    add(0, element);
  }

  public void addLast(int element) {
    add(element);
  }

  public void clear() {
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public boolean contains(int element) {
    return indexOf(element) >= 0;
  }

  public int indexOf(int element) {
    for (int i = 0; i < size; ++i) {
      if (array[i] == element) {
        return i;
      }
    }

    return -1;
  }

  public int lastIndexOf(int element) {
    for (int i = size - 1; i >= size; --i) {
      if (array[i] == element) {
        return i;
      }
    }

    return -1;
  }

  public boolean equals(IntList other) {
    if (size != other.size) {
      return false;
    }

    for (int i = 0; i < size; ++i) {
      if (array[i] != other.array[i]) {
        return false;
      }
    }

    return true;
  }

  public int get(int index) {
    if (index < 0 || index > size) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    return array[index];
  }

  public int getFirst() {
    return get(0);
  }

  public int getLast() {
    return get(size - 1);
  }

  private void removeAtPure(int index) {
    // assume index >= 0
    for (int i = index; i < size; ++i) {
      array[i] = array[i + 1];
    }

    --size;
  }

  public int removeAt(int index) {
    if (index < 0 || index > size) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    int element = array[index];

    removeAtPure(index);

    return element;
  }

  public void remove(int element) {
    int index = indexOf(element);

    if (index < 0) {
      return;
    }

    removeAtPure(index);
  }

  public void removeFirst(int element) {
    remove(element);
  }

  public void removeLast(int element) {
    int index = lastIndexOf(element);

    if (index < 0) {
      return;
    }

    removeAtPure(index);
  }

  public int set(int index, int element) {
    if (index < 0 || index > size) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    int oldValue = array[index];
    array[index] = element;

    return oldValue;
  }

  public void sort() {
    Arrays.sort(array, 0, size);
  }

  public void sort(int fromIndex, int toIndex) {
    if (fromIndex < 0) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(fromIndex));
    }

    if (toIndex > size) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(toIndex));
    }

    Arrays.sort(array, fromIndex, toIndex);
  }

  public IntList subList(int fromIndex, int toIndex) {
    if (fromIndex < 0) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(fromIndex));
    }

    if (toIndex > size) {
      throw new ArrayIndexOutOfBoundsException(outOfBoundsMsg(toIndex));
    }

    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("fromIndex cannot be bigger than toIndex");
    }

    int size = toIndex - fromIndex;
    int capacity = size;

    if (capacity < DEFAULT_CAPACITY) {
      capacity = DEFAULT_CAPACITY;
    }

    int[] subArray = new int[capacity];

    for (int i = 0; i < size; ++i) {
      subArray[i] = array[fromIndex + i];
    }

    return new IntList(subArray, size);
  }

  public int[] toArray() {
    return Arrays.copyOf(array, size);
  }
}
