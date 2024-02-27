package org.mangorage.gridgame.common;

import com.pploder.events.SimpleEvent;
import org.mangorage.gridgame.common.events.LoadEvent;
import org.mangorage.gridgame.common.events.RegisterEvent;
import org.mangorage.gridgame.common.events.RegisterRenderersEvent;
import org.mangorage.gridgame.common.events.RenderEvent;

public class Events {
    public static final SimpleEvent<LoadEvent> LOAD_EVENT = new SimpleEvent<>();
    public static final SimpleEvent<RegisterEvent> REGISTER_EVENT = new SimpleEvent<>();
    public static final SimpleEvent<RegisterRenderersEvent> REGISTER_RENDERERS = new SimpleEvent<>();
    public static final SimpleEvent<RenderEvent> RENDER_EVENT = new SimpleEvent<>();
}
