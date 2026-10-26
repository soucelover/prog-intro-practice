package intro.java.markup;

import java.util.List;

public class OrderedList implements BBCodeContainer {
    private final List<ListItem> items;

    public OrderedList(List<ListItem> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public void toBBCode(StringBuilder dst) {
        dst.append("[list=1]");

        for (ListItem item : items) {
            item.toBBCode(dst);
        }

        dst.append("[/list]");
    }
}
