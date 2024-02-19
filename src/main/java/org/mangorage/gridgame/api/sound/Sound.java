package org.mangorage.gridgame.api.sound;

public record Sound(String path) implements ISound {
    @Override
    public void play() {
        SoundAPI.playSound(path);
    }
}
