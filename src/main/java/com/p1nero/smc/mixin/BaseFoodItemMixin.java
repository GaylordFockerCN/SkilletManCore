package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaseFoodItem.class, remap = false)
public class BaseFoodItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void smc$finishUsingItem(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir){
        if(stack.hasTag() && stack.getOrCreateTag().getBoolean(SkilletManCoreMod.GUO_CHAO) && !level.isClientSide) {
            if(entity.getRandom().nextFloat() < 0.5F) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
            }
        }
        if(stack.hasTag() && stack.getOrCreateTag().getBoolean(SkilletManCoreMod.POISONED_SKILLET) && !level.isClientSide) {
            entity.setHealth(0.1F);
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
            entity.die(entity.damageSources().magic());
        }
    }
}
