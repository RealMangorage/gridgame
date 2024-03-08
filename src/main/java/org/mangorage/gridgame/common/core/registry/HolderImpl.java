package org.mangorage.gridgame.common.core.registry;


import java.util.function.Supplier;

final class HolderImpl<T, X extends T> implements Holder<X> {
    private final String ID;
    private final Supplier<X> supplier;
    private X object;

    HolderImpl(String ID, Supplier<X> supplier) {
        this.ID = ID;
        this.supplier = supplier;
    }

    T register() {
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
