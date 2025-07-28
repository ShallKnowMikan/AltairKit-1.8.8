package dev.mikan.altairkit.utils;

import dev.mikan.altairkit.AltairKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LongMessage {

    private final List<String> message = new ArrayList<>();

    public LongMessage(List<String> startingMessage, Map<String, String> replaceMap) {

        if (replaceMap.isEmpty()) {
            message.addAll(AltairKit.colorize(startingMessage));
        } else {
            for (String line : startingMessage) {
                replaceMap.forEach((key,value) -> {
                    message.add(AltairKit.colorize(line.replace(key,value)));
                });
            }

        }
    }

    public List<String> toList(){
        return this.message;
    }
}
