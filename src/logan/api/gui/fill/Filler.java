package logan.api.gui.fill;

import logan.api.gui.Menu;

import java.util.Collection;

public interface Filler {

    void fill(Menu menu);

    void fill(Menu menu, int start, int end);

    void fill(Menu menu, int start, int end, Collection<Integer> slots, FillPlacer.FillMode mode);
}
