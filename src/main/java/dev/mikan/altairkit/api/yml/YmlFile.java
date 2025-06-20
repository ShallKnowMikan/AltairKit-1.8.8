package dev.mikan.altairkit.api.yml;

import dev.mikan.altairkit.utils.Singleton;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class YmlFile implements Singleton {

    private FileConfiguration file;

    public FileConfiguration getFile() {
        return file;
    }

    public YmlFile(String fileName, String dirName, JavaPlugin plugin){

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if (!fileName.endsWith(".yml"))
            fileName = fileName + ".yml";

        File test = new File(plugin.getDataFolder(), dirName + '/' + fileName);
        ConfigManager manager = new ConfigManager(plugin);
        if (!test.exists()) {
            File dir = new File(plugin.getDataFolder(), dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            try (InputStream in = plugin.getResource(dirName + '/' + fileName)){
                if (in != null) {
                    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("Created default settings file: " + file.getPath());
                } else {
                    plugin.getLogger().warning("Default Curfew.yml file not found in plugin resources.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while creating " + file.getName() + " file!", e);
                throw new RuntimeException(e);
            }
        }
        manager.save(dirName + '/' + fileName);

        this.file = manager.get(dirName+fileName);
    }

    public YmlFile(String fileName, String dirName, Plugin plugin){

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if (!fileName.endsWith(".yml"))
            fileName = new StringBuilder(fileName).append(".yml").toString();

        File test = new File(plugin.getDataFolder(), dirName + '/' + fileName);
        ConfigManager manager = new ConfigManager(plugin);
        if (!test.exists()) {
            File dir = new File(plugin.getDataFolder(), dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            try (InputStream in = plugin.getResource(dirName + '/' + fileName)){
                if (in != null) {
                    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("Created default settings file: " + file.getPath());
                } else {
                    plugin.getLogger().warning("Default Curfew.yml file not found in plugin resources.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while creating " + file.getName() + " file!", e);
                throw new RuntimeException(e);
            }
        }
        manager.save(dirName + '/' + fileName);

        this.file = manager.get(dirName+fileName);
    }


    public YmlFile(String fileName, JavaPlugin plugin) {

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if (!fileName.endsWith(".yml"))
            fileName = new StringBuilder(fileName).append(".yml").toString();

        File test = new File(plugin.getDataFolder(), fileName);
        ConfigManager manager = new ConfigManager(plugin);
        if (!test.exists()) {
            try (InputStream in = plugin.getResource(fileName)){
                if (in != null) {
                    Files.copy(in, test.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("Created default settings file: " + test.getPath());
                } else {
                    plugin.getLogger().warning("Default Curfew.yml file not found in plugin resources.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while creating " + test.getName() + " file!", e);
                throw new RuntimeException(e);
            }
        }

        manager.save(fileName);

        this.file = manager.get(fileName);
    }

    public YmlFile(String fileName, Plugin plugin) {

        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if (!fileName.endsWith(".yml"))
            fileName = fileName + ".yml";

        File test = new File(plugin.getDataFolder(), fileName);
        ConfigManager manager = new ConfigManager(plugin);
        if (!test.exists()) {
            try (InputStream in = plugin.getResource(fileName)){
                if (in != null) {
                    Files.copy(in, test.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("Created default settings file: " + test.getPath());
                } else {
                    plugin.getLogger().warning("Default Curfew.yml file not found in plugin resources.");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while creating " + test.getName() + " file!", e);
                throw new RuntimeException(e);
            }
        }

        manager.save(fileName);

        this.file = manager.get(fileName);
    }

}
