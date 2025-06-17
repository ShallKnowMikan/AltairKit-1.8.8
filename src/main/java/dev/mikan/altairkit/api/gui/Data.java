package dev.mikan.altairkit.api.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Data {

    public static Map<Player,String> GUIMap = new HashMap<>();

    public static boolean listeningToAltairGUIs;


    public static void addToMap(Player player,String title){
        if (!Data.GUIMap.containsKey(player)) Data.GUIMap.put(player,title);
        else Data.GUIMap.replace(player,title);
    }
}
