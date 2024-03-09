package org.mangorage.gridgame.common.core.bootstrap;

public record MenuResult(String IP, String username) {
    public boolean isBlank() {
        return IP.isBlank() || username.isBlank();
    }
}
