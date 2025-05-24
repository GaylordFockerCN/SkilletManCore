package com.p1nero.smc.client.sound.player;

import com.p1nero.smc.client.gui.screen.SMCEndScreen;
import com.p1nero.smc.client.sound.SMCSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

/**
 * 工作时的bgm
 */
public class WinMusicPlayer {

    private static WinMusic music;

    public static void playWinMusic() {
        Player player = Minecraft.getInstance().player;
        if (player != null && SMCSounds.WIN_BGM != null && player.isAlive()) {
            if (music != null) {
                if (Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.RECORDS) <= 0) {
                    music = null;
                }
            } else {
                if (canPlay()) {
                    music = new WinMusic(SMCSounds.WIN_BGM.get(), player.getRandom());
                }
            }

            if (music != null && !Minecraft.getInstance().getSoundManager().isActive(music)) {
                Minecraft.getInstance().getSoundManager().play(music);
            }
        }
    }

    public static boolean canPlay() {
        return Minecraft.getInstance().screen instanceof SMCEndScreen;
    }


    private static class WinMusic extends AbstractTickableSoundInstance {
        private int ticksExisted = 0;
        public final SoundEvent soundEvent;

        public WinMusic(SoundEvent bgm,  RandomSource random) {
            super(bgm, SoundSource.RECORDS, random);
            this.soundEvent = bgm;
            this.attenuation = Attenuation.NONE;
            this.looping = true;
            this.delay = 0;
            this.volume = 0.5F;
        }

        @Override
        public boolean canPlaySound() {
            return WinMusicPlayer.music == this;
        }

        public void tick() {
            if (!canPlay()) {
                if (volume >= 0) {
                    volume -= 0.01F;
                } else {
                    WinMusicPlayer.music = null;
                }
            }

            if (ticksExisted % 100 == 0) {
                Minecraft.getInstance().getMusicManager().stopPlaying();
            }
            ticksExisted++;
        }
    }
}
