package dev.mikan.altairkit.api.yml;

import dev.mikan.altairkit.utils.Singleton;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigManager implements Singleton {

    private final File dataFolder;

    public File getDataFolder() {
        return dataFolder;
    }
    private final Map<String,FileConfiguration> fileMap;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
        loadFolder();
        fileMap = new HashMap<>();
    }

    public ConfigManager(Plugin plugin) {
        this.dataFolder = plugin.getDataFolder();
        loadFolder();
        fileMap = new HashMap<>();
    }

    public ConfigManager(JavaPlugin plugin) {
        this.dataFolder = plugin.getDataFolder();
        loadFolder();
        fileMap = new HashMap<>();
    }

    /*
     * Gets a file configuration from a file.yml
     * in the given data folder
     * */
    public FileConfiguration get(String file){
        File f = new File(this.getDataFolder(),file);
        if (!f.exists()) return null;

        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        fileMap.putIfAbsent(file,config);
        return config;
    }



    /*
     * Saves the file given by path and name
     * */
    public void save(String file){
        FileConfiguration config = fileMap.get(file);
        if (config == null) {
            throw new IllegalArgumentException("The specified configuration file doesn't exist!");
        }
        try {
            config.save(new File(this.dataFolder, file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    /*
     * Loads a file
     *
     * If the file or the specified dirs don't exist
     * it will create it
     * */
    public void load(String filePath, JavaPlugin plugin) throws IOException {
        InputStream source = plugin.getResource(filePath);
        if (!filePath.endsWith(".yml"))
            filePath += ".yml";

        File file = new File(this.dataFolder,filePath);

        if (file.exists())return;

        if (filePath.contains("/")){
            Bukkit.getLogger().info("loading file dirs!");
            Bukkit.getLogger().info(file.getPath());
            File directories = new File(this.dataFolder,getDirsName(filePath));
            if (directories.mkdirs()){
                Bukkit.getLogger().info("File dirs successfully loaded!");
                if (file.createNewFile()){
                    Bukkit.getLogger().info("File successfully created!");
                    FileUtils.copyInputStreamToFile(source,file);
                }
            } else {
                Bukkit.getLogger().info("Couldn't load file dirs!");
                if (file.createNewFile()){
                    Bukkit.getLogger().info("File successfully created!");
                    FileUtils.copyInputStreamToFile(source,file);
                }
            }
        } else {
            file.createNewFile();
            FileUtils.copyInputStreamToFile(source,file);
            Bukkit.getLogger().info("yml file loaded!");
        }

        Bukkit.getLogger().info("yml file loaded!");

    }

    private String getDirsName(String path){
        List<String> substrings = new ArrayList<>(Arrays.asList(path.split("/")));
        StringBuilder builder = new StringBuilder();
        if (!substrings.isEmpty()) {
            substrings.remove(substrings.size() - 1); // Remove last
        }
        for (String dir : substrings) {
            builder.append(dir).append("/");
        }
        return builder.toString();
    }
    private void loadFolder(){
        if (!this.dataFolder.exists())
            dataFolder.mkdir();
    }

}
