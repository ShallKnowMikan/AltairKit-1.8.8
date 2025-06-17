package dev.mikan.altairkit.api.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AltairCommand extends BukkitCommand {

    public static final Set<AltairCommand> commands = new HashSet<>();

    public final List<AltairCommand> subcommands = new ArrayList<>();
    public final List<String> subcommandNames = new ArrayList<>();

    public AltairCommand(String name) {
        super(name);
        commands.add(this);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if (!subcommands.isEmpty() && strings.length > 0) {
            processSubcommands(commandSender,strings);
            return true;
        }
        this.onPerforms(commandSender,strings);
        if (commandSender instanceof Player)
            this.onPlayerPerforms((Player) commandSender,strings);
        else if (commandSender instanceof ConsoleCommandSender)
            this.onConsolePerforms((ConsoleCommandSender) commandSender,strings);
        return true;

    }

    public abstract void onPlayerPerforms(Player player, String[] args);
    public abstract void onConsolePerforms(ConsoleCommandSender consoleCommandSender, String[] args);
    public abstract void onPerforms(CommandSender sender, String[] args);

    public void addSubCommand(AltairCommand command){
        subcommands.add(command);
        subcommandNames.add(command.getName());
    }

    public void addAlias(String alias){
        this.getAliases().add(alias);
    }

    private static String[] skipFirstArrayElement(String[] array) {
        if (array == null || array.length == 0 || array.length <= 1) {
            return new String[0];
        }

        String[] newArr = new String[array.length - 1];
        System.arraycopy(array, 1, newArr, 0, array.length - 1);
        return newArr;
    }

    private void processSubcommands(CommandSender sender,String[] args){

        this.subcommands.forEach(command -> {

            if (command.getName().equalsIgnoreCase(args[0]) || command.getAliases().contains(args[0])) {
                String[] updatedArgs = skipFirstArrayElement(args);

                if (sender instanceof Player) {

                    command.execute(sender,command.getName(), updatedArgs);
                } else if (sender instanceof ConsoleCommandSender) {
                    command.execute(sender,command.getName(), updatedArgs);
                }
            }
        });
    }

    public static Set<AltairCommand> getCommands(){
        return commands;
    }

    public List<AltairCommand> getSubcommands() {
        return subcommands;
    }

    public List<String> getSubcommandNames() {
        return subcommandNames;
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
