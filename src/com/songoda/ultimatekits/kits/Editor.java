package com.songoda.ultimatekits.kits;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.UltimateKits;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by songoda on 3/2/2017.
 */
public class Editor {

    private Kit kit;
    private String kitStr;
    private Player p;

    public Editor(String kitName, Player pl) {
        try {
            this.kitStr = kitName;
            this.kit = UltimateKits.getInstance().getKitManager().getKit(kitName);
            this.p = pl;
            if (kit == null) {
                p.sendMessage(UltimateKits.getInstance().references.getPrefix() + TextComponent.formatText("&aThat kit doesn't exist. Creating it now."));
                kit = new Kit(kitStr, null, null, 0, 0, new ArrayList<>());
                UltimateKits.getInstance().getKitManager().addKit(kit);
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            UltimateKits.getInstance().inEditor.put(p.getUniqueId(), window);
            UltimateKits.getInstance().editingKit.put(p.getUniqueId(), kitStr);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open(boolean backb, ItemStack command) {
        try {
            p.updateInventory();
            String name = kit.getShowableName();
            Inventory i = Bukkit.createInventory(null, 54, Arconix.pl().format().formatTitle("&8You are editing kit: &9" + name + "&8."));

            ItemStack exit = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!UltimateKits.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);


            ItemStack it = new ItemStack(Material.CHEST, 1);
            ItemMeta itmeta = it.getItemMeta();
            itmeta.setDisplayName(TextComponent.formatText("&5&l" + kitStr));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&fPermissions:"));
            lore.add(TextComponent.formatText("&7essentials.kits." + kitStr.toLowerCase()));
            itmeta.setLore(lore);
            it.setItemMeta(itmeta);

            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta glassmeta = glass.getItemMeta();
            glassmeta.setDisplayName(TextComponent.formatText("&" + kitStr.replaceAll(".(?!$)", "$0&")));
            glass.setItemMeta(glassmeta);

            if (backb)
                i.setItem(0, back);
            i.setItem(4, it);
            i.setItem(8, exit);

            int num = 10;
            List<ItemStack> list = kit.getReadableContents(p, true);
            for (ItemStack is : list) {
                /*
                ItemMeta meta;

                if (is.hasItemMeta()) meta = is.getItemMeta();
                else meta = Bukkit.getItemFactory().getItemMeta(is.getType());

                List<String> itemLore;

                if (meta.hasLore()) itemLore = meta.getLore();
                else itemLore = new ArrayList<>();

                itemLore.add("");
                itemLore.add(TextComponent.formatText("&7Left-Click: &6To set a display item."));
                itemLore.add(TextComponent.formatText("&7Middle-Click: &6To set a display name."));
                itemLore.add(TextComponent.formatText("&7Right-Click: &6To set display lore."));
                itemLore.add(TextComponent.formatText("&7Shift-Click: &6To set crate percentage."));
                itemLore.add("");
                itemLore.add(TextComponent.formatText("&6Leave function mode to move items."));
                meta.setLore(itemLore);
                is.setItemMeta(meta);
*/
                if (num == 17 || num == 36)
                    num++;
                if (is.getAmount() > 64) {
                    int overflow = is.getAmount() % 64;
                    int stackamt = (int) ((long) (is.getAmount() / 64));
                    int num3 = 0;
                    while (num3 != stackamt) {
                        ItemStack is2 = is;
                        is2.setAmount(64);
                        i.setItem(num, is2);
                        num++;
                        num3++;
                    }
                    if (overflow != 0) {
                        ItemStack is2 = is;
                        is2.setAmount(overflow);
                        i.setItem(num, is2);
                        num++;
                    }
                } else {
                    i.setItem(num, is);
                    num++;
                }
            }
            if (command != null)
                i.setItem(num, command);

            i.setItem(3, Methods.getGlass());
            i.setItem(5, Methods.getGlass());

            i.setItem(48, Methods.getGlass());
            i.setItem(50, Methods.getGlass());

            if (!backb)
                i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));

            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(17, Methods.getBackgroundGlass(true));

            i.setItem(54 - 18, Methods.getBackgroundGlass(true));
            i.setItem(54 - 9, Methods.getBackgroundGlass(true));
            i.setItem(54 - 8, Methods.getBackgroundGlass(true));

            i.setItem(54 - 10, Methods.getBackgroundGlass(true));
            i.setItem(54 - 2, Methods.getBackgroundGlass(true));
            i.setItem(54 - 1, Methods.getBackgroundGlass(true));

            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(54 - 7, Methods.getBackgroundGlass(false));
            i.setItem(54 - 3, Methods.getBackgroundGlass(false));


            p.openInventory(i);
            defineInstance("editor");

            getInvItems(p);
            updateInvButton(i);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public void updateInvButton(Inventory i) {
        ItemStack alli = new ItemStack(Material.ITEM_FRAME, 1);
        ItemMeta allmeta = alli.getItemMeta();
        if (UltimateKits.getInstance().inventoryHolder.containsKey(p.getUniqueId())) {
            allmeta.setDisplayName(TextComponent.formatText("&6Switch To Your Inventory"));
            List<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Clicking to switch to"));
            lore.add(TextComponent.formatText("&7your inventory."));
            allmeta.setLore(lore);
        } else {
            allmeta.setDisplayName(TextComponent.formatText("&6Switch To Kit Functions"));
            List<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Clicking to switch back"));
            lore.add(TextComponent.formatText("&7to the kit functions."));
            allmeta.setLore(lore);
        }
        alli.setItemMeta(allmeta);
        i.setItem(49, alli);
    }

    public void getInvItems(Player p) {

        UltimateKits.getInstance().inventoryHolder.put(p.getUniqueId(), p.getInventory().getContents().clone());
        p.getInventory().clear();

        ItemStack alli = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        ItemMeta allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&6General Options"));
        List<String> lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to edit adjust"));
        lore.add(TextComponent.formatText("&7general options."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(9, alli);

        alli = new ItemStack(Material.EMERALD, 1);
        allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&9Selling Options"));
        lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to edit adjust"));
        lore.add(TextComponent.formatText("&7selling options."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(10, alli);

        alli = new ItemStack(Material.ITEM_FRAME, 1);
        allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&5GUI Options"));
        lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to edit GUI options"));
        lore.add(TextComponent.formatText("&7for this kit."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(12, alli);

        alli = new ItemStack(Material.PAPER, 1);
        allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&fAdd Command"));
        lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to add a command"));
        lore.add(TextComponent.formatText("&7to this kit."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(13, alli);

        alli = new ItemStack(Material.DOUBLE_PLANT, 1);
        allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&6Add Economy"));
        lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to add money"));
        lore.add(TextComponent.formatText("&7to this kit."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(14, alli);

        alli = new ItemStack(Material.REDSTONE, 1);
        allmeta = alli.getItemMeta();
        allmeta.setDisplayName(TextComponent.formatText("&aSave Changes"));
        lore = new ArrayList<>();
        lore.add(TextComponent.formatText("&7Click to save all changes."));
        allmeta.setLore(lore);
        alli.setItemMeta(allmeta);

        p.getInventory().setItem(17, alli);
    }

    public void selling() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8Selling Options for &a" + kit.getShowableName() + "&8."));

            Methods.fillGlass(i);

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

            ItemStack exit = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!UltimateKits.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&c&lSet not for sale"));
            ArrayList<String> lore = new ArrayList<>();

            if (kit.getPrice() != 0 ||
                    kit.getLink() != null)
                lore.add(TextComponent.formatText("&7Currently &aFor Sale&7."));
            else
                lore.add(TextComponent.formatText("&7Currently &cNot For Sale&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7remove this kit from sale."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lSet kit link"));
            lore = new ArrayList<>();
            if (kit.getLink() != null)
                lore.add(TextComponent.formatText("&7Currently: &a" + kit.getLink() + "&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7allow you to set a link"));
            lore.add(TextComponent.formatText("&7that players will receive"));
            lore.add(TextComponent.formatText("&7when attempting to purchase"));
            lore.add(TextComponent.formatText("&7this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lSet kit price"));
            lore = new ArrayList<>();
            if (kit.getPrice() != 0)
                lore.add(TextComponent.formatText("&7Currently: &a$" + Arconix.pl().format().formatEconomy(kit.getPrice()) + "&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7allow you to set a price"));
            lore.add(TextComponent.formatText("&7that players will be able to"));
            lore.add(TextComponent.formatText("&7purchase this kit for"));
            lore.add(TextComponent.formatText("&7requires &aVault&7."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            p.openInventory(i);
            defineInstance("selling");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void gui() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8GUI Options for &a" + kit.getShowableName() + "&8."));

            Methods.fillGlass(i);

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

            ItemStack exit = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!UltimateKits.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet Title"));
            ArrayList<String> lore = new ArrayList<>();
            if (kit.getTitle() != null)
                lore.add(TextComponent.formatText("&7Currently: &a" + kit.getDelay() + "&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Left-Click: &9to set"));
            lore.add(TextComponent.formatText("&9the kit title for holograms"));
            lore.add(TextComponent.formatText("&9and the kit / kits GUIs."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Right-Click: &9to reset."));
            allmeta.setLore(lore);

            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits") != null) {
                alli = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet /kits DisplayItem"));
            lore = new ArrayList<>();
            if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits") != null) {
                ItemStack is = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits");
                lore.add(TextComponent.formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7Right-Click to: &9Set a"));
            lore.add(TextComponent.formatText("&9display item for this kit"));
            lore.add(TextComponent.formatText("&9to the item in your hand."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Left-Click to: &9Remove the item."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.COAL);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lBlacklist kit"));
            lore = new ArrayList<>();
            if (UltimateKits.getInstance().getConfig().getString("data.kit." + kit.getName() + ".blacklisted") != null) {
                lore.add(TextComponent.formatText("&7Currently: &aBlacklisted&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently: &cNot blacklisted&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7A blacklisted kit will not"));
            lore.add(TextComponent.formatText("&7show up in the /kits gui."));
            lore.add(TextComponent.formatText("&7This is usually optimal for"));
            lore.add(TextComponent.formatText("&7preventing players from seeing"));
            lore.add(TextComponent.formatText("&7non obtainable kits or starter kits."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("gui");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void general() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8General Options for &a" + kit.getShowableName() + "&8."));

            Methods.fillGlass(i);

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

            ItemStack exit = new ItemStack(Material.valueOf(UltimateKits.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!UltimateKits.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (UltimateKits.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet Title"));
            ArrayList<String> lore = new ArrayList<>();
            if (kit.getTitle() != null)
                lore.add(TextComponent.formatText("&7Currently: &a" + kit.getDelay() + "&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Left-Click: &9to set"));
            lore.add(TextComponent.formatText("&9the kit title for holograms"));
            lore.add(TextComponent.formatText("&9and the kit / kits GUIs."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Right-Click: &9to reset."));
            allmeta.setLore(lore);

            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            /*i.setItem(11, alli);*/

            alli = new ItemStack(Material.WATCH);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lChange Delay"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Currently set to: &a" + kit.getDelay() + "&7."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Use this to alter this kits delay."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.TNT);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&c&lDestroy Kit"));
            lore = new ArrayList<>();
            lore.add("");
            lore.add(TextComponent.formatText("&7Click this to destroy this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("general");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setKitsDisplayItem(boolean type) {
        try {
            gui();
            if (type) {
                ItemStack is = p.getItemInHand();
                if (is == null || is.getType() == Material.AIR) {
                    p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "&8You must be holding an item to use this function."));
                    return;
                }
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitemkits", is);
                UltimateKits.getInstance().saveConfig();
                p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "&8Custom Item Display set for kit &a" + kit.getShowableName() + "&8."));
            } else {
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitemkits", null);
                UltimateKits.getInstance().saveConfig();
                p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "&8Custom Item Display removed from kit &a" + kit.getShowableName() + "&8."));
            }

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void createCommand() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "command") {
                        p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                        UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 500L);
            p.closeInventory();
            defineInstance("command");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a command. Example: &aeco give {player} 1000"));
            p.sendMessage(TextComponent.formatText("do not include a &a/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void createMoney() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "money") {
                        p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                        UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 500L);
            p.closeInventory();
            defineInstance("money");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a dollar amount. Example: &a10000"));
            p.sendMessage(TextComponent.formatText("do not include a &a$"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void saveKit(Player p, Inventory i) {
        try {
            ItemStack[] items = i.getContents();
            int num = 0;
            for (ItemStack item : items) {
                if (num < 10 || num == 17 || num == 36) {
                    items[num] = null;
                }
                num++;
            }

            items = Arrays.copyOf(items, items.length - 10);

            kit.saveKit(Arrays.asList(items));
            p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "&8Changes to &a" + kit.getShowableName() + " &8saved successfully."));
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void blacklist() {
        try {
            if (UltimateKits.getInstance().getConfig().getString("data.kit." + kit.getName() + ".blacklisted") == null) {
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".blacklisted", true);
                UltimateKits.getInstance().saveConfig();
            } else {
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".blacklisted", null);
                UltimateKits.getInstance().saveConfig();
            }
            gui();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setNoSale() {
        try {
            kit.setPrice(0);
            kit.setLink(null);
            UltimateKits.getInstance().holo.updateHolograms();
            selling();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setDelay() {
        try {
            p.closeInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "delay") {
                        p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                        UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 200L);
            defineInstance("delay");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Type a delay in seconds for this kit. Example: 10"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setTitle(boolean type) {
        try {
            if (type) {
                p.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                    if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                        if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "title") {
                            p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                            UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                        }
                    }
                }, 200L);
                defineInstance("title");
                p.sendMessage("");
                p.sendMessage(TextComponent.formatText("Type a title for the GUI. Example: &aThe Cool Kids Kit"));
                p.sendMessage("");
            } else {
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".title", null);
                UltimateKits.getInstance().saveConfig();
                UltimateKits.getInstance().holo.updateHolograms();
                gui();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editPrice() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "price") {
                        p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                        UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("price");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a price. Example: &a50000"));
            p.sendMessage(TextComponent.formatText("&cUse only numbers."));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editLink() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateKits.getInstance(), () -> {
                if (UltimateKits.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (UltimateKits.getInstance().inEditor.get(p.getUniqueId()) == "link") {
                        p.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "Editing Timed out."));
                        UltimateKits.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("link");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a link. Example: &ahttp://buy.viscernity.com/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public Kit getKit() {
        return kit;
    }

}
