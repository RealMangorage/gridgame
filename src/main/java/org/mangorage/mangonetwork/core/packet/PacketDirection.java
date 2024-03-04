package org.mangorage.mangonetwork.core.packet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PacketDirection {
    PacketFlow flow();
}
