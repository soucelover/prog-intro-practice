package intro.java.markup;

import java.util.SequencedCollection;

public class Strong extends MarkupElement {
  public Strong(InlineElement item) {
    super(item, "__");
  }

  public Strong(SequencedCollection<InlineElement> items) {
    super(items, "__");
  }
}
