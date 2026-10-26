package intro.java.markup;

import java.util.List;

public class ListItem {
    private final List<BBCodeContainer> content;

    public ListItem(List<BBCodeContainer> items) {
        this.content = List.copyOf(items);
    }

    public void toBBCode(StringBuilder dst) {
        dst.append("[*]");
        for (BBCodeContainer item : content) {
            item.toBBCode(dst);
        }
    }
}
