package org.mangorage.gridgame.core.sound;

public record Sound(String path) implements ISound {
    @Override
    public void play() {
        SoundAPI.playSound(path);
    }
}
