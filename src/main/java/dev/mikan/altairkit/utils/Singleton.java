package dev.mikan.altairkit.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface Singleton {
    static <T extends Singleton> T getInstance(Class<T> clazz, Supplier<T> supplier) {
        return (T) Holder.get(clazz, supplier);
    }

    public static final class Holder {
        private static final Map<Class<?>, Singleton> INSTANCES = new ConcurrentHashMap<>();

        static <T extends Singleton> T get(Class<T> clazz, Supplier<T> supplier) {
            return (T) INSTANCES.computeIfAbsent(clazz, c -> supplier.get());
        }
    }
}


