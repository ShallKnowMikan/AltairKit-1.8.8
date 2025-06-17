package dev.mikan.altairkit.api.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


import java.io.IOException;
import java.util.*;


public abstract class AltairTypeAdapter<T> extends TypeAdapter<T> {

    public static Set<AltairTypeAdapter<?>> adapters = new HashSet<>();

    public AltairTypeAdapter(){
        boolean contains = false;

        for (AltairTypeAdapter<?> adapter : adapters){
            if (adapter.getClass().getName().equals(this.getClass().getName())) {
                contains = true;
                break;
            }
        }
        if (!contains)
            adapters.add(this);
    }


    public abstract Class<T> getType();


    /*
     *   Serializes an object who implements ConfigurationSerializable from bukkit
     *   by taking every field to serialize from the map gotten from the ConfigurationSerializable
     *   serialize method.
     *
     *   For every field it checks if it is a primitive type object, if it is it serializes it
     *   otherwise it will start going in recursion until it serializes all the primitive types
     *   into the objects in the fields.
     *
     * */
    public void serialize(String key, ConfigurationSerializable object, JsonWriter out) throws IOException {
        if (key != null) {
            out.name(key);
        }

        out.beginObject();

        Map<String, Object> serializedMap = object.serialize();
        for (Map.Entry<String, Object> entry : serializedMap.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            // Checks if it's a native type
            if (isNativeType(value.getClass())) {
                out.name(name).value(value.toString());
            } else {
                // otherwise we'll go in recursion until serialized all the complex objects found
                if (value instanceof ConfigurationSerializable) {
                    serialize(name, (ConfigurationSerializable) value, out);
                } else if (value instanceof Map) {
                    serialize(name, (Map<String, Object>) value, out);
                } else {
                    // Not supported type
                    Bukkit.getLogger().info("Error: " + value.getClass().getName() + " doesn't implement ConfigurationSerializable!");
                }
            }
        }

        out.endObject();
    }





    public void serializeArray(String key,ConfigurationSerializable[] objectArr,JsonWriter out) throws IOException {
        if (key != null)
            out.name(key);

        for (ConfigurationSerializable object : objectArr){

            if (object == null) continue;

            out.beginObject();

            Map<String,Object> serializedMap = object.serialize();
            for (String name : serializedMap.keySet()){
                // if the object is primitive we don't need to serialize it as well
                if (isNativeType(serializedMap.get(name).getClass())){
                    out.name(name).value(serializedMap.get(name).toString());
                } else {
                    // otherwise we'll go in recursion until serialized all the complex objects found
                    if (serializedMap.get(name).getClass().isAssignableFrom(ConfigurationSerializable.class)){
                        serialize(name, (ConfigurationSerializable) serializedMap.get(name),out);
                    } else if (serializedMap.get(name).getClass().getName().equals("java.util.HashMap")) {
                        Bukkit.getLogger().info("Instance of hashmap");
                        this.serialize(name, (Map<String, Object>) serializedMap.get(name), out);
                    } else {
                        Bukkit.getLogger().info("Error: " + serializedMap.get(name).getClass().getName() + " doesn't implement ConfigurationSerializable!");
                    }
                }
            }

            out.endObject();
        }

    }






    /*
     *   Serializes a Map<String,Object> by taking every field to serialize
     *   from the map gotten from the ConfigurationSerializable serialize method.
     *
     *   For every field it checks if it is a primitive type object, if it is it serializes it
     *   otherwise it will start going in recursion until it serializes all the primitive types
     *   into the objects in the fields.
     *
     * */
    public void serialize(String key,Map<String,Object> serializedMap,JsonWriter out) throws IOException {
        if (key != null)
            out.name(key);

        out.beginObject();

        // object.serialize returns a Map<String,Object>
        // Key/Name --> object
        for (String name : serializedMap.keySet()){
            // if the object is primitive we don't need to serialize it as well
            if (isNativeType(serializedMap.get(name).getClass())){
                out.name(name).value(serializedMap.get(name).toString());
            } else {
                // otherwise we'll go in recursion until serialized all the complex objects found
                Bukkit.getLogger().info("Condition: " + serializedMap.get(name).getClass().getName().equals("java.util.HashMap"));
                Bukkit.getLogger().info("serializedMap.get(name).getClass().getName(): " + serializedMap.get(name).getClass().getName());
                Bukkit.getLogger().info("java.util.HashMap");

                if (serializedMap.get(name).getClass().isAssignableFrom(ConfigurationSerializable.class)){
                    serialize(name, (ConfigurationSerializable) serializedMap.get(name),out);
                } else if (serializedMap.get(name).getClass().getName().equals("java.util.HashMap")) {
                    Bukkit.getLogger().info("Instance of hashmap");
                    this.serialize(name,(Map<String,Object>) serializedMap.get(name),out);
                }else {
                    Bukkit.getLogger().info("Error: " + serializedMap.get(name).getClass().getName() + " doesn't implement ConfigurationSerializable!");
                }
            }
        }
        out.endObject();
    }



