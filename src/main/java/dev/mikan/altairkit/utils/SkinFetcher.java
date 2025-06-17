package dev.mikan.altairkit.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class SkinFetcher {

    public static String getSkinValueFromNick(String nickname) {
        try {
            // STEP 1: Ottieni UUID dal nickname
            URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + nickname);
            HttpURLConnection uuidConnection = (HttpURLConnection) uuidUrl.openConnection();
            uuidConnection.setRequestMethod("GET");

            if (uuidConnection.getResponseCode() != 200) return null;

            InputStream uuidStream = uuidConnection.getInputStream();
            String uuidResponse = new BufferedReader(new InputStreamReader(uuidStream))
                    .lines().collect(Collectors.joining());
            uuidStream.close();
            var parser = new JsonParser();
            HttpURLConnection skinConnection = getHttpURLConnection(uuidResponse,parser);

            if (skinConnection.getResponseCode() != 200) return null;

            InputStream skinStream = skinConnection.getInputStream();
            String skinResponse = new BufferedReader(new InputStreamReader(skinStream))
                    .lines().collect(Collectors.joining());
            skinStream.close();


            JsonObject skinJson = parser.parse(skinResponse).getAsJsonObject();
            JsonArray properties = skinJson.getAsJsonArray("properties");
            if (properties.size() == 0) return null;

            JsonObject property = properties.get(0).getAsJsonObject();
            return property.get("value").getAsString();

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpURLConnection getHttpURLConnection(String uuidResponse,JsonParser parser) throws IOException {
        JsonObject uuidJson = parser.parse(uuidResponse).getAsJsonObject();
        String uuid = uuidJson.get("id").getAsString();

        URL skinUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        HttpURLConnection skinConnection = (HttpURLConnection) skinUrl.openConnection();
        skinConnection.setRequestMethod("GET");
        return skinConnection;
    }


}
