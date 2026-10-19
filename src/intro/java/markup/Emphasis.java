package intro.java.markup;

import java.util.SequencedCollection;

public class Emphasis extends MarkupElement {
  public Emphasis(InlineElement item) {
    super(item, "*");
  }

  public Emphasis(SequencedCollection<InlineElement> items) {
    super(items, "*");
  }
}
