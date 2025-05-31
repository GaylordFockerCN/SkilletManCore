package com.p1nero.smc.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 移除物品耐久度
 */
@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "canBeDepleted", at = @At("HEAD"), cancellable = true)
    private void smc$damageable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
