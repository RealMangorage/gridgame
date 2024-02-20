package org.mangorage.gridgame.core.registry;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registry<T> {
    public static <T> Registry<T> create(String ID) {
        return new Registry<>(ID);
    }

    private static final class HolderImpl<T, X extends T> implements Holder<X> {
        private String ID;
        private final Supplier<X> supplier;
        private X object;

        private HolderImpl(String ID, Supplier<X> supplier) {
            this.supplier = supplier;
        }

        private T register() {
            object = supplier.get();
            return object;
        }

        @Override
        public X get() {
            return object;
        }

        @Override
        public String getID() {
            return ID;
        }
    }

    private final String registryID;
    private final Map<String, Holder<? extends T>> holdersMap = new LinkedHashMap<>();
    private boolean frozen = false;

    private final Byte2ObjectMap<T> registryMap = new Byte2ObjectArrayMap<>();
    private final Object2ByteMap<T> registry_reversed = new Object2ByteArrayMap<>();
    private byte id = 0;



    private Registry( String registryID) {
        this.registryID = registryID;
    }

    public <X extends T> Holder<X> register(String id, Supplier<X> supplier) {
        if (frozen) throw new IllegalStateException("Registry is frozen.");
        if (holdersMap.containsKey(id)) throw new IllegalStateException("Already registered %s to registry of %s type".formatted(id, registryID));
        HolderImpl<T, X> holder = new HolderImpl<>(id, supplier);
        holdersMap.put(id, holder);
        return holder;
    }

    public void register() {
        frozen = true;
        holdersMap.forEach((k, holder) -> {
            System.out.println("%s is being registered under ID %s".formatted(k, id));
            var object = ((HolderImpl<T, ? extends T>) holder).register();
            registryMap.put(id, object);
            registry_reversed.put(object, id);
            id = (byte) (id + 1);
        });
    }

    public byte getID(T object) {
        return registry_reversed.getByte(object);
    }

    public T getObject(byte ID) {
        return registryMap.get(ID);
    }
}
