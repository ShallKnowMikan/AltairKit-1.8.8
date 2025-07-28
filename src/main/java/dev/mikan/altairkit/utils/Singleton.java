package dev.mikan.altairkit.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface Singleton {
    static <T extends Singleton> T getInstance(Class<T> clazz, Supplier<T> supplier) {
        return Holder.get(clazz, supplier);
    }

    public static final class Holder {
        private static final Map<Class<?>, Singleton> INSTANCES = new ConcurrentHashMap<>();

        static <T extends Singleton> T get(Class<T> clazz, Supplier<T> supplier) {
            Singleton instance = INSTANCES.get(clazz);
            if (instance == null) {
                synchronized (Holder.class) {
                    instance = INSTANCES.get(clazz);
                    if (instance == null) {
                        instance = supplier.get();
                        INSTANCES.put(clazz, instance);
                    }
                }
            }
            return clazz.cast(instance);
        }
    }
}