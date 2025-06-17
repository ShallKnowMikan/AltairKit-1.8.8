package dev.mikan.altairkit.api.commands.actors;

import dev.mikan.altairkit.AltairKit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;



public class PlayerActor implements CMDActor {

    private final Player actor;
    private final CommandSender sender;

    public PlayerActor(CommandSender actor) {
        this.actor = (Player) actor;
        this.sender = actor;
    }

    @Override
    public void reply(String message,Object ...args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{}",arg.toString());
        }
        this.actor.sendMessage(AltairKit.colorize(message));
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public Player asPlayer() {
        return this.actor;
    }

    @Override
    public ConsoleCommandSender asConsole() {
        return null;
    }

    @Override
    public CommandSender asSender() {
        return this.sender;
    }

    @Override
    public boolean hasPermission(String perm) {
        return this.sender.hasPermission(perm);
    }

    @Override
    public String name() {
        return actor.getName();
    }

}
