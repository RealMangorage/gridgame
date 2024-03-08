package org.mangorage.gridgame.common.core.registry;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import org.mangorage.gridgame.common.Events;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registry<T> {
    public static <T> Registry<T> create(String ID) {
        return new Registry<>(ID);
    }

    private final String registryID;
    private final Map<String, Holder<? extends T>> holdersMap = new LinkedHashMap<>();
    private final Byte2ObjectMap<Holder<? extends T>> holdersMap_Backed = new Byte2ObjectLinkedOpenHashMap<>();
    private boolean frozen = false;

    private final Byte2ObjectMap<T> registryMap = new Byte2ObjectArrayMap<>();
    private final Object2ByteMap<T> registry_reversed = new Object2ByteArrayMap<>();
    private byte id = 0;



    private Registry(String registryID) {
        this.registryID = registryID;
        Events.REGISTER_EVENT.addListener(e -> register());
    }

    public <X extends T> Holder<X> register(String id, Supplier<X> supplier) {
        if (frozen) throw new IllegalStateException("Registry is frozen.");
        if (holdersMap.containsKey(id)) throw new IllegalStateException("Already registered %s to registry of %s type".formatted(id, registryID));
        HolderImpl<T, X> holder = new HolderImpl<>(id, supplier);
        holdersMap.put(id, holder);
        return holder;
    }

    void register() {
        frozen = true;
        holdersMap.forEach((k, holder) -> {
            System.out.println("%s is being registered under ID %s".formatted(k, id));
            var object = ((HolderImpl<T, ? extends T>) holder).register();
            registryMap.put(id, object);
            registry_reversed.put(object, id);
            holdersMap_Backed.put(id, holder);
            id = (byte) (id + 1);
        });
    }

    public byte getID(T object) {
        return registry_reversed.getByte(object);
    }

    public Holder<? extends T> getHolderByObject(T object) {
        return holdersMap_Backed.get(getID(object));
    }

    public T getObject(byte ID) {
        return registryMap.get(ID);
    }
}