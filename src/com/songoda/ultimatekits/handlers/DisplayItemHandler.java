package com.songoda.ultimatekits.handlers;

import com.songoda.arconix.Arconix;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.kits.object.KitBlockData;
import com.songoda.ultimatekits.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songoda on 2/24/2017.
 */
public class DisplayItemHandler {

    private final UltimateKits instance;

    public DisplayItemHandler(UltimateKits instance) {
        this.instance = instance;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(UltimateKits.getInstance(), () -> displayItems(), 30L, 30L);
    }


    private void displayItems() {
        try {
            loop:
            for (KitBlockData kitBlockData : instance.getKitManager().getKitLocations().values()) {
                displayItem(kitBlockData);
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
    public void displayItem(KitBlockData kitBlockData) {
        Location location = kitBlockData.getLocation();
        location.add(0.5, 0, 0.5);

        Kit kit = kitBlockData.getKit();

        List<ItemStack> list = kit.getReadableContents(null, false);
        for (Entity e : location.getChunk().getEntities()) {
            if (e.getType() != EntityType.DROPPED_ITEM
                    || e.getLocation().getX() != location.getX()
                    || e.getLocation().getZ() != location.getZ()) {
                continue;
            }
            Item i = (Item) e;
            if (i.getItemStack().getItemMeta().getDisplayName() == null) {
                i.remove();
                return;
            }
            int inum = Integer.parseInt(i.getItemStack().getItemMeta().getDisplayName()) + 1;
            if (inum > list.size()) inum = 1;

            ItemStack is = list.get(inum - 1);
            if (instance.getConfig().getItemStack("data.kit." + kit + ".displayitem") != null) {
                is = instance.getConfig().getItemStack("data.kit." + kit + ".displayitem").clone();
            }
            ItemMeta meta = is.getItemMeta();
            is.setAmount(1);
            meta.setDisplayName(Integer.toString(inum));
            is.setItemMeta(meta);
            i.setItemStack(is);
            i.setPickupDelay(9999);
            return;
        }
        if (!kitBlockData.isDisplayingItems()) return;

        ItemStack is = list.get(0);
        is.setAmount(1);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName("0");
        is.setItemMeta(meta);
        Item i = location.getWorld().dropItem(location.add(0, 1, 0), list.get(0));
        Vector vec = new Vector(0, 0, 0);
        i.setVelocity(vec);
        i.setPickupDelay(9999);
        i.setMetadata("displayItem", new FixedMetadataValue(UltimateKits.getInstance(), true));
        i.setMetadata("betterdrops_ignore", new FixedMetadataValue(UltimateKits.getInstance(), true));
    }
}
