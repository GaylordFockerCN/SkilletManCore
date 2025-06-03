package com.p1nero.smc.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 降低凋零飞行高度
 */
@Mixin(WitherBoss.class)
public class WitherBossMixin {
    @Inject(method = "isPowered", at = @At("HEAD"), cancellable = true)
    private void smc$isPowered(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
}
