package intro.java.markup;

public interface BBCodeSerializable {
  /**
   * Appends bbcode serialization to the <code>dst</code> buffer.
   */
  public void toBBCode(StringBuilder dst);
}
