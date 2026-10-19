package intro.java.markup;

import java.util.SequencedCollection;

public class Emphasis extends MarkupElement {
  static private final String DELIMITER = "*";

  public Emphasis(InlineElement item) {
    super(item, DELIMITER);
  }

  public Emphasis(SequencedCollection<InlineElement> items) {
    super(items, DELIMITER);
  }
}
