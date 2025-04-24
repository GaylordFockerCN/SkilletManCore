package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.item.custom.DirtPlateItem;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlateItem.class)
public abstract class PlateItemMixin extends Item {
    public PlateItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "giveBack", at = @At("HEAD"), remap = false)
    private void smc$use(ItemStack foodStack, CookedFoodData food, PlateItem.ReturnTarget target, CallbackInfo ci){
        if(target instanceof PlateItem.PlayerTarget playerTarget && playerTarget.player() instanceof ServerPlayer serverPlayer) {
            if(serverPlayer.serverLevel().isNight()) {
                SMCAdvancementData.finishAdvancement("pre_cook", serverPlayer);
                DirtPlateItem.giveScoreEffect(serverPlayer, food.score);
            }
        }
    }

}
