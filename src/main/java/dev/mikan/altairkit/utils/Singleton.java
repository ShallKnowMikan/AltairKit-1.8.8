package dev.mikan.altairkit.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Singleton {

    static <T extends Singleton> T getInstance(Class<T> clazz) {
        return Holder.get(clazz);
    }

    final class Holder {
        private static final Map<Class<?>, Singleton> INSTANCES = new ConcurrentHashMap<>();

        @SuppressWarnings("unchecked")
        static <T extends Singleton> T get(Class<T> clazz) {
            return (T) INSTANCES.computeIfAbsent(clazz, c -> {
                try {
                    return (Singleton) c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create singleton instance", e);
                }
            });
        }
    }
}


