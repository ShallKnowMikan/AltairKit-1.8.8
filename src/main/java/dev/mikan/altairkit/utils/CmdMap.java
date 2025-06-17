package dev.mikan.altairkit.utils;

import dev.mikan.altairkit.api.commands.AltairCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public abstract class CmdMap {

    public static CommandMap getCommandMap(){
        try {
            final Field mapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            mapField.setAccessible(true);
            return (CommandMap) mapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to access the command map from Bukkit server.", e);
        }
    }

    public static void registerCommand(AltairCommand command){

        getCommandMap().register("", command);

    }


}
