package intro.java.markup;

import java.util.SequencedCollection;

public class Strikeout extends MarkupElement {
  public Strikeout(InlineElement item) {
    super(item, "-");
  }

  public Strikeout(SequencedCollection<InlineElement> items) {
    super(items, "-");
  }
}
