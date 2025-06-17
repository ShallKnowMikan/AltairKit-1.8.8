package dev.mikan.altairkit.api.commands.actors;

import dev.mikan.altairkit.AltairKit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class DefaultActor implements CMDActor {

    private final CommandSender actor;


    public DefaultActor(CommandSender actor) {
        this.actor = actor;
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
        return actor instanceof Player;
    }

    @Override
    public boolean isConsole() {
        return actor instanceof ConsoleCommandSender;
    }

    @Override
    public Player asPlayer() {
        return (Player) actor;
    }

    @Override
    public ConsoleCommandSender asConsole() {
        return (ConsoleCommandSender) actor;
    }

    @Override
    public CommandSender asSender() {
        return actor;
    }
    @Override
    public boolean hasPermission(String perm) {
        return this.actor.hasPermission(perm);
    }
    @Override
    public String name() {
        return actor.getName();
    }
}
