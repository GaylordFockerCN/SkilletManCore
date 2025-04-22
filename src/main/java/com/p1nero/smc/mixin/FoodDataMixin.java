package com.p1nero.smc.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodData.class)
public class FoodDataMixin {

    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    private void smc$needsFood(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
