package dev.mikan.altairkit.utils;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class Module implements Singleton {

    @Getter
    protected Set<Listener> listeners;

    @Getter
    protected final Plugin pluginInstance;

    @Getter
    private final String name;
    @Getter
    private final java.util.logging.Logger logger;
    @Getter
    protected boolean loaded = false;
    protected final Map<Class<? extends Module>,Module> submodules = new ConcurrentHashMap<>();



    public Module(Plugin plugin, String name, java.util.logging.Logger logger) {
        this.pluginInstance = plugin;
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Module name cannot be blank");
        }

        this.name = name;
        this.logger = logger;

    }


    protected void listen(Listener... listeners){
        this.listeners = new HashSet<> (Arrays.asList(listeners));
        for (Listener listener : listeners) {
            pluginInstance.getServer().getPluginManager().registerEvents(listener, pluginInstance);
        }
    }

    protected void listen(Set<Listener>  listeners){
        this.listeners = listeners;
        for (Listener listener : listeners) {
            pluginInstance.getServer().getPluginManager().registerEvents(listener, pluginInstance);
        }
    }

    public abstract void onEnable();

    public abstract void onReload();
    public abstract void onDisable();

    public abstract void loadConfig();


    public abstract void registerCommands(Plugin plugin);
    public abstract void registerListeners(Plugin plugin);

    public FileConfiguration getConfig() {
        return pluginInstance.getConfig();
    }

    public void info(String message,Object...params){
        logger.info(processMessage(message,params));
    }

    public void error(String message,Object...params){
        logger.warning(processMessage(message,params));
    }

    public void warning(String message,Object...params){
        logger.severe(processMessage(message,params));
    }


    private String processMessage(String msg, Object... params) {
        for (Object param : params) {
            msg = msg.replaceFirst("\\{}",param.toString());
        }
        return  "["+ pluginInstance.getName()+" -> "+name+"] "+msg;
    }
}



