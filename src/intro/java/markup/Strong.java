package intro.java.markup;

import java.util.List;

public class Strong extends MarkupElement {
  static private final String DELIMITER = "__";
  static private final String BBCODE_TAG = "b";

  public Strong(InlineElement item) {
    super(item, DELIMITER, BBCODE_TAG);
  }

  public Strong(List<InlineElement> items) {
    super(items, DELIMITER, BBCODE_TAG);
  }
}
