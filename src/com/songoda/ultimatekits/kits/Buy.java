package com.songoda.ultimatekits.kits;

import com.songoda.arconix.Arconix;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.utils.Debugger;
import com.songoda.ultimatekits.utils.Methods;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 2/24/2017.
 */
public class Buy {


    public static void confirmBuy(String kitName, Player p) {
        try {
            String cost = Double.toString(UltimateKits.getInstance().getConfig().getDouble("data.kit." + kitName + ".eco"));

            Kit kit = UltimateKits.getInstance().getKitManager().getKit(kitName);
            if (kit.hasPermission(p) && UltimateKits.getInstance().getConfig().getBoolean("Main.Allow Players To Receive Kits For Free If They Have Permission")) {
                cost = "0";
            }
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle(Lang.GUI_TITLE_YESNO.getConfigValue(cost)));

            String title = "§c" + StringUtils.capitalize(kitName.toLowerCase());
            ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
            if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kitName + ".displayitemkits") != null) {
                item = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kitName + ".displayitemkits").clone();
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(title);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§a$" + Arconix.pl().format().formatEconomy(Double.parseDouble(cost)));


            int nu = 0;
            while (nu != 27) {
                i.setItem(nu, Methods.getGlass());
                nu++;
            }

            i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(8, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));
            i.setItem(10, Methods.getBackgroundGlass(false));
            i.setItem(16, Methods.getBackgroundGlass(false));
            i.setItem(17, Methods.getBackgroundGlass(true));
            i.setItem(18, Methods.getBackgroundGlass(true));
            i.setItem(19, Methods.getBackgroundGlass(true));
            i.setItem(20, Methods.getBackgroundGlass(false));
            i.setItem(24, Methods.getBackgroundGlass(false));
            i.setItem(25, Methods.getBackgroundGlass(true));
            i.setItem(26, Methods.getBackgroundGlass(true));

            ItemStack item2 = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Buy Icon")), 1);
            ItemMeta itemmeta2 = item2.getItemMeta();
            itemmeta2.setDisplayName(Lang.YES_GUI.getConfigValue());
            item2.setItemMeta(itemmeta2);

            ItemStack item3 = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta itemmeta3 = item3.getItemMeta();
            itemmeta3.setDisplayName(Lang.NO_GUI.getConfigValue());
            item3.setItemMeta(itemmeta3);

            i.setItem(4, item);
            i.setItem(11, item2);
            i.setItem(15, item3);

            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
            p.openInventory(i);
            UltimateKits.getInstance().buy.put(p.getUniqueId(), kitName);
                }, 1);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}
