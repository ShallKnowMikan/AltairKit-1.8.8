package dev.mikan.altairkit.api.json;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class JsonSection {

    protected final File file;
    protected final Gson gson;
    protected final File dataFolder;
    protected final String fileName;
    protected final JsonParser parser = new JsonParser();
    protected JsonObject root;
    protected JsonObject sectionRoot;
    protected String sectionPath;


    protected JsonSection(File dataFolder,String name,JsonObject sectionRoot,String sectionPath){
        if (!name.contains(".json")) this.file = new File(dataFolder, name + ".json");
        else this.file = new File(dataFolder, name );

        this.fileName = name;
        this.gson = AltairGsonFactory.createGson();
        this.dataFolder = dataFolder;


        buildFolder(name);
        this.sectionRoot = sectionRoot;
        this.sectionPath = sectionPath;

        if (sectionRoot != null)
            this.root = sectionRoot;
    }

    public JsonSection(File dataFolder, String name){
        if (!name.contains(".json")) this.file = new File(dataFolder, name + ".json");
        else this.file = new File(dataFolder, name );

        this.fileName = name;
        this.gson = AltairGsonFactory.createGson();
        this.dataFolder = dataFolder;

        buildFolder(name);
    }

    public JsonSection(JavaPlugin plugin, String name){
        if (!name.contains(".json")) this.file = new File(plugin.getDataFolder(), name + ".json");
        else this.file = new File(plugin.getDataFolder(), name );

        this.fileName = name;
        this.gson = AltairGsonFactory.createGson();
        this.dataFolder = plugin.getDataFolder();

        buildFolder(name);
    }

    public JsonSection(Plugin plugin, String name){
        if (!name.contains(".json")) this.file = new File(plugin.getDataFolder(), name + ".json");
        else this.file = new File(plugin.getDataFolder(), name );

        this.fileName = name;
        this.gson = AltairGsonFactory.createGson();
        this.dataFolder = plugin.getDataFolder();

        buildFolder(name);
    }

    private void buildFolder(String name){
        if (!file.exists()) {
            createFolderAndFiles(name);
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(new JsonObject(), writer);
                System.out.println("plugin folder created!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try (Reader reader = new FileReader(file)) {
            this.root = this.parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFolderAndFiles(String name){
        if (!this.dataFolder.exists())
            this.dataFolder.mkdir();

        if (name.contains("/")){

            List<String> dirList = new ArrayList<>();
            for (int i = 0; i < name.split("/").length -1; i++) {
                String dirName = name.split("/")[i];
                StringBuilder lastDirs = new StringBuilder();
                dirList.forEach(dir -> lastDirs.append(dir).append("/"));
                File f = new File(this.dataFolder,lastDirs + dirName);
                if (!f.exists()) {

                    if (f.mkdir())
                        System.out.println("mkdir: " + dirName);
                    else {
                        System.out.println("ERROR: " + dirName);
                        System.out.println("ERROR: " + f.getName());
                        System.out.println("ERROR: " + f.getAbsolutePath());
                    }
                    dirList.add(dirName);
                }
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("IO EXEPTION - File: " + file);
            throw new RuntimeException(e);
        }

    }


    public abstract void set(String path,Object value);
    public abstract Object get(String path);
    public abstract Object get(String path, Type type);
    public abstract Set<String> getKeys(boolean recursive);
    public abstract boolean isEmpty();
    public abstract JsonSection getSection(String field);

    public File getDataFolder() {
        return dataFolder;
    }

    public File getFile() {
        return file;
    }

    public JsonObject getSectionRoot() {
        return sectionRoot;
    }

    public JsonObject getRoot() {
        return root;
    }

    public JsonParser getParser() {
        return parser;
    }

    public Gson getGson() {
        return gson;
    }
}
