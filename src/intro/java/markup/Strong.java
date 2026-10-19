package intro.java.markup;

import java.util.SequencedCollection;

public class Strong extends MarkupElement {
  static private final String DELIMITER = "__";

  public Strong(InlineElement item) {
    super(item, DELIMITER);
  }

  public Strong(SequencedCollection<InlineElement> items) {
    super(items, DELIMITER);
  }
}
