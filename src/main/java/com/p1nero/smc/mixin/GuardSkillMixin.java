package com.p1nero.smc.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

/**
 * 原版也能防御
 */
@Mixin(value = GuardSkill.class)
public class GuardSkillMixin {

    @Inject(method = "isExecutableState", at = @At("HEAD"), remap = false, cancellable = true)
    private void smc$isExecutable(PlayerPatch<?> executer, CallbackInfoReturnable<Boolean> cir) {
        ItemStack mainHand = executer.getValidItemInHand(InteractionHand.MAIN_HAND);
        if(EpicFightCapabilities.getItemStackCapability(mainHand) instanceof WeaponCapability && !executer.isBattleMode()) {
            cir.setReturnValue(true);
        }
    }
}
