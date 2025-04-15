package com.p1nero.smc.gameasset;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.ai.epicfight.api.IModifyAttackSpeedEntityPatch;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCAnimations {


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(SkilletManCoreMod.MOD_ID, SMCAnimations::build);
    }

    private static void build() {
        HumanoidArmature biped = Armatures.BIPED;


        if (WOMAnimations.TIME_TRAVEL != null) {
            WOMAnimations.TIME_TRAVEL.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, ((dynamicAnimation, livingEntityPatch, v, v1, v2) -> {
                if (livingEntityPatch instanceof IModifyAttackSpeedEntityPatch patch) {
                    return patch.getAttackSpeed();
                }
                return 1.0F;
            }));
        }

    }

}


