package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;

@Mixin(value = Skill.class, remap = false)
public abstract class SkillMixin {
    @Shadow public abstract ResourceLocation getRegistryName();

    @Inject(method = "onInitiate", at = @At("HEAD"))
    private void smc$onInitiate(SkillContainer container, CallbackInfo ci){
        if(!container.getExecuter().isLogicalClient()){
            SMCAdvancementData.finishAdvancement("skill_adv_" + this.getRegistryName().getNamespace() + "_" + this.getRegistryName().getPath(), ((ServerPlayer) container.getExecuter().getOriginal()));
        }
    }
}
