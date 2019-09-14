package io.github.etrayed.icury.video;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.map.MapView;

/**
 * @author Etrayed
 */
public class Canvas {

    final MapView[][] frames = new MapView[][] {new MapView[0]};

    @SuppressWarnings("deprecation")
    public void addFrame(int x, int y, ItemFrame frame) {
        frames[x][y] = Bukkit.getMap(frame.getItem().getDurability());
    }
}
