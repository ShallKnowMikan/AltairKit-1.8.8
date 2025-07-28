package dev.mikan.altairkit;


import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.mikan.altairkit.api.commands.AltairCommand;
import dev.mikan.altairkit.api.commands.TestCommands;
import dev.mikan.altairkit.api.gui.Data;
import dev.mikan.altairkit.api.gui.InventoryClickEventListener;
import dev.mikan.altairkit.api.gui.InventoryCloseEventListener;
import dev.mikan.altairkit.api.json.AltairGsonFactory;
import dev.mikan.altairkit.api.json.JsonFile;
import dev.mikan.altairkit.utils.CmdMap;
import dev.mikan.altairkit.utils.Commands;
import dev.mikan.altairkit.utils.SkinFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public final class AltairKit extends JavaPlugin {


    public  static JsonFile file ;
    @Override
    public void onEnable() {
        // Testing only

        // mvn install:install-file -Dfile=/home/mikan/IdeaProjects/AltairKit_1_8_8/target/AltairKit-1.8.8.jar -DgroupId=dev.mikan -DartifactId=AltairKit -Dversion=1.8.8 -Dpackaging=jar

        registerCommands(new TestCommands());
//        AltairKit.tabComplete("give item",words);
//        enableGUIManager(this);
//
//        JsonFile file = new JsonFile(this,"test");
//        file.set("t1.t2.t3.t4.t5","END");

    }


    public static void enableGUIManager(JavaPlugin plugin){
        dev.mikan.altairkit.api.gui.Data.listeningToAltairGUIs = true;
        Bukkit.getPluginManager().registerEvents(new InventoryClickEventListener(),plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseEventListener(),plugin);
    }


    public static void disableGUIManager(JavaPlugin plugin){
        Data.listeningToAltairGUIs = false;
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }


    public static void registerCommand(AltairCommand command){
        CmdMap.getCommandMap().register("", command);
    }



    public static ItemStack head(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = ((CraftPlayer)player).getProfile();

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
            meta.setOwner(player.getName());
            skull.setItemMeta(meta);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return skull;
    }

    public static ItemStack head(String name){
        return getCustomHead(SkinFetcher.getSkinValueFromNick(name));
    }

    public static ItemStack getCustomHead(String texture){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); // 3 = player head
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skull.setItemMeta(meta);
        return skull;
    }


    public static void registerCommand(Object object){
        Commands.registerCommands(object);
    }

    public static void registerCommands(Object ...cmdClasses){
        for (Object cmdClass : cmdClasses) {
            Commands.registerCommands(cmdClass);
        }

    }

    public static void tabComplete(String command,String ...suggestions){
        Commands.tabComplete(command,suggestions);
    }

    public static Gson buildGson(){
        return AltairGsonFactory.createGson();
    }

    public static String colorize(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static List<String> colorize(List<String> lines){
        List<String> colorizedList = new ArrayList<>();
        lines.forEach(line -> colorizedList.add(ChatColor.translateAlternateColorCodes('&',line)));
        return colorizedList;
    }

}
