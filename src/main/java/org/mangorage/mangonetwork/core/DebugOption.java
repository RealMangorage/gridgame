package org.mangorage.mangonetwork.core;

import java.util.function.Consumer;

public class DebugOption {
    private final Consumer<Object[]> printer;
    private boolean enabled = false;

    public DebugOption(Consumer<Object[]> printer) {
        this(printer, false);
    }

    public DebugOption(Consumer<Object[]> printer, boolean enabled) {
        this.printer = printer;
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

    public void print(Object... objects) {
        if (isEnabled())
            printer.accept(objects);
    }
}
