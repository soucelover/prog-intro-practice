package intro.java.markup;

import java.util.SequencedCollection;

public class Strikeout extends MarkupElement {
  static private final String DELIMITER = "~";
  static private final String BBCODE_TAG = "s";

  public Strikeout(InlineElement item) {
    super(item, DELIMITER, BBCODE_TAG);
  }

  public Strikeout(SequencedCollection<InlineElement> items) {
    super(items, DELIMITER, BBCODE_TAG);
  }
}
