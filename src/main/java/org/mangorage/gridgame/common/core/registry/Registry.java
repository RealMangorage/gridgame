package org.mangorage.gridgame.common.core.registry;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import org.mangorage.gridgame.common.Events;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Registry<T> {
    public static <T> Registry<T> create(String ID) {
        return new Registry<>(ID);
    }

    private final String registryID;
    private final Map<String, Holder<? extends T>> holders = new LinkedHashMap<>(); /** Only queried once. in {@link Registry#register()} **/
    private final Byte2ObjectMap<Holder<? extends T>> holdersID = new Byte2ObjectArrayMap<>(); /** Only Queried for getting the Holder from Byte {@link Registry#getObject(byte)}  **/
    private final Object2ByteMap<T> registeredObjects = new Object2ByteArrayMap<>(); /** Only Queried for getting the Byte from Object {@link Registry#getID(T object)} **/
    private boolean frozen = false;
    private byte id = 0;

    private Registry(String registryID) {
        this.registryID = registryID;
        Events.REGISTER_EVENT.addListener(e -> register());
    }

    public <X extends T> Holder<X> register(String id, Supplier<X> supplier) {
        if (frozen) throw new IllegalStateException("Registry is frozen.");
        if (holders.containsKey(id)) throw new IllegalStateException("Already registered %s to registry of %s type".formatted(id, registryID));
        HolderImpl<T, X> holder = new HolderImpl<>(id, supplier);
        holders.put(id, holder);
        return holder;
    }

    void register() {
        frozen = true;
        holders.forEach((k, holder) -> {
            System.out.println("%s is being registered under ID %s".formatted(k, id));
            var object = ((HolderImpl<T, ? extends T>) holder).register();
            registeredObjects.put(object, id);
            holdersID.put(id, holder);
            id = (byte) (id + 1);
        });
    }

    public byte getID(T object) {
        return registeredObjects.getByte(object);
    }

    public T getObject(byte ID) {
        return getHolderByID(ID).get();
    }

    public Holder<? extends T> getHolderByObject(T object) {
        return getHolderByID(getID(object));
    }

    public Holder<? extends T> getHolderByID(byte id) {
        return holdersID.get(id);
    }
}