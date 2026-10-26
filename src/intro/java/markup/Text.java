package intro.java.markup;

public class Text implements InlineElement {
  private String text;

  public Text(String text) {
    this.text = text;
  }

  public void toMarkdown(StringBuilder dst) {
    dst.append(text);
  }

  public void toBBCode(StringBuilder dst) {
    dst.append(text);
  }
}
