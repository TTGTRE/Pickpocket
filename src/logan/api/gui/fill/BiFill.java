package logan.api.gui.fill;

import logan.api.gui.Menu;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Tre Logan
 */
public class BiFill implements Filler {

    private Material fillMaterial1;
    private Material fillMaterial2;

    public BiFill(Material fillMaterial1, Material fillMaterial2) {
        this.fillMaterial1 = fillMaterial1;
        this.fillMaterial2 = fillMaterial2;
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
        FillPlacer fillPlacer = new FillPlacer(fillMaterial1);
        fillPlacer.placeIntermittently(menu, 0, menu.getSize() - 1, 2, slots, mode);

        fillPlacer.setFill(fillMaterial2);
        fillPlacer.placeIntermittently(menu, 1, menu.getSize() - 1, 2, slots, mode);
    }
}
