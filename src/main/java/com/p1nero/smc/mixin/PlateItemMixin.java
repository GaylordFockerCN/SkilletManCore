package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.item.custom.DirtPlateItem;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static com.p1nero.smc.datagen.SMCAdvancementData.FOOD_ADV_PRE;

@Mixin(value = PlateItem.class)
public abstract class PlateItemMixin extends Item {
    public PlateItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "giveBack", at = @At("HEAD"), remap = false)
    private void smc$giveBack(ItemStack foodStack, CookedFoodData food, PlateItem.ReturnTarget target, CallbackInfo ci){
        if(target instanceof PlateItem.PlayerTarget playerTarget) {
            if(playerTarget.player() instanceof ServerPlayer serverPlayer) {
                AtomicReference<PlateFood> plateFoodAtomicReference = new AtomicReference<>();
                if(Arrays.stream(PlateFood.values()).anyMatch(plateFood -> {
                    if(foodStack.is(plateFood.item.asItem())){
                        plateFoodAtomicReference.set(plateFood);
                        return true;
                    }
                    return false;
                })){
                    String name = FOOD_ADV_PRE + plateFoodAtomicReference.get().name().toLowerCase(Locale.ROOT);
                    SMCAdvancementData.finishAdvancement(name, serverPlayer);
                }

                if(serverPlayer.serverLevel().isNight()){
                    SMCAdvancementData.finishAdvancement("pre_cook", serverPlayer);
                }
            }
            DirtPlateItem.giveScoreEffect(playerTarget.player(), food.score);
        }
    }

}
