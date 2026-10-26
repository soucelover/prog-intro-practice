package intro.java.markup;

import java.util.List;

// Immutable
public class Paragraph implements MarkdownSerializable, BBCodeContainer {
  private final List<InlineElement> elements;

  public Paragraph() {
    elements = List.of();
  }

  public Paragraph(List<InlineElement> elements) {
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
    dst.append("[p]");

    for (InlineElement element : elements) {
      element.toBBCode(dst);
    }

    dst.append("[/p]");
  }
}
