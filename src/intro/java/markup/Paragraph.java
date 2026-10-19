package intro.java.markup;

import java.util.List;
import java.util.SequencedCollection;

// Immutable
public class Paragraph implements MarkdownSerializable {
  private SequencedCollection<InlineElement> elements;

  public Paragraph() {
    elements = List.of();
  }

  public Paragraph(SequencedCollection<InlineElement> elements) {
    this.elements = elements;
  }

  @Override
  public void toMarkdown(StringBuilder dst) {
    for (InlineElement element : elements) {
      element.toMarkdown(dst);
    }
  }
}
