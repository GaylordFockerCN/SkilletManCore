package com.p1nero.smc.client.sound.player;

import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.client.sound.SMCSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

/**
 * 袭击时的bgm
 */
public class RaidMusicPlayer {

    private static RaidMusic music;

    public static void playRaidMusic() {
        Player player = Minecraft.getInstance().player;
        if (player != null && SMCSounds.RAID_BGM != null && player.isAlive()) {
            if (music != null) {
                if (Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.RECORDS) <= 0) {
                    music = null;
                } else if (!DataManager.inRaid.get(player)) {
                    music.player = null;
                } else if (music.player == null && music.soundEvent == SMCSounds.RAID_BGM.get()) {
                    music.player = player;
                }
            } else {
                if (DataManager.inRaid.get(player)) {
                    music = new RaidMusic(SMCSounds.RAID_BGM.get(), player, player.getRandom());
                } else {
                    music = null;
                }
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

    private static class RaidMusic extends AbstractTickableSoundInstance {
        public Player player;
        private int ticksExisted = 0;
        public final SoundEvent soundEvent;

        public RaidMusic(SoundEvent bgm, Player player, RandomSource random) {
            super(bgm, SoundSource.RECORDS, random);
            this.player = player;
            this.soundEvent = bgm;
            this.attenuation = Attenuation.NONE;
            this.looping = true;
            this.delay = 0;
            this.volume = 0.35F;
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }

        @Override
        public boolean canPlaySound() {
            return RaidMusicPlayer.music == this;
        }

        public void tick() {
            if (player == null || !DataManager.inRaid.get(player)) {
                player = null;
                if (volume >= 0) {
                    volume -= 0.01F;
                } else {
                    RaidMusicPlayer.music = null;
                }
            }

            if (ticksExisted % 100 == 0) {
                Minecraft.getInstance().getMusicManager().stopPlaying();
            }
            ticksExisted++;
        }
    }
}
