package com.p1nero.smc.mixin;

import com.p1nero.smc.client.sound.player.WorkingMusicPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    /**
     * 播放碟片的时候手动关默认bgm
     */
    @Inject(method = "playStreamingMusic(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/RecordItem;)V", at = @At("HEAD"), remap = false)
    private void smc$addRecord(SoundEvent soundEvent, BlockPos pos, RecordItem musicDiscItem, CallbackInfo ci) {
        if(soundEvent != null && musicDiscItem != null) {
            WorkingMusicPlayer.setIsRecordPlaying(true);
            WorkingMusicPlayer.stopMusic();
        }
        if(soundEvent == null) {
            WorkingMusicPlayer.setIsRecordPlaying(false);
            WorkingMusicPlayer.playWorkingMusic();
        }
    }
}