    /*
     *   Serializes an object who implements Serializable from AltairKit
     *   by taking every field to serialize from the map gotten from the Serializable
     *   serialize method.
     *
     *   For every field it checks if it is a primitive type object, if it is it serializes it
     *   otherwise it will start going in recursion until it serializes all the primitive types
     *   into the objects in the fields.
     *
     * */
    public void serialize(String key, Serializable object, JsonWriter out) throws IOException {
        if (key != null)
            out.name(key);

        out.beginObject();


        // object.serialize returns a Map<String,Object>
        // Key/Name --> object
        Map<String,Object> serializedMap = object.serialize();
        for (String name : serializedMap.keySet()){
            // if the object is primitive we don't need to serialize it as well
            if (isNativeType(serializedMap.get(name).getClass())){
                out.name(name).value(serializedMap.get(name).toString());
            } else {
                // otherwise we'll go in recursion until serialized all the complex objects found
                if (serializedMap.get(name).getClass().isAssignableFrom(ConfigurationSerializable.class)){
                    serialize(name, (ConfigurationSerializable) serializedMap.get(name),out);
                } else if (serializedMap.get(name).getClass().getName().equals("java.util.HashMap")) {
                    this.serialize(name, (Map<String, Object>) serializedMap.get(name), out);
                } else {
                    System.out.println("Error: " + serializedMap.get(name).getClass().getName() + " doesn't implement ConfigurationSerializable!");
                }
            }
        }
        out.endObject();
    }





    /*
    *   Returns a map filled with a string as key and
    *   an object as value, taken from json.
    *
    * */
    public Map<String,Object> getData(JsonReader in) throws IOException {
        Map<String,Object> data = new HashMap<>();

        in.beginObject();

        while (in.hasNext()){
            String field = in.nextName();

            switch (in.peek()) {
                case STRING:
                    data.put(field, in.nextString());
                    break;
                case NUMBER:
                    data.put(field, in.nextDouble());
                    break;
                case BOOLEAN:
                    data.put(field, in.nextBoolean());
                    break;
                case BEGIN_OBJECT:
                    data.put(field, getData(in));
                    break;
                case BEGIN_ARRAY:
                    data.put(field, readArray(in));
                    break;
                default:
                    throw new IOException("Unexpected token in JSON");
            }
        }
        in.endObject();
        return data;
    }



    private boolean isNativeType(Class<?> type) {
        return type.isPrimitive() ||
                type.equals(Boolean.class) ||
                type.equals(Integer.class) ||
                type.equals(Double.class) ||
                type.equals(Float.class) ||
                type.equals(Long.class) ||
                type.equals(Short.class) ||
                type.equals(Byte.class) ||
                type.equals(Character.class) ||
                type.equals(String.class) ||
                type.getTypeName().equalsIgnoreCase("boolean") ||
                type.getTypeName().equalsIgnoreCase("int") ||
                type.getTypeName().equalsIgnoreCase("float") ||
                type.getTypeName().equalsIgnoreCase("String") ||
                type.getTypeName().equalsIgnoreCase("double");
    }


    private List<Object> readArray(JsonReader in) throws IOException {
        List<Object> list = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
            switch (in.peek()) {
                case STRING:
                    list.add(in.nextString());
                    break;
                case NUMBER:
                    list.add(in.nextDouble());
                    break;
                case BOOLEAN:
                    list.add(in.nextBoolean());
                    break;
                case BEGIN_OBJECT:
                    list.add(getData(in));
                    break;
                case BEGIN_ARRAY:
                    list.add(readArray(in));
                    break;
                default:
                    throw new IOException("Unexpected token in array");
            }
        }
        in.endArray();
        return list;
    }

    public List<Object> getArrayData(JsonReader in) throws IOException {
        List<Object> data = new ArrayList<>();

        in.beginArray();

        while (in.hasNext()){
            switch (in.peek()) {
                case STRING:
                    data.add(in.nextString());
                    break;
                case NUMBER:
                    data.add(in.nextDouble());
                    break;
                case BOOLEAN:
                    data.add(in.nextBoolean());
                    break;
                case BEGIN_OBJECT:
                    data.add(getData(in));
                    break;
                case BEGIN_ARRAY:
                    data.add(this.getArrayData(in));
                    break;
                default:
                    throw new IOException("Unexpected token in array");
            }
        }
        in.endArray();
        return data;
    }

}
