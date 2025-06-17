package dev.mikan.altairkit.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class InventoryCloseEventListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){


        Player player = (Player) e.getPlayer();

        if (!Data.GUIMap.containsKey(player)) return;

        if (e.getInventory().getTitle().equals(Data.GUIMap.get(player)))
            Data.GUIMap.remove(player);
    }

}
