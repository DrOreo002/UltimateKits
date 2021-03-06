package com.songoda.ultimatekits.handlers;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.ultimatekits.Lang;
import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.kits.BlockEditor;
import com.songoda.ultimatekits.kits.Editor;
import com.songoda.ultimatekits.kits.KitsGUI;
import com.songoda.ultimatekits.kits.object.Key;
import com.songoda.ultimatekits.kits.object.Kit;
import com.songoda.ultimatekits.utils.Debugger;
import com.songoda.ultimatekits.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by songoda on 2/24/2017.
 */
public class CommandHandler implements CommandExecutor {

    private final UltimateKits instance;

    public CommandHandler(UltimateKits instance) {
        this.instance = instance;
    }

    public void help(CommandSender sender, int page) {
        sender.sendMessage("");
        sender.sendMessage(TextComponent.formatText("&7Page: &a" + page + " of 2 ======================"));
        if (page == 1) {
            sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&7" + instance.getDescription().getVersion() + " Created by &5&l&oBrianna"));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&cKits &7View all available kits."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&dPK <kit> &7Preview a kit."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK help <page> &7Displays this page."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK reload &7Reload the Configuration and Language files."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK edit <kit> &7Edit a kit."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK set <kit> &7Make the block you are looking at display a kit"));
        } else if (page == 2) {
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK createkit <kit> &7Create a kit in a GUI."));
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK remove &7Remove a kit from the block you are looking at."));
            String keys = "";
            for (Key key : instance.getKeyManager().getKeys()) {
                keys += "/" + key.getName();
            }
            sender.sendMessage(TextComponent.formatText(" &8- " + "&aUK key <kit/all> <" + keys.substring(1) + "> <player/all> <amount> &7Give a kit key to the players of your server. These keys can be used to redeem kits."));
            sender.sendMessage(TextComponent.formatText("&aTo edit a kit block hold shift and right click it."));
        } else {
            sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "That page does not exist!"));
        }
        sender.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (cmd.getName().equalsIgnoreCase("kits")) {
                KitsGUI.show((Player) sender, 1);
            }
            if (cmd.getName().equalsIgnoreCase("kit")) {
                if (args.length == 0) {
                    KitsGUI.show((Player) sender, 1);
                    return true;
                }
                if (args.length == 1) {
                    Player p = (Player) sender;
                    String kitName = args[0].toLowerCase();
                    if (!Methods.doesKitExist(kitName)) {
                        p.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kitName));
                        return true;
                    }
                    Kit kit = instance.getKitManager().getKit(kitName);
                    if (sender.hasPermission("ultimatekits.admin")) {
                        kit.give(p, false, false, true);
                    } else {
                        kit.buy(p);
                    }
                    return true;
                }
                if (args.length == 2) {
                    String kitName = args[0].toLowerCase();
                    if (!Methods.doesKitExist(kitName)) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kitName));
                        return true;
                    }

                    if (Bukkit.getPlayerExact(args[1]) == null) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.PLAYER_NOT_FOUND.getConfigValue(kitName));
                        return true;
                    }
                    Player p2 = Bukkit.getPlayer(args[1]);
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (!Methods.canGiveKit(p)) {
                            p.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                            return true;
                        }
                    }
                    Kit kit = instance.getKitManager().getKit(kitName);
                    kit.give(p2, false, false, true);
                    sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&7You gave &9" + p2.getDisplayName() + "&7 kit &9" + kit.getShowableName() + "&7."));
                    return true;
                }
                sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText(Lang.SYNTAX.getConfigValue()));
                return true;

            }
            if (cmd.getName().equalsIgnoreCase("ultimatekits")) {
                if (instance.getConfig().getBoolean("Main.Block Help Page For Non Admins") && !sender.hasPermission("ultimatekits.admin")) {
                    sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&7" + instance.getDescription().getVersion() + " " + Lang.NO_PERM.getConfigValue()));
                    return true;
                }

                if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    if (args.length == 2) {
                        help(sender, Integer.parseInt(args[1]));
                    } else {
                        help(sender, 1);
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    Player p = (Player) sender;
                    Block b = p.getTargetBlock((Set<Material>) null, 200);
                    String loc = Arconix.pl().serialize().serializeLocation(b);
                    if (args.length > 2) return true;
                    if (!p.hasPermission("ultimatekits.admin")) {
                        p.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }

                    if (args.length == 1) {
                        if (instance.getConfig().getString("data.block." + loc) == null) {
                            p.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8This block does not contain a kit."));
                            return true;
                        }
                        BlockEditor edit = new BlockEditor(b, p);
                        edit.open();
                    } else if (args.length == 2) {
                        String kit = args[1].toLowerCase();
                        if (!Methods.doesKitExist(kit)) {
                            p.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                            return true;
                        }
                        Editor edit = new Editor(kit, p);
                        edit.open(false, null);
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("createkit")) {
                    Player p = (Player) sender;
                    if (args.length != 2) return true;
                    if (!p.hasPermission("ultimatekits.admin")) {
                        p.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }
                    String kit = args[1].toLowerCase();
                    if (Methods.doesKitExist(kit)) {
                        p.sendMessage(instance.references.getPrefix() + Lang.KIT_ALREADY_EXISTS.getConfigValue(kit));
                    } else {
                        Editor edit = new Editor(kit, p);
                        edit.open(false, null);
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("ultimatekits.admin")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }

                    instance.reload();
                    sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Configuration and Language files reloaded."));
                    return true;
                }
                if (args[0].equalsIgnoreCase("settings")) {
                    if (!sender.hasPermission("ultimatekits.admin")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }
                    Player p = (Player) sender;
                    instance.sm.openSettingsManager(p);
                    return true;
                }
                if (args[0].equalsIgnoreCase("key")) {
                    //UK key <kit/all> <"+keys.substring(1)+"> <player/all> <amount>
                    if (args.length != 4 && args.length != 5) {
                        sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText(Lang.SYNTAX.getConfigValue()));
                        return true;
                    }
                    if (!sender.hasPermission("ultimatekits.admin")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }
                    Kit kit = instance.getKitManager().getKit(args[1]);
                    if (kit == null && !args[1].toLowerCase().equals("all")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[3]) == null && !args[3].trim().equalsIgnoreCase("all")) {
                        sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&cThat username does not exist, or the user is offline!"));
                        return true;
                    }
                    int amt = 1;
                    if (args.length == 5) {
                        if (!Arconix.pl().doMath().isNumeric(args[4])) {
                            amt = 0;
                        } else {
                            amt = Integer.parseInt(args[4]);
                        }
                    }
                    if (amt == 0) {
                        sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&a" + args[3] + " &cis not a number."));
                        return true;
                    }

                    Key key = instance.getKeyManager().getKey(args[2]);
                    if (key == null) {
                        sender.sendMessage(instance.references.getPrefix() + TextComponent.formatText("&a" + args[3] + " &cis not a key."));
                        return true;
                    }


                    if (!args[3].trim().equals("all")) {
                        Player p = Bukkit.getPlayer(args[3]);
                        p.getInventory().addItem(key.getKeyItem(kit, amt));
                        p.sendMessage(instance.references.getPrefix() + Lang.KEY_GIVEN.getConfigValue(kit.getShowableName()));
                        return true;
                    }
                    for (Player pl : instance.getServer().getOnlinePlayers()) {
                        pl.getInventory().addItem(key.getKeyItem(kit, amt));
                        pl.sendMessage(instance.references.getPrefix() + Lang.KEY_GIVEN.getConfigValue(kit.getShowableName()));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length != 2) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                        return true;
                    }
                    if (!sender.hasPermission("ultimatekits.admin")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }
                    Player player = (Player) sender;
                    String kit = args[1].toLowerCase();
                    if (!Methods.doesKitExist(kit)) {
                        player.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                        return true;
                    }
                    Block b = player.getTargetBlock((Set<Material>) null, 200);
                    instance.getKitManager().addKitToLocation(instance.getKitManager().getKit(kit), b.getLocation());
                    sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Kit &a" + kit + " &8set to: &a" + b.getType().toString() + "&8."));

                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length != 1) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                        return true;
                    }
                    if (!sender.hasPermission("ultimatekits.admin")) {
                        sender.sendMessage(instance.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        return true;
                    }
                    Player player = (Player) sender;
                    Block b = player.getTargetBlock((Set<Material>) null, 200);
                    Kit kit = instance.getKitManager().removeKitFromLocation(b.getLocation());
                    UltimateKits.getInstance().holo.updateHolograms();
                    player.sendMessage(TextComponent.formatText(UltimateKits.getInstance().references.getPrefix() + "&8Kit &9" + kit.getName() + " &8unassigned from: &a" + b.getType().toString() + "&8."));

                    return true;
                }
                sender.sendMessage(TextComponent.formatText(instance.references.getPrefix() + "&8Invalid argument.. Looking for &9/pk " + args[0] + "&8?"));
                return true;
            } else if (cmd.getName().equalsIgnoreCase("previewkit")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(instance.references.getPrefix() + Lang.NO_CONSOLE_ACCESS.getConfigValue());
                    return true;
                }
                Player p = (Player) sender;
                if (args.length != 1) {
                    p.sendMessage(instance.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                    return true;
                }
                Kit kit = instance.getKitManager().getKit(args[0].toLowerCase());
                if (kit == null) {
                    p.sendMessage(instance.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue());
                    return true;
                }
                kit.display(p, false);
            }

        } catch (
                Exception ex)

        {
            Debugger.runReport(ex);
        }
        return true;
    }

}
