package dev.mikan.altairkit.utils;


import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public abstract class Module implements Singleton {

    protected Set<Listener> listeners;
    @Getter private final Plugin plugin;

    @Getter
    private final String name;
    @Getter
    private final Logger logger;
    @Getter
    protected boolean loaded = false;



    public Module(Plugin plugin, String name, Logger logger) {
        this.plugin = plugin;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Module name cannot be blank");
        }

        this.name = name;
        this.logger = logger;

    }


    protected void listen(Listener... listeners){
        this.listeners = new HashSet<> (Arrays.asList(listeners));
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    protected void listen(Set<Listener>  listeners){
        this.listeners = listeners;
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();

    public abstract void loadConfig();


    public abstract void registerCommands();
    public abstract void registerListeners();

    public void info(String message,Object...params){
        logger.info(processMessage(message,params));
    }

    public void error(String message,Object...params){
        logger.warning(processMessage(message,params));
    }

    public void warning(String message,Object...params){
        logger.severe(processMessage(message,params));
    }

    public void unregisterListeners(){
        for (Listener listenerClass : this.listeners) {
            HandlerList.unregisterAll(listenerClass);
        }
        this.listeners.clear();
    }

    private String processMessage(String msg, Object... params) {
        for (Object param : params) {
            msg = msg.replaceFirst("\\{}",param.toString());
        }
        return  "["+ plugin.getName()+" -> "+name+"] "+msg;
    }
}



