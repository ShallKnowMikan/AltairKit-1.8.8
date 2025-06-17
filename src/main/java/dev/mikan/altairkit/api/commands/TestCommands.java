package dev.mikan.altairkit.api.commands;

import dev.mikan.altairkit.api.commands.actors.CMDActor;
import dev.mikan.altairkit.api.commands.annotations.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class TestCommands {

    public final static String[] words = new String[]{"ciao","marameo","sissiogamer"};


    @Command("mm ciao")
    @Description("testing my stuff")
    @Permission(value = "dev.mikan.test",blocking = false)
    @Sender(SenderType.PLAYER)
    public void hello(AltairCMD cmd, CMDActor actor, String message, int integer, @Default Player target, double doubleValue){
        target.sendMessage("Altair: " + ChatColor.BLUE + message + " " + integer+ " " + doubleValue);
    }


}
