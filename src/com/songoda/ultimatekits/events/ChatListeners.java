package com.songoda.ultimatekits.events;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.kits.Editor;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.utils.Debugger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 2/24/2017.
 */
public class ChatListeners implements Listener {

    private final UltimateKits instance;

    public ChatListeners(UltimateKits instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        try {
            final Player p = e.getPlayer();
            if (!instance.inEditor.containsKey(p.getUniqueId())) return;

            Editor edit = new Editor(instance.editingKit.get(p.getUniqueId()), p);
            String msg = e.getMessage().trim();
            Kit kit = instance.getKitManager().getKit(instance.editingKit.get(p.getUniqueId()));
            e.setCancelled(true);

            switch (instance.inEditor.get(p.getUniqueId())) {
                case "price":
                    if (instance.getServer().getPluginManager().getPlugin("Vault") == null) {
                        p.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&8You must have &aVault &8installed to utilize economy.."));
                    } else if (!Arconix.pl().doMath().isNumeric(msg)) {
                        p.sendMessage(TextComponent.formatText("&a" + msg + " &8is not a number. Please do not include a &a$&8."));
                    } else {

                        if (kit.getLink() != null) {
                            kit.setLink(null);
                            p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8LINK has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                        }
                        Double eco = Double.parseDouble(msg);
                        kit.setPrice(eco);
                        instance.holo.updateHolograms();
                    }
                    instance.inEditor.remove(p.getUniqueId());
                    edit.selling();
                    break;
                case "delay":
                    if (!Arconix.pl().doMath().isNumeric(msg)) {
                        p.sendMessage(TextComponent.formatText("&a" + msg + " &8is not a number. Please do not include a &a$&8."));
                    } else {
                        kit.setDelay(Integer.parseInt(msg));
                    }
                    instance.inEditor.remove(p.getUniqueId());
                    edit.general();
                    break;
                case "link":
                    if (kit.getPrice() != 0) {
                        kit.setPrice(0);
                        p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8ECO has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                    }
                    kit.setLink(msg);
                    instance.holo.updateHolograms();
                    instance.inEditor.remove(p.getUniqueId());
                    edit.selling();
                    break;
                case "title":
                    kit.setTitle(msg);
                    instance.saveConfig();
                    instance.holo.updateHolograms();
                    p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Title &5" + msg + "&8 added to Kit &a" + kit.getShowableName() + "&8."));
                    instance.inEditor.remove(p.getUniqueId());
                    edit.gui();
                    break;
                case "command":
                    ItemStack parseStack = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = parseStack.getItemMeta();

                    ArrayList<String> lore = new ArrayList<>();

                    int index = 0;
                    while (index < msg.length()) {
                        lore.add("§a/" + msg.substring(index, Math.min(index + 30, msg.length())));
                        index += 30;
                    }
                    meta.setLore(lore);
                    meta.setDisplayName(Lang.COMMAND.getConfigValue());
                    parseStack.setItemMeta(meta);

                    p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Command &5" + msg + "&8 has been added to your kit."));
                    instance.inEditor.remove(p.getUniqueId());
                    edit.open(false, parseStack);
                    break;
                case "money":
                    ItemStack parseStack2 = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta2 = parseStack2.getItemMeta();

                    ArrayList<String> lore2 = new ArrayList<>();

                    int index2 = 0;
                    while (index2 < msg.length()) {
                        lore2.add("§a$" + msg.substring(index2, Math.min(index2 + 30, msg.length())));
                        index2 += 30;
                    }
                    meta2.setLore(lore2);
                    meta2.setDisplayName(Lang.MONEY.getConfigValue());
                    parseStack2.setItemMeta(meta2);

                    p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Money &5$" + msg + "&8 has been added to your kit."));
                    instance.inEditor.remove(p.getUniqueId());
                    edit.open(false, parseStack2);
                    break;
                default:
                    e.setCancelled(false);
                    break;
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerChatEvent event) {
        try {
            if (event.getMessage().equalsIgnoreCase("/kits") || event.getMessage().equalsIgnoreCase("/kit")) {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

}
