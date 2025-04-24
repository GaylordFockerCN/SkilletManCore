package com.p1nero.smc.client.sound.player;

import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
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
public class WorkingMusicPlayer {

    private static WorkingMusic music;
    private static boolean isRecordPlaying;

    public static void playWorkingMusic() {
        Player player = Minecraft.getInstance().player;
        if (player != null && SMCSounds.WORKING_BGM != null && player.isAlive()) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
            if (music != null) {
                if (Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.RECORDS) <= 0) {
                    music = null;
                } else if (!smcPlayer.isWorking()) {
                    music.player = null;
                } else if (music.player == null && music.soundEvent == SMCSounds.WORKING_BGM.get()) {
                    music.player = player;
                }
            } else {
                if (smcPlayer.isWorking()) {
                    music = new WorkingMusic(SMCSounds.WORKING_BGM.get(), player, player.getRandom());
                } else {
                    music = null;
                }
            }

            if(!SMCConfig.ENABLE_WORKING_BGM.get()) {
                music = null;
            }

            if (music != null && !Minecraft.getInstance().getSoundManager().isActive(music)) {
                Minecraft.getInstance().getSoundManager().play(music);
            }
        }
    }

    public static boolean isPlaying() {
        return music != null && music.player != null;
    }

    public static void stopMusic() {
        if (music != null) {
            music.player = null;
        }
    }

    public static boolean isRecordPlaying() {
        return isRecordPlaying;
    }

    public static void setIsRecordPlaying(boolean isRecordPlaying) {
        WorkingMusicPlayer.isRecordPlaying = isRecordPlaying;
    }

    private static class WorkingMusic extends AbstractTickableSoundInstance {
        public Player player;
        private int ticksExisted = 0;
        public final SoundEvent soundEvent;

        public WorkingMusic(SoundEvent bgm, Player player, RandomSource random) {
            super(bgm, SoundSource.MUSIC, random);
            this.player = player;
            this.soundEvent = bgm;
            this.attenuation = Attenuation.NONE;
            this.looping = true;
            this.delay = 0;
            this.volume = 0.5F;
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }

        @Override
        public boolean canPlaySound() {
            return WorkingMusicPlayer.music == this;
        }

        public void tick() {
            if (player == null || !SMCCapabilityProvider.getSMCPlayer(player).isWorking()) {
                player = null;
                if (volume >= 0) {
                    volume -= 0.01F;
                } else {
                    WorkingMusicPlayer.music = null;
                }
            }

            if (ticksExisted % 100 == 0) {
                Minecraft.getInstance().getMusicManager().stopPlaying();
            }
            ticksExisted++;
        }
    }
}
