package dev.mikan.altairkit.utils;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Module {

    protected Set<Listener> listeners;

    protected final Plugin plugin;

    private final String name;
    private final Logger logger;
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
            plugin.getServer().getPluginManager().registerEvents(listener,plugin);
        }
    }

    protected void listen(Set<Listener>  listeners){
        this.listeners = listeners;
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener,plugin);
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void loadConfig();


    public abstract void registerCommands(Plugin plugin);
    public abstract void registerListeners(Plugin plugin);

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void info(String message,Object...params){
        String msg = "["+name+"] "+message;
        logger.info(msg,params);
    }

    public void error(String message,Object...params){
        String msg = "["+name+"] "+message;
        logger.error(msg,params);
    }

    public void warning(String message,Object...params){
        String msg = "["+name+"] "+message;
        logger.warn(msg,params);
    }

    public Set<Listener> getListeners() {
        return listeners;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isLoaded() {
        return loaded;
    }
}



