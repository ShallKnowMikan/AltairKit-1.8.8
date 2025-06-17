package dev.mikan.altairkit.api.commands;

import dev.mikan.altairkit.utils.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandInterceptor implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage();
        if (!msg.startsWith("/")) return;

        String[] parts = msg.substring(1).split(" ");

        Bukkit.broadcastMessage(ChatColor.BLUE + "Parts: " + Arrays.toString(parts));


//        Bukkit.broadcastMessage(ChatColor.BLUE + "cmd: " + cmd);
//        if (cmd == null) return;
//        Bukkit.broadcastMessage(ChatColor.BLUE + "Args: " + Arrays.toString(Arrays.copyOfRange(parts, 1, parts.length)));
//        cmd.execute(e.getPlayer(), cmd.getLabel(), Arrays.copyOfRange(parts,1,parts.length));
    }

    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent e) {
        String buffer = e.getChatMessage();
        if (!buffer.startsWith("/")) return;

        String[] parts = buffer.substring(1).split(" ");
        String root = parts[0].toLowerCase();

        AltairCMD cmd = Commands.getRootCMD(root);
        if (cmd == null) return;

        List<String> completions = new ArrayList<>(e.getTabCompletions());

        if (parts.length == 1) {
            completions.addAll(cmd.getSubcommands().keySet());
        } else {
            if (cmd.getSubcommands().containsKey(parts[1].toLowerCase())) {
                String[] subArgs = Arrays.copyOfRange(parts, 1, parts.length);
                completions.addAll(cmd.tabComplete(e.getPlayer(), root, subArgs));
            }
        }

        e.getTabCompletions().clear();
        e.getTabCompletions().addAll(completions);
    }
}
