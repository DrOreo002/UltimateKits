package com.songoda.ultimatekits.kits;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.kits.object.KitBlockData;
import com.songoda.ultimatekits.utils.Debugger;
import com.songoda.ultimatekits.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 3/3/2017.
 */
public class BlockEditor {

    private Location location;
    private String locationStr;
    private KitBlockData kitBlockData;
    private Kit kit;
    private Block block;
    private Player player;

    public BlockEditor(Block block, Player player) {
        try {
            this.location = block.getLocation();
            this.locationStr = Arconix.pl().serialize().serializeLocation(block);
            this.player = player;
            this.kitBlockData = UltimateKits.getInstance().getKitManager().getKit(location);
            this.kit = kitBlockData.getKit();
            this.block = block;
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            UltimateKits.getInstance().inEditor.put(player.getUniqueId(), window);
            UltimateKits.getInstance().editing.put(player.getUniqueId(), block);
            UltimateKits.getInstance().editingKit.put(player.getUniqueId(), kit.getName());
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, TextComponent.formatText("&8This contains &a" + Arconix.pl().format().formatTitle(kit.getShowableName())));

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
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.REDSTONE_COMPARATOR);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&5&lSwitch kit type"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to swap this kits type."));
            lore.add("");
            if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr) == null) {
                lore.add(TextComponent.formatText("&6Normal"));
                lore.add(TextComponent.formatText("&7Crate"));
                lore.add(TextComponent.formatText("&7Daily"));
            } else if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr).equals("crate")) {
                lore.add(TextComponent.formatText("&7Normal"));
                lore.add(TextComponent.formatText("&6Crate"));
                lore.add(TextComponent.formatText("&7Daily"));
            } else if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr).equals("daily")) {
                lore.add(TextComponent.formatText("&7Normal"));
                lore.add(TextComponent.formatText("&7Crate"));
                lore.add(TextComponent.formatText("&6Daily"));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            alli = new ItemStack(Material.RED_ROSE);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lDecor Options"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to edit the decoration"));
            lore.add(TextComponent.formatText("&7options for this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.DIAMOND_PICKAXE);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lEdit kit"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to edit the kit"));
            lore.add(TextComponent.formatText("&7contained in this block."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            player.openInventory(i);
            defineInstance("menu");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void decor() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, TextComponent.formatText("&8Editing decor for &a" + Arconix.pl().format().formatTitle(kit.getShowableName()) + "&8."));

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
            ItemStack back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.SIGN);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lToggle Holograms"));
            ArrayList<String> lore = new ArrayList<>();
            if (kitBlockData.showHologram()) {
                lore.add(TextComponent.formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.POTION);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lToggle Particles"));
            lore = new ArrayList<>();
            if (kitBlockData.hasParticles()) {
                lore.add(TextComponent.formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.GRASS);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lToggle DisplayItems"));
            lore = new ArrayList<>();
            if (kitBlockData.isDisplayingItems()) {
                lore.add(TextComponent.formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.GLASS);
            if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getShowableName() + ".displayitem") != null) {
                alli = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getShowableName() + ".displayitem");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet single DisplayItem"));
            lore = new ArrayList<>();
            if (UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getShowableName() + ".displayitem") != null) {
                ItemStack is = UltimateKits.getInstance().getConfig().getItemStack("data.kit." + kit.getShowableName() + ".displayitem");
                lore.add(TextComponent.formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7Right-Click to &9Set a"));
            lore.add(TextComponent.formatText("&9forced display item for this "));
            lore.add(TextComponent.formatText("&9kit to the item in your hand."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Left-Click to &9Remove the item."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            player.openInventory(i);
            defineInstance("decor");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void toggleHologram() {
        try {
            if (kitBlockData.showHologram()) {
                kitBlockData.setShowHologram(false);
            } else {
                kitBlockData.setShowHologram(true);
            }
            UltimateKits.getInstance().holo.updateHolograms();
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleParticles() {
        try {
            if (kitBlockData.hasParticles()) {
                kitBlockData.setHasParticles(false);
            } else {
                kitBlockData.setHasParticles(true);
            }
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleDisplayItems() {
        try {
            UltimateKits plugin = UltimateKits.getInstance();

            boolean isHolo = kitBlockData.showHologram();

            if (isHolo) {
                kitBlockData.setShowHologram(false);
                plugin.holo.updateHolograms();
            }

            if (kitBlockData.isDisplayingItems()) {
                kitBlockData.setDisplayingItems(false);
            } else {
                kitBlockData.setDisplayingItems(true);
            }
            decor();
            if (isHolo) {
                kitBlockData.setShowHologram(true);
                UltimateKits.getInstance().holo.updateHolograms();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setDisplayItem(boolean type) {
        try {
            if (type) {
                ItemStack is = player.getItemInHand().clone();
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitem", is);
                UltimateKits.getInstance().saveConfig();
            } else {
                UltimateKits.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitem", null);
                UltimateKits.getInstance().saveConfig();
            }
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void changeDisplayType() {
        try {
            if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr) == null) {
                UltimateKits.getInstance().getConfig().set("data.type." + locationStr, "crate");
            } else if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr).equals("crate")) {
                UltimateKits.getInstance().getConfig().set("data.type." + locationStr, "daily");
            } else if (UltimateKits.getInstance().getConfig().getString("data.type." + locationStr).equals("daily")) {
                UltimateKits.getInstance().getConfig().set("data.type." + locationStr, null);
            }
            UltimateKits.getInstance().saveConfig();
            UltimateKits.getInstance().holo.updateHolograms();
            open();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
