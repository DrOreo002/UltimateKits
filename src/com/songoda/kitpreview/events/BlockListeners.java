package com.songoda.kitpreview.events;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.kits.Kit;
import com.songoda.kitpreview.utils.Debugger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by songoda on 2/24/2017.
 */
public class BlockListeners implements Listener {

    private final KitPreview instance;

    public BlockListeners(KitPreview instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            Block b = event.getBlock();
            String loc = Arconix.pl().serialize().serializeLocation(b);
            if (instance.getConfig().getString("data.block." + loc) == null) return;
            Kit kit = new Kit(b);
            Player p = event.getPlayer();
            kit.removeKitFromBlock(p);

        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        try {
            Block b = e.getBlockAgainst();
            String loc = Arconix.pl().serialize().serializeLocation(b);
            if (instance.getConfig().getString("data.block." + loc) != null) {
                e.setCancelled(true);
            }

        } catch (Exception ee) {
            Debugger.runReport(ee);
        }
    }
}
