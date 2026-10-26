package intro.java.markup;

import java.util.List;

public class Emphasis extends MarkupElement {
  static private final String DELIMITER = "*";
  static private final String BBCODE_TAG = "i";

  public Emphasis(InlineElement item) {
    super(item, DELIMITER, BBCODE_TAG);
  }

  public Emphasis(List<InlineElement> items) {
    super(items, DELIMITER, BBCODE_TAG);
  }
}
