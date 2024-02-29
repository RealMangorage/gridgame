package org.mangorage.mangonetwork.core;

public enum Side {
    SERVER,
    CLIENT;


    public Side getOpposite() {
        return getOpposite(this);
    }

    public static Side getOpposite(Side side) {
        return side == SERVER ? CLIENT : SERVER;
    }
}
