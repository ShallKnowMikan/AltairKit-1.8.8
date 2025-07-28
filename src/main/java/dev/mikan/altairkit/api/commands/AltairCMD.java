package dev.mikan.altairkit.api.commands;

import dev.mikan.altairkit.api.commands.actors.CMDActor;
import dev.mikan.altairkit.api.commands.actors.ConsoleActor;
import dev.mikan.altairkit.api.commands.actors.DefaultActor;
import dev.mikan.altairkit.api.commands.actors.PlayerActor;
import dev.mikan.altairkit.api.commands.annotations.Complete;
import dev.mikan.altairkit.api.commands.annotations.Description;
import dev.mikan.altairkit.api.commands.annotations.Permission;
import dev.mikan.altairkit.api.commands.annotations.Sender;
import dev.mikan.altairkit.utils.Commands;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class AltairCMD extends BukkitCommand {

    private @Setter Method onPerform;
    private @Setter Object instance;
    private final String permission;
    private final boolean isPermissionBlocking;
    private final SenderType senderType;
    private final String description;
    private final String[] params;
    private @Setter String[] suggestions = new String[]{};

    private CMDActor actor;

    private final Map<String,AltairCMD> subcommands = new HashMap<>();

    public AltairCMD(String name, Object instance, Method onPerform, String[] params, Complete complete, Permission permission, Sender senderType, Description description) {
        super(name);
        this.onPerform = onPerform;
        this.instance = instance;
        this.permission = permission == null ? "" : permission.value();
        this.isPermissionBlocking = permission != null && permission.blocking();
        this.senderType = senderType == null ? SenderType.ALL : senderType.value();
        this.description = description == null ? "" : description.value();
        if (complete != null)
            this.suggestions = List.of(complete.value().split(" ")).toArray(new String[0]);
        this.params = params;
    }

    @Override
    @SneakyThrows
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!permission.isEmpty() && !commandSender.hasPermission(permission) && isPermissionBlocking) return false;
        if (strings.length > 0 && subcommands.containsKey(strings[0])) {
            subcommands.get(strings[0]).execute(commandSender,s, Arrays.copyOfRange(strings,1,strings.length));
            return true;
        }



        if (instance == null || onPerform == null) return false;

        CMDActor actor;
        if (commandSender instanceof Player && senderType == SenderType.PLAYER)
            actor = new PlayerActor(commandSender);
        else if (commandSender instanceof ConsoleCommandSender && senderType == SenderType.CONSOLE)
            actor = new ConsoleActor(commandSender);
        else if (senderType == SenderType.ALL)
            actor = new DefaultActor(commandSender);
        else
            return false;

        this.actor = actor;

        Commands.invokeCommand(this,strings);
        return true;

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        AltairCMD current = this;
        if (args.length > 1 && !Arrays.stream(args).allMatch(String::isEmpty)) {
            /*
            * Using args.length - 1 because assuming a command tree like @Command("ciao mi chiamo mikan")
            * args will everytime be something like this: [arg1,arg2,lastARG, ""]
            * And since I need to get the subcommand suggestion from lastARG the loop
            * has to stop one element before its real last
            * */
            for (int i = 0; i < args.length - 1; i++) {
                List<String> nextArgs = StringUtil.copyPartialMatches(args[i], current.subcommands.keySet(), new ArrayList<>());
                String name = !nextArgs.isEmpty() ? nextArgs.get(0) : "";
                current = !name.isEmpty() ? current.subcommands.get(name) : current;
            }

        }

        return current.subcommands.isEmpty() ?
                current.suggestions.length == 0 ? super.tabComplete(sender,"",args) : Arrays.asList(current.suggestions)
                :
                StringUtil.copyPartialMatches(args[args.length-1],current.subcommands.keySet(),new ArrayList<>());
    }


    public AltairCMD getSubCMD(String name){
        return subcommands.get(name);
    }
    public void addSubCMD(AltairCMD cmd){
        this.subcommands.put(cmd.getName(),cmd);
    }
}
