package intro.java.markup;

import java.util.SequencedCollection;

public class Strong extends MarkupElement {
  static private final String DELIMITER = "__";
  static private final String BBCODE_TAG = "b";

  public Strong(InlineElement item) {
    super(item, DELIMITER, BBCODE_TAG);
  }

  public Strong(SequencedCollection<InlineElement> items) {
    super(items, DELIMITER, BBCODE_TAG);
  }
}
