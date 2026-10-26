package intro.java.markup;

import java.util.List;

public class UnorderedList implements BBCodeContainer {
    private final List<ListItem> items;

    public UnorderedList(List<ListItem> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public void toBBCode(StringBuilder dst) {
        dst.append("[list]");

        for (ListItem item : items) {
            item.toBBCode(dst);
        }

        dst.append("[/list]");
    }
}
