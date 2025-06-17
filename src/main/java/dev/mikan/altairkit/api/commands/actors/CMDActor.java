package dev.mikan.altairkit.api.commands.actors;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface CMDActor {

    List<String> args = new ArrayList<>();

    void reply(String message,Object ...args);
    boolean isPlayer();
    boolean isConsole();
    Player asPlayer();
    ConsoleCommandSender asConsole();
    CommandSender asSender();
    String name();
    boolean hasPermission(String perm);
    default List<String> args(){
        return args;
    }

}
