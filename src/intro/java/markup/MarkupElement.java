package intro.java.markup;

import java.util.List;
import java.util.SequencedCollection;

public abstract class MarkupElement implements InlineElement {
  private final List<InlineElement> items;

  private final String mdDelim;

  private final String bbCodeTag;

  protected MarkupElement(InlineElement item, String mdDelimiter, String bbCodeTag) {
    this.items = List.of(item);

    this.mdDelim = mdDelimiter;
    this.bbCodeTag = bbCodeTag;
  }

  protected MarkupElement(SequencedCollection<InlineElement> items, String mdDelimiter, String bbCodeTag) {
    this.items = List.copyOf(items);

    this.mdDelim = mdDelimiter;
    this.bbCodeTag = bbCodeTag;
  }

  @Override
  public void toMarkdown(StringBuilder dst) {
    dst.append(mdDelim);

    for (InlineElement item : items) {
      item.toMarkdown(dst);
    }

    dst.append(mdDelim);
  }

  @Override
  public void toBBCode(StringBuilder dst) {
    dst.append("[");
    dst.append(bbCodeTag);
    dst.append("]");

    for (InlineElement item : items) {
      item.toBBCode(dst);
    }

    dst.append("[");
    dst.append(bbCodeTag);
    dst.append("/]");
  }
}
