package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEvent;

/**
 * 招架计数
 */
@Mixin(value = HurtEvent.class, remap = false)
public abstract class HurtEventMixin extends PlayerEvent<ServerPlayerPatch> {

    @Shadow private boolean parried;

    public HurtEventMixin(ServerPlayerPatch playerPatch, boolean cancelable) {
        super(playerPatch, cancelable);
    }

    @Inject(method = "setParried", at = @At("HEAD"))
    private void smc$setParry(boolean parried, CallbackInfo ci){
        if(!this.parried && parried) {
            SMCPlayer.addParryCount(this.getPlayerPatch().getOriginal());
        }
    }
}
