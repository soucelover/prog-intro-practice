package intro.java.markup;

public interface MarkdownSerializable {
  /**
   * Appends markdown code to the <code>dst</code> buffer.
   */
  public void toMarkdown(StringBuilder dst);
}
