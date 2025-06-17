package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;

public class SoundGlobal {
    public static void playGlobalSound(SoundEvent soundEvent, float volume, float pitch) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        SimpleSoundInstance sound = SimpleSoundInstance.forUI(soundEvent, volume, pitch);
        mc.getSoundManager().play(sound);
    }
}
