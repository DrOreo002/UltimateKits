package com.songoda.ultimatekits.kits;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.utils.Debugger;
import com.songoda.ultimatekits.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by songoda on 3/16/2017.
 */
public class KitsGUI {

    public static void show(Player p, int page) {
        try {
            UltimateKits.getInstance().page.put(p.getUniqueId(), page);
            UltimateKits.getInstance().kits.clear();


            String guititle = Lang.KITS_TITLE.getConfigValue();


            ItemStack exit = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            List<String> kitList = new ArrayList<>();

            int ino = 14;
            if (UltimateKits.getInstance().getConfig().getBoolean("Interfaces.Do Not Use Glass Borders")) ino = 54;
            int num = 0;
            int start = (page - 1) * ino;
            int show = 1;
            for (Kit kit : UltimateKits.getInstance().getKitManager().getKits()) {
                if (UltimateKits.getInstance().getConfig().getString("data.kit." + kit.getName() + ".blacklisted") == null
                        && (!UltimateKits.getInstance().getConfig().getBoolean("Main.Only Show Players Kits They Have Permission To Use") || kit.hasPermission(p))
                        && num >= start
                        && show <= ino) {
                    kitList.add(kit.getName());
                    show++;
                }
                num++;
            }

            boolean glassless = UltimateKits.getInstance().getConfig().getBoolean("Interfaces.Do Not Use Glass Borders");

            int n = 7;
            if (glassless)
                n = 9;
            int max = 27;
            if (kitList.size() > n) {
                max = 36;
            }
            if (glassless) {
                if (kitList.size() > n + 34)
                    max = max + 34;
                else if (kitList.size() > n + 25)
                    max = max + 25;
                else if (kitList.size() > n + 16)
                    max = max + 16;
                else if (kitList.size() > n + 7)
                    max = max + 7;
            }
            if (glassless) max -= 18;
            Inventory i = Bukkit.createInventory(null, max, Arconix.pl().format().formatTitle(guititle));

            if (!glassless) {
                num = 0;
                while (num != max) {
                    ItemStack glass = Methods.getGlass();
                    i.setItem(num, glass);
                    num++;
                }

                i.setItem(0, Methods.getBackgroundGlass(true));
                i.setItem(1, Methods.getBackgroundGlass(true));
                i.setItem(9, Methods.getBackgroundGlass(true));

                i.setItem(7, Methods.getBackgroundGlass(true));
                i.setItem(8, Methods.getBackgroundGlass(true));
                i.setItem(17, Methods.getBackgroundGlass(true));

                i.setItem(max - 18, Methods.getBackgroundGlass(true));
                i.setItem(max - 9, Methods.getBackgroundGlass(true));
                i.setItem(max - 8, Methods.getBackgroundGlass(true));

                i.setItem(max - 10, Methods.getBackgroundGlass(true));
                i.setItem(max - 2, Methods.getBackgroundGlass(true));
                i.setItem(max - 1, Methods.getBackgroundGlass(true));

                i.setItem(2, Methods.getBackgroundGlass(false));
                i.setItem(6, Methods.getBackgroundGlass(false));
                i.setItem(max - 7, Methods.getBackgroundGlass(false));
                i.setItem(max - 3, Methods.getBackgroundGlass(false));
            }

            num = 10;
            if (glassless) num = 0;
            int id = 0;
            int tmax = max;
            if (!glassless)
                tmax = tmax - 10;
            for (int index = num; index != tmax; index++) {
                if (!glassless && index == 17) index = 19;
                if (id > kitList.size() - 1) {
                    i.setItem(index, new ItemStack(Material.AIR));
                    continue;
                }
                String kitItem = kitList.get(id);

                Kit kit = UltimateKits.getInstance().getKitManager().getKit(kitItem);

                String title = Lang.GUI_KIT_NAME.getConfigValue(TextComponent.formatText(kitItem, true));
                if (UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".title") != null)
                    title = TextComponent.formatText(UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".title"));
                UltimateKits.getInstance().kits.put(title, kitItem.toLowerCase());

                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kitItem.toLowerCase() + ".displayitemkits") != null)
                    item = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kitItem.toLowerCase() + ".displayitemkits").clone();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(title);
                ArrayList<String> lore = new ArrayList<>();
                if (UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".eco") != null) {
                    Double cost = UltimateKits.getInstance().getConfig().getDouble("data.kit." + kitItem.toLowerCase() + ".eco");
                    lore.add(TextComponent.formatText("&7This kit costs &a$" + TextComponent.formatEconomy(cost) + "&7."));
                } else if (UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".link") != null)
                    lore.add(Lang.LINK.getConfigValue());


                if (!Lang.ABOUT_KIT.getConfigValue().trim().equals("")) {
                    String[] parts = Lang.ABOUT_KIT.getConfigValue().split("\\|");
                    lore.add("");
                    for (String line : parts)
                        lore.add(TextComponent.formatText(line));
                }
                if (kit.hasPermission(p)) {
                    long time = new GregorianCalendar().getTimeInMillis();

                    long delay = kit.getNextUse(p) - time; // gets delay
                    if (delay >= 0) {
                        if (!Lang.PLEASE_WAIT.getConfigValue().trim().equals("")) {
                            lore.add(TextComponent.formatText(Lang.PLEASE_WAIT.getConfigValue(Arconix.pl().format().readableTime(delay))));
                        }
                    } else if (!Lang.READY.getConfigValue().trim().equals("")) {
                        lore.add(TextComponent.formatText(Lang.READY.getConfigValue()));
                    }
                } else
                    lore.add(TextComponent.formatText(Lang.NO_ACCESS.getConfigValue()));
                lore.add("");
                lore.add(TextComponent.formatText(Lang.LEFT_PREVIEW.getConfigValue()));
                if (kit.hasPermission(p)) {
                    lore.add(TextComponent.formatText(Lang.RIGHT_CLAIM.getConfigValue()));
                } else if (UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".eco") != null || UltimateKits.getInstance().getConfig().getString("data.kit." + kitItem.toLowerCase() + ".link") != null) {
                    lore.add(TextComponent.formatText(Lang.RIGHT_BUY.getConfigValue()));
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
                i.setItem(index, item);
                id++;
            }

            ItemStack info = new ItemStack(Material.BOOK, 1);
            ItemMeta infometa = info.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            String[] parts = Lang.DETAILS.getConfigValue(p.getName()).split("\\|");
            boolean hit = false;
            for (String line : parts) {
                if (!hit)
                    infometa.setDisplayName(TextComponent.formatText(line));
                else
                    lore.add(TextComponent.formatText(line));
                hit = true;
            }
            infometa.setLore(lore);
            info.setItemMeta(infometa);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull = head;
            if (!UltimateKits.getInstance().v1_7)
                skull = Arconix.pl().getGUI().addTexture(head, "http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b");
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skullMeta.setOwner("MHF_ArrowRight");
            skull.setDurability((short) 3);
            skullMeta.setDisplayName(Lang.NEXT.getConfigValue());
            skull.setItemMeta(skullMeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull2 = head2;
            if (!UltimateKits.getInstance().v1_7)
                skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            skull2.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.LAST.getConfigValue());
            skull2.setItemMeta(skull2Meta);

            if (!UltimateKits.getInstance().getConfig().getBoolean("Interfaces.Do Not Use Glass Borders"))
                i.setItem(max - 5, exit);
            if (kitList.size() == 14)
                i.setItem(max - 4, skull);
            if (page != 1)
                i.setItem(max - 6, skull2);
            if (!UltimateKits.getInstance().getConfig().getBoolean("Interfaces.Do Not Use Glass Borders"))
                i.setItem(4, info);
            p.openInventory(i);

            UltimateKits.getInstance().whereAt.remove(p.getUniqueId());
            UltimateKits.getInstance().whereAt.put(p.getUniqueId(), "kits");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

}
