package com.p1nero.smc.gameasset.skill;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.passive.PassiveSkill;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCSkills {
    public static Skill BETTER_DODGE_DISPLAY;
    @SubscribeEvent
    public static void BuildSkills(SkillBuildEvent event){
        SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(SkilletManCoreMod.MOD_ID);
        BETTER_DODGE_DISPLAY = registryWorker.build("better_dodge_display", BetterDodgeDisplaySkill::new, PassiveSkill.createPassiveBuilder());

    }
}
