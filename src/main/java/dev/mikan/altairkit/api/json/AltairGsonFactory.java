package dev.mikan.altairkit.api.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class AltairGsonFactory {


    public static Gson createGson(){

        GsonBuilder builder = new GsonBuilder();

        for (AltairTypeAdapter<?> adapter : AltairTypeAdapter.adapters){
            builder.registerTypeAdapter(adapter.getType(), adapter);
        }
        return builder.setPrettyPrinting().create();
    }

}
