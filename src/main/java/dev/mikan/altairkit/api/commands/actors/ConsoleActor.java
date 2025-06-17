package dev.mikan.altairkit.api.commands.actors;

import dev.mikan.altairkit.AltairKit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;



public class ConsoleActor implements CMDActor {

    private final ConsoleCommandSender actor;
    private final CommandSender sender;

    public ConsoleActor(CommandSender actor) {
        this.actor = (ConsoleCommandSender) actor;
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
        return null;
    }

    @Override
    public ConsoleCommandSender asConsole() {
        return this.actor;
    }

    @Override
    public CommandSender asSender() {
        return null;
    }

    @Override
    public String name() {
        return actor.getName();
    }

    @Override
    public boolean hasPermission(String perm) {
        return this.sender.hasPermission(perm);
    }
}
