package com.p1nero.smc.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 装备掉率 0
 */
@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "getEquipmentDropChance", at = @At("HEAD"), cancellable = true)
    private void smc$getEquipmentDropChance(EquipmentSlot p_21520_, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }

}
