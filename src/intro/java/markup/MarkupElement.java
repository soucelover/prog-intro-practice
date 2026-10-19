package intro.java.markup;

import java.util.List;
import java.util.SequencedCollection;

public abstract class MarkupElement implements InlineElement {
  private final List<InlineElement> items;
  private final String delim;

  protected MarkupElement(InlineElement item, String delimiter) {
    this.items = List.of(item);
    this.delim = delimiter;
  }

  protected MarkupElement(SequencedCollection<InlineElement> items, String delimiter) {
    this.items = List.copyOf(items);
    this.delim = delimiter;
  }

  @Override
  public void toMarkdown(StringBuilder dst) {
    dst.append(delim);

    for (InlineElement item : items) {
      item.toMarkdown(dst);
    }

    dst.append(delim);
  }
}
