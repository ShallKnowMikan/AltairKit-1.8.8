package dev.mikan.altairkit.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InventoryClickEventListener implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){


        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();

        if (!Data.GUIMap.containsKey(player)) return;

        if (e.getClickedInventory() == null) return;

        String title = e.getClickedInventory().getTitle();

        if (title.equals(Data.GUIMap.get(player)))
            e.setCancelled(true);
    }





}
