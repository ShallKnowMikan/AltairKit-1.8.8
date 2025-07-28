package dev.mikan.altairkit.utils;

import dev.mikan.altairkit.api.commands.AltairCMD;
import dev.mikan.altairkit.api.commands.actors.CMDActor;
import dev.mikan.altairkit.api.commands.annotations.*;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

@UtilityClass
public final class Commands {

    final Map<String, AltairCMD> rootCMDs= new HashMap<>();


    public AltairCMD getRootCMD(String name){
        return rootCMDs.get(name.toLowerCase());
    }


    public void registerCommands(Object commandsObject){
        Class<?> cmdClass = commandsObject.getClass();
        // Loops through methods of given class
        for (Method method : cmdClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(Command.class)){
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) continue;

                Command command = method.getDeclaredAnnotation(Command.class);
                Description description = method.getDeclaredAnnotation(Description.class);
                Sender sender = method.getDeclaredAnnotation(Sender.class);
                Permission permission = method.getDeclaredAnnotation(Permission.class);
                Complete complete = method.getDeclaredAnnotation(Complete.class);

                // Saves just the root cmd
                // split method always return at least one element
                String[] cmdParts = command.value().split(" ");
                // if it has subcommands
                if (cmdParts.length > 1) {
                    if (!rootCMDs.containsKey(cmdParts[0])){
                        registerRoot(cmdParts[0],commandsObject,method,Arrays.copyOfRange(cmdParts,1,cmdParts.length),complete,permission,sender,description);
                    }

                    AltairCMD currentCMD = rootCMDs.get(cmdParts[0]);
                    /*
                    * Load command tree splitting command value by spaces
                    * @Command(root sub1 sub2) = [root,sub1,sub2]
                    *
                    * And always set the gotten method to the last subcommand
                    * So that if not specified with the @Usage() annotation
                    * the methods before in the tree will not do anything if called
                    * */
                    for (int i = 0; i < cmdParts.length; i++) {

                        boolean isLast = i == cmdParts.length - 1;

                        if (!isLast && !currentCMD.getSubcommands().containsKey(cmdParts[i + 1])) {
                            String[] args = Arrays.copyOfRange(cmdParts,2,cmdParts.length);
                            AltairCMD nextCMD = new AltairCMD(cmdParts[i + 1],null,null, args, complete,permission, sender ,description);
                            currentCMD.addSubCMD(nextCMD);
                            currentCMD = nextCMD;
                        } else if (isLast) {
                            currentCMD.setInstance(commandsObject);
                            currentCMD.setOnPerform(method);
                        } else {
                            // if is not last and the command instance already exists
                            currentCMD = currentCMD.getSubCMD(cmdParts[i + 1]);
                        }

                    }
                    // If command has no subcommands
                } else {
                    registerRoot(cmdParts[0],commandsObject,method,Arrays.copyOfRange(cmdParts,1,cmdParts.length),complete,permission,sender,description);
                }

            }
        }

    }

    /*
    * goes to last cmd-node and then sets the suggestions on tabComplete
    * */
    public void tabComplete(String command,String ...suggestions){
        if (!command.contains(" ")){
            AltairCMD cmd = rootCMDs.get(command);
            if (cmd == null) return;
            cmd.setSuggestions(suggestions);
            return;
        }

        String[] parts = command.split(" ");
        AltairCMD current = rootCMDs.get(parts[0]);
        if (current == null) return;

        // Goes to last node
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.getSubCMD(parts[i + 1]);
        }

        current.setSuggestions(suggestions);
    }

    private void registerRoot(String cmdName, Object instance, Method onPerform,String[] args,Complete complete, Permission permission, Sender sender, Description description){
        AltairCMD rootCMD = new AltairCMD(cmdName,instance,onPerform,args,complete,permission, sender, description);
        CmdMap.getCommandMap().register(cmdName,rootCMD);
        rootCMDs.put(cmdName, rootCMD);
    }

    public void invokeCommand(AltairCMD cmd, String[] args) throws Exception {
        Method m = cmd.getOnPerform();
        Object inst = cmd.getInstance();
        Object[] invokeArgs = buildMethod(cmd, args);

        m.invoke(inst, invokeArgs);
    }

    private Object[] buildMethod(AltairCMD cmd,String[] args){
        Method perform = cmd.getOnPerform();
        Parameter[] params = perform.getParameters();
        List<Object> objects = new ArrayList<>();

        CMDActor actor = cmd.getActor();

        objects.add(cmd);
        objects.add(actor);



        int nexrArg = 0;
        for (int i = 2; i < params.length; i++) {
            Parameter param = params[i];



            Player currentTarget = args.length > nexrArg ? Bukkit.getPlayerExact(args[nexrArg]) : null;
            if (param.getType().isAssignableFrom(Player.class)
                    && param.isAnnotationPresent(Default.class)
                    && currentTarget == null
                    && args.length + 2 != params.length){
                objects.add(actor.asPlayer());

            } else if (param.getType().isAssignableFrom(Player.class)) {

                objects.add(currentTarget);
                nexrArg++;
            } else if (param.getType().isAssignableFrom(String.class)){

                objects.add(args.length > nexrArg ? args[nexrArg] : "");
                nexrArg++;
            } else if (param.getType().isAssignableFrom(Integer.class)
                    || param.getType().isAssignableFrom(int.class)){

                try {
                    objects.add(args.length > nexrArg ? Integer.parseInt(args[nexrArg]) : 0);
                } catch (NumberFormatException e){
                    objects.add(args.length > nexrArg ? (int)Double.parseDouble(args[nexrArg]) : 0);
                }
                nexrArg++;
            } else if (param.getType().isAssignableFrom(Double.class)
                    || param.getType().isAssignableFrom(double.class)){

                objects.add(args.length > nexrArg ? Double.parseDouble(args[nexrArg]) : 0);
                nexrArg++;
            }
        }

        return objects.toArray();
    }

}
