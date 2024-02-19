package org.mangorage.gridgame.api.sound;

import org.mangorage.gridgame.api.CacheAPI;

import javax.sound.sampled.AudioSystem;


public class SoundAPI {
    public static void playSound(String path) {
        var sfx = CacheAPI.getResourceStreamInternalAsBuffer(path);
        if (sfx == null)
            return;
        try {
            var audioStream = AudioSystem.getAudioInputStream(sfx);
            var clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
