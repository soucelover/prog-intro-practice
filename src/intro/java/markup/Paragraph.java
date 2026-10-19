package intro.java.markup;

import java.util.List;
import java.util.SequencedCollection;

// Immutable
public class Paragraph implements MarkdownSerializable {
  private SequencedCollection<MarkdownSerializable> elements;

  public Paragraph() {
    elements = List.of();
  }

  public Paragraph(SequencedCollection<MarkdownSerializable> elements) {
    this.elements = elements;
  }

  @Override
  public void toMarkdown(StringBuilder dst) {
    for (MarkdownSerializable element : elements) {
      element.toMarkdown(dst);
    }
  }
}
