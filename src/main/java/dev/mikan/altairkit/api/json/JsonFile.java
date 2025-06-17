package dev.mikan.altairkit.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonFile extends JsonSection {


    public JsonFile(File dataFolder, String name) {
        super(dataFolder, name);
    }

    public JsonFile(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    public JsonFile(Plugin plugin, String name) {
        super(plugin, name);
    }


    private JsonFile (File dataFolder,String name,JsonObject root,String path){
        super(dataFolder,name,root,path);
        this.root = root;
    }


    @Override
    public JsonSection getSection(String path){
        JsonElement element = getJsonValue(this.root,path);
        if (element == null || !element.isJsonObject()) return null;

        return new JsonFile(this.dataFolder,this.fileName,element.getAsJsonObject(),path);
    }




    /*
     * Allows to put a key and a value in a json file
     *
     * @Param
     * path: the path of the value
     *   example: "key1.key2.key3"
     *
     * value: the value you want to associate with the key
     * */
    @Override
    public void set(String path,Object value){
        JsonObject root;
        try (Reader reader = new FileReader(file)){
            root = this.parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        updateJsonValue(root,path,value);

        try (Writer writer = new FileWriter(file)){
            this.gson.toJson(root,writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Allows user to get an object in a path
    // @param path :  "key.key1.key2"
    @Override
    public Object get(String path){
        update();
        JsonElement result = getJsonValue(this.root,path);
        return this.gson.fromJson(result,Object.class);
    }

    // Overload of get, use it to get objects from json files
    @Override
    public Object get(String path, Type type){
        update();
        JsonElement result = getJsonValue(root,path);
        return this.gson.fromJson(result,type);
    }

    // updates the file stream
    private void update(){
        try (Reader reader = new FileReader(file)){
            this.root = this.parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (this.sectionRoot != null)
            updateSection();
    }

    private void updateSection(){
        JsonElement sectionElement = getJsonValue(this.root,this.sectionPath) ;
        if (sectionElement == null) return;

        this.sectionRoot = sectionElement.getAsJsonObject();
        this.root = sectionRoot.getAsJsonObject();
    }

    // Gets all the keys contained in the json file
    @Override
    public Set<String> getKeys(boolean recursive){
        Set<String> keys = new HashSet<>();

        update();

        addKeys(this.root,keys,recursive);
        return keys;
    }
    // Helper method to add keys recursively
    private void addKeys(JsonObject jsonObject, Set<String> keys, boolean recursive) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
            if (recursive && entry.getValue().isJsonObject()) {
                // If the value is a nested JsonObject, recurse into it
                addKeys(entry.getValue().getAsJsonObject(), keys, true);
            }
        }
    }


    // returns true if the file is empty
    @Override
    public boolean isEmpty(){
        return this.file.length() == 2;
    }




    private void updateJsonValue(JsonObject jsonObject, String path, Object value) {
        String[] keys = path.split("\\.");
        JsonObject currentObject = jsonObject;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            JsonElement element = currentObject.get(key);

            if (element == null || !element.isJsonObject()) {
                JsonObject newObject = new JsonObject();
                currentObject.add(key, newObject);
                currentObject = newObject;
            } else {
                currentObject = element.getAsJsonObject();
            }
        }
        if (value == null){
            currentObject.remove(keys[keys.length - 1]);
            return;
        }
        if (value instanceof String)
            currentObject.addProperty(keys[keys.length - 1], (String) value);
        else {
            JsonElement objectElement = this.gson.toJsonTree(value);
            currentObject.add(keys[keys.length - 1],objectElement);
            try (FileWriter writer = new FileWriter(file)){
                this.gson.toJson(currentObject,writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static JsonElement getJsonValue(JsonObject jsonObject, String path) {
        String[] keys = path.split("\\.");
        JsonElement currentElement = jsonObject;

        for (String key : keys) {
            if (currentElement == null) {
                return null;
            }

            if (currentElement.isJsonArray()){
                currentElement = currentElement.getAsJsonArray();
            }else if (currentElement.isJsonObject()){
                currentElement = currentElement.getAsJsonObject().get(key);
            }

        }

        return currentElement;
    }
}