package intro.java.markup;

import java.util.SequencedCollection;

public class Strikeout extends MarkupElement {
  static private final String DELIMITER = "~";

  public Strikeout(InlineElement item) {
    super(item, DELIMITER);
  }

  public Strikeout(SequencedCollection<InlineElement> items) {
    super(items, DELIMITER);
  }
}
