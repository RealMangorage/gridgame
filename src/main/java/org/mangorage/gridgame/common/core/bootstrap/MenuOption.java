package org.mangorage.gridgame.common.core.bootstrap;

public record MenuOption(String IP, String username) {
    public boolean isBlank() {
        return IP.isBlank() || username.isBlank();
    }
}
