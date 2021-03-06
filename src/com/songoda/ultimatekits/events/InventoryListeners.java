package com.songoda.ultimatekits.events;

import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.kits.*;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class InventoryListeners implements Listener {

    private final UltimateKits instance;

    public InventoryListeners(UltimateKits instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            Player p = (Player) event.getWhoClicked();
            if (instance.buy.containsKey(p.getUniqueId())) {
                if (event.getSlot() == 11) {
                    Kit kit = instance.getKitManager().getKit(instance.buy.get(p.getUniqueId()));
                    kit.buyWithEconomy(p);
                    p.closeInventory();
                    instance.buy.remove(p.getUniqueId());
                } else if (event.getSlot() == 15) {
                    p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + Lang.BUYCANCELLED.getConfigValue()));
                    p.closeInventory();
                    instance.buy.remove(p.getUniqueId());
                }
                event.setCancelled(true);
            } else if (instance.whereAt.containsKey(event.getWhoClicked().getUniqueId()) && instance.whereAt.get(event.getWhoClicked().getUniqueId()).equals("kits")) {
                event.setCancelled(true);
                if (instance.references.isPlaySound())
                    p.playSound(event.getWhoClicked().getLocation(), instance.references.getSound(), 10.0F, 1.0F);
                if (event.getAction() == InventoryAction.NOTHING
                        || event.getCurrentItem().getType() == Material.AIR
                        || event.getCurrentItem().getItemMeta().getDisplayName() == null) {
                    return;
                }
                ItemStack clicked = event.getCurrentItem();
                int page = instance.page.get(p.getUniqueId());
                if (event.getSlot() == 4) {
                } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.NEXT.getConfigValue()))) {
                    KitsGUI.show(p, page + 1);
                } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.LAST.getConfigValue()))) {
                    KitsGUI.show(p, page - 1);
                } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                    p.closeInventory();
                } else if (!ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("")) {
                    if (!clicked.getItemMeta().hasDisplayName()) return;
                    String kitName = instance.kits.get(clicked.getItemMeta().getDisplayName());
                    Kit kit = instance.getKitManager().getKit(kitName);
                    if (event.getClick().isLeftClick()) {
                        kit.display(p, true);
                    } else {
                        kit.buy(p);
                    }
                }

            } else if (instance.whereAt.containsKey(event.getWhoClicked().getUniqueId()) && instance.whereAt.get(event.getWhoClicked().getUniqueId()).equals("display")) {
                event.setCancelled(true);
                if (instance.references.isPlaySound()) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), instance.references.getSound(), 10.0F, 1.0F);
                }
                if (event.getAction() == InventoryAction.NOTHING
                        || event.getCurrentItem().getType() == Material.AIR
                        || event.getCurrentItem().getItemMeta().getDisplayName() == null) {
                    return;
                }
                ItemStack clicked = event.getCurrentItem();
                if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BACK.getConfigValue()))) {
                    KitsGUI.show(p, 1);
                }
                if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                    p.closeInventory();
                }
                if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BUYNOW.getConfigValue()))) {
                    p.closeInventory();
                    String kitName = instance.inKit.get(p.getUniqueId()).getName();
                    Kit kit = instance.getKitManager().getKit(kitName);
                    kit.buy(p);
                }
            } else if (instance.inEditor.containsKey(event.getWhoClicked().getUniqueId()) && event.getClickedInventory() != null) {
                if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "menu") {
                    event.setCancelled(true);
                    BlockEditor edit = new BlockEditor(instance.editing.get(p.getUniqueId()), p);

                    if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
                    ItemStack clicked = event.getCurrentItem();
                    if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue())))
                        p.closeInventory();
                    if ((event.getSlot() > 44 || event.getSlot() < 9)
                            && event.getClickedInventory().getType() == InventoryType.CHEST) {
                        event.setCancelled(true);
                    }
                    switch (event.getSlot()) {
                        case 11:
                            edit.changeDisplayType();
                            break;
                        case 13:
                            edit.decor();
                            break;
                        case 15:
                            Editor ed = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                            ed.open(true, null);
                            break;
                    }
                } else if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "editor") {
                    Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                    if ((event.getSlot() < 10 || event.getSlot() > 43) || event.getSlot() == 17 || event.getSlot() == 36) {
                        if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                            event.setCancelled(true);
                        }
                    }
                    if (event.getClickedInventory() == p.getInventory()
                            &&(instance.inventoryHolder.containsKey(p.getUniqueId()))) {
                        event.setCancelled(true);
                        if (event.getClickedInventory() == p.getInventory()) {
                            switch (event.getSlot()) {
                                case 9:
                                    edit.general();
                                    break;
                                case 10:
                                    edit.selling();
                                    break;
                                case 12:
                                    edit.gui();
                                    break;
                                case 13:
                                    edit.createCommand();
                                    break;
                                case 14:
                                    edit.createMoney();
                                    break;
                                case 17:
                                    edit.saveKit(p, p.getOpenInventory().getTopInventory());
                                    break;
                            }
                        }
                    }
                    if (event.getSlot() == 49) {
                        if (instance.inventoryHolder.containsKey(p.getUniqueId())) {
                            p.getInventory().setContents(instance.inventoryHolder.get(p.getUniqueId()));
                            p.updateInventory();
                            instance.inventoryHolder.remove(p.getUniqueId());
                        } else edit.getInvItems(p);
                        edit.updateInvButton(event.getClickedInventory());
                    }
                } else if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "decor") {
                    BlockEditor edit = new BlockEditor(instance.editing.get(p.getUniqueId()), p);
                    event.setCancelled(true);
                    switch (event.getSlot()) {
                        case 10:
                            edit.toggleHologram();
                            break;
                        case 12:
                            edit.toggleParticles();
                            break;
                        case 14:
                            edit.toggleDisplayItems();
                            break;
                        case 16:
                            if (event.getClick() == ClickType.LEFT)
                                edit.setDisplayItem(true);
                            else if (event.getClick() == ClickType.RIGHT)
                                edit.setDisplayItem(false);
                            break;
                    }
                } else if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "selling") {
                    Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                    event.setCancelled(true);
                    switch (event.getSlot()) {
                        case 10:
                            edit.setNoSale();
                            break;
                        case 12:
                            edit.editLink();
                            break;
                        case 14:
                            edit.editPrice();
                            break;
                    }
                } else if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "gui") {
                    Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                    event.setCancelled(true);
                    switch (event.getSlot()) {
                        case 11:
                            if (event.getClick() == ClickType.RIGHT)
                                edit.setTitle(false);
                            else if (event.getClick() == ClickType.LEFT)
                                edit.setTitle(true);
                            break;
                        case 13:
                            if (event.getClick() == ClickType.LEFT)
                                edit.setKitsDisplayItem(true);
                            else if (event.getClick() == ClickType.RIGHT)
                                edit.setKitsDisplayItem(false);
                            break;
                        case 15:
                            edit.blacklist();
                            break;
                    }
                } else if (instance.inEditor.get(event.getWhoClicked().getUniqueId()) == "general") {
                    Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                    event.setCancelled(true);
                    switch (event.getSlot()) {
                        case 13:
                            edit.setDelay();
                            break;
                        case 15:
                            instance.getKitManager().removeKit(edit.getKit());
                            p.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&cKit destroyed successfully."));
                            p.closeInventory();
                            break;
                    }
                }
                if (event.getCurrentItem().getItemMeta() == null || !event.getCurrentItem().getItemMeta().hasDisplayName())
                    return;
                if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                    p.closeInventory();
                }
                if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BACK.getConfigValue()))) {
                    if (event.getClickedInventory().getTitle().contains("Editing decor for") || event.getClickedInventory().getTitle().contains("You are editing kit")) {
                        BlockEditor edit = new BlockEditor(instance.editing.get(p.getUniqueId()), p);
                        edit.open();
                    } else {
                        Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
                        edit.open(false, null);
                    }
                }
            }


        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private boolean hasEssentialsX() {
        Class db = com.earth2me.essentials.ItemDb.class;
        for (Method method : db.getDeclaredMethods()) {
            if (method.getName().equals("serialize")) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        try {
            if (!event.getInventory().getTitle().startsWith("Previewing")
                    || !(instance.whereAt.containsKey(event.getWhoClicked().getUniqueId()) && instance.whereAt.get(event.getWhoClicked().getUniqueId()).equals("display")))
                return;
            event.setCancelled(true);
            if (instance.references.isPlaySound())
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), instance.references.getSound(), 10.0F, 1.0F);

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        try {
            if (!event.getInventory().getTitle().startsWith("Previewing")
                    || !(instance.whereAt.containsKey(event.getWhoClicked().getUniqueId()) && instance.whereAt.get(event.getWhoClicked().getUniqueId()).equals("display")))
                return;
            event.setCancelled(true);
            if (instance.references.isPlaySound())
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), instance.references.getSound(), 10.0F, 1.0F);

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        try {
            final Player p = (Player) event.getPlayer();
            instance.buy.remove(p.getUniqueId());


            if (instance.inventoryHolder.containsKey(p.getUniqueId())) {
                p.getInventory().setContents(instance.inventoryHolder.get(p.getUniqueId()));
                p.updateInventory();
                instance.inventoryHolder.remove(p.getUniqueId());
            }

            if (instance.inEditor.containsKey(p.getUniqueId())) {
                instance.inEditor.remove(p.getUniqueId());
            }

            if (!instance.whereAt.containsKey(p.getUniqueId())) {
                return;
            }

            if (instance.whereAt.containsKey(p.getUniqueId()))
                instance.whereAt.remove(p.getUniqueId());

            Bukkit.getScheduler().runTaskLater(instance, () -> {
                if (!p.getOpenInventory().getTopInventory().getType().equals(InventoryType.CHEST))
                    p.closeInventory();
            }, 1L);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}

