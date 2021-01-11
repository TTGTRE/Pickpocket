package logan.api.gui.fill;

import logan.api.gui.Menu;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Tre Logan
 */
public class UniFill implements Filler {

    private Material fillMaterial;

    public UniFill(Material fillMaterial) {
        this.fillMaterial = fillMaterial;
    }

    @Override
    public void fill(Menu menu) {
        this.fill(menu, 0, menu.getSize() - 1, Collections.emptyList(), FillPlacer.FillMode.IGNORE);
    }

    @Override
    public void fill(Menu menu, int start, int end) {
        this.fill(menu, start, end, Collections.emptyList(), FillPlacer.FillMode.IGNORE);
    }

    @Override
    public void fill(Menu menu, int start, int end, Collection<Integer> slots, FillPlacer.FillMode mode) {
        FillPlacer fillPlacer = new FillPlacer(fillMaterial);
        fillPlacer.placeIntermittently(menu, start, end, 1, slots, mode);
    }
}
