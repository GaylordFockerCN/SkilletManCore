package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = SkillContainer.class, remap = false)
public abstract class SkillContainerMixin {

    @Shadow public abstract PlayerPatch<?> getExecuter();

    @Shadow private PlayerPatch<?> executor;

    @Inject(method = "setSkill(Lyesman/epicfight/skill/Skill;Z)Z", at = @At("HEAD"))
    private void smc$setSkill(Skill skill, boolean initialize, CallbackInfoReturnable<Boolean> cir){
        if(executor != null && !executor.isLogicalClient() && skill != null){
            ResourceLocation name = skill.getRegistryName();
            SMCAdvancementData.finishAdvancement("skill_adv_" +name.getNamespace() + "_" + name.getPath(), ((ServerPlayerPatch) executor).getOriginal());
        }
    }
}
