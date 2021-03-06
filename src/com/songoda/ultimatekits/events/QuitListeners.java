package com.songoda.ultimatekits.events;

import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.utils.Debugger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListeners implements Listener {

    private final UltimateKits instance;

    public QuitListeners(UltimateKits instance) {
        this.instance = instance;
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        try {
            Player p = event.getPlayer();
            if (instance.inventoryHolder.containsKey(p.getUniqueId())) {
                p.getInventory().setContents(instance.inventoryHolder.get(p.getUniqueId()));
                p.updateInventory();
                instance.inventoryHolder.remove(p.getUniqueId());
            }
            instance.inEditor.remove(p.getUniqueId());
            instance.whereAt.remove(p.getUniqueId());
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}

