package org.mangorage.gridgame.core.events;

import com.pploder.events.Event;
import com.pploder.events.SimpleEvent;

public class Events {
    public static final Event<RegistryEvent> REGISTRY_EVENT = new SimpleEvent<>();
    public static final Event<TileRenderRegisterEvent> REGISTER_TILE_RENDERER_EVENT = new SimpleEvent<>();
    public static final Event<LoadCompleteEvent> LOAD_COMPLETE_EVENT = new SimpleEvent<>();
    public static final Event<TileChangeEvent> TILE_CHANGE_EVENT = new SimpleEvent<>();
}
