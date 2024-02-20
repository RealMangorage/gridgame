package org.mangorage.gridgame.core.grid.entities;

public class EntityType<T extends Entity> {
    public static Class<EntityType<? extends Entity>> getClassType() {
        return ((Class<EntityType<? extends Entity>>) (Object) EntityType.class);
    }
}
