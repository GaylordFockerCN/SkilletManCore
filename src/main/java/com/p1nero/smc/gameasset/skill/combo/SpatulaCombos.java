package com.p1nero.smc.gameasset.skill.combo;

import com.p1nero.invincible.api.events.TimeStampedEvent;
import com.p1nero.invincible.conditions.CustomCondition;
import com.p1nero.invincible.conditions.JumpCondition;
import com.p1nero.invincible.conditions.SprintingCondition;
import com.p1nero.invincible.skill.api.ComboNode;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.gameasset.SMCAnimations;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.effect.EpicFightMobEffects;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpatulaCombos {
    public static Skill DIAMOND_SPATULA_COMBO;
    @SubscribeEvent
    public static void BuildSkills(SkillBuildEvent event){
        ComboNode root = ComboNode.create();
        ComboNode a = ComboNode.create();
        ComboNode jumpAttack = ComboNode.createNode(() -> WOMAnimations.HERRSCHER_AUSROTTUNG).setNotCharge(true).addCondition(new JumpCondition()).setPriority(3);
        ComboNode slam = ComboNode.createNode(() -> WOMAnimations.RUINE_COMET).setNotCharge(true).addCondition(new CustomCondition() {
            @Override
            public boolean predicate(LivingEntityPatch<?> entityPatch) {
                return !entityPatch.getOriginal().onGround();
            }
        }).setPriority(2);
        ComboNode rise = ComboNode.createNode(() -> WOMAnimations.HERRSCHER_AUSROTTUNG).setNotCharge(true);
        ComboNode dashAttack = ComboNode.createNode(() -> WOMAnimations.AGONY_CLAWSTRIKE).setPlaySpeed(0.7F).setNotCharge(true).addCondition(new SprintingCondition()).setPriority(1).setCanBeInterrupt(false);
        ComboNode auto1 = ComboNode.createNode(() -> WOMAnimations.AGONY_AUTO_1);
        a.addConditionAnimation(jumpAttack).addConditionAnimation(slam).addConditionAnimation(dashAttack).addConditionAnimation(auto1);
        ComboNode aa = ComboNode.createNode(() -> WOMAnimations.HERRSCHER_AUTO_3);
        ComboNode aa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aa).addConditionAnimation(dashAttack);
        ComboNode aaa = ComboNode.createNode(() -> WOMAnimations.AGONY_AUTO_3);
        ComboNode aaa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aaa).addConditionAnimation(dashAttack);
        ComboNode aaaa = ComboNode.createNode(() -> WOMAnimations.AGONY_AUTO_4).setCanBeInterrupt(false);
        ComboNode skill = ComboNode.createNode(() -> SMCAnimations.ANTITHEUS_ASCENSION).setCanBeInterrupt(false)
                .addTimeEvent(new TimeStampedEvent(1.5F, (entityPatch -> {
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2));
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 200, 2));
                })));
        ComboNode aaaa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aaaa).addConditionAnimation(dashAttack);
        ComboNode ab = ComboNode.createNode(() -> WOMAnimations.MOONLESS_BYPASS).setNotCharge(true);
        ComboNode aba = ComboNode.createNode(() -> WOMAnimations.MOONLESS_REVERSED_BYPASS).setNotCharge(true);
        ComboNode aab = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_1_POLVORA).setNotCharge(true);
        ComboNode aaab = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_1_POLVORA).setNotCharge(true);
        ComboNode da = ComboNode.createNode(() -> WOMAnimations.AGONY_RISING_EAGLE).setNotCharge(true);
        ComboNode daa = ComboNode.createNode(() -> WOMAnimations.AGONY_SKY_DIVE).setNotCharge(true);
        a.key2(ab);
        a.key1(aa_);
        aa.key1(aaa_);
        aa.key2(aab);
        aaa.key1(aaaa_);
        aaa.key2(aaab);
        aaaa.key2(skill);
        ab.key1(aba);
        ab.key2(ab);
        aba.key1(aba);
        aba.key2(ab);
        aab.key1(aaa_);
        aaab.key1(aaaa_);
        jumpAttack.key1(rise);
        jumpAttack.key2(slam);
        rise.key1(rise);
        rise.key2(slam);
        root.key1(a);
        root.key2(dashAttack);
        dashAttack.key1(da);
        dashAttack.key2(da);
        da.key1(daa);
        da.key2(slam);
        SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(SkilletManCoreMod.MOD_ID);
        DIAMOND_SPATULA_COMBO = registryWorker.build("diamond_spatula_combo", DiamondSpatulaCombo::new, DiamondSpatulaCombo.createComboBasicAttack().setCombo(root).setShouldDrawGui(false));
    }
}
