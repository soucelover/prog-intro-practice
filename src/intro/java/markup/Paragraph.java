package intro.java.markup;

import java.util.List;
import java.util.SequencedCollection;

// Immutable
public class Paragraph implements MarkdownSerializable, BBCodeSerializable {
  private final SequencedCollection<InlineElement> elements;

  public Paragraph() {
    elements = List.of();
  }

  public Paragraph(SequencedCollection<InlineElement> elements) {
    this.elements = List.copyOf(elements);
  }

  @Override
  public void toMarkdown(StringBuilder dst) {
    for (InlineElement element : elements) {
      element.toMarkdown(dst);
    }
  }

  @Override
  public void toBBCode(StringBuilder dst) {
    for (InlineElement element : elements) {
      element.toBBCode(dst);
    }
  }
}
