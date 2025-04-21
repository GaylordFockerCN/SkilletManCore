package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

/**
 * 完美闪避计数
 */
@Mixin(value = ServerPlayerPatch.class, remap = false)
public abstract class ServerPlayerPatchMixin extends PlayerPatch<ServerPlayer> {

    @Inject(method = "onDodgeSuccess", at = @At("HEAD"))
    private void smc$onDodgeSuccess(DamageSource damageSource, CallbackInfo ci){
        SMCPlayer.addDodgeCount(this.getOriginal());
    }

}
