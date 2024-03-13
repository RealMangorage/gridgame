package org.mangorage.mangonetwork.core;

public class DebugOption {
    private boolean enabled = false;

    public DebugOption() {
    }

    public DebugOption(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
