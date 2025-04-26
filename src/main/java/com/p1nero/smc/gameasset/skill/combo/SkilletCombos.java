package com.p1nero.smc.gameasset.skill.combo;

import com.p1nero.invincible.api.events.TimeStampedEvent;
import com.p1nero.invincible.conditions.*;
import com.p1nero.invincible.skill.api.ComboNode;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.ai.epicfight.SMCCombatBehaviors;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.effect.EpicFightMobEffects;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkilletCombos {
    public static Skill DIAMOND_SKILLET_COMBO;
    @SubscribeEvent
    public static void BuildSkills(SkillBuildEvent event){
        ComboNode root = ComboNode.create();
        ComboNode a = ComboNode.create();
        ComboNode jumpAttack = ComboNode.createNode(() -> WOMAnimations.RUINE_EXPIATION).setNotCharge(true).addCondition(new JumpCondition()).setPriority(2);
        ComboNode dashAttack = ComboNode.createNode(() -> WOMAnimations.RUINE_EXPIATION).setNotCharge(true).addCondition(new SprintingCondition()).setPriority(1).setCanBeInterrupt(false);
        ComboNode auto1 = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_1);
        a.addConditionAnimation(jumpAttack).addConditionAnimation(dashAttack).addConditionAnimation(auto1);
        ComboNode aa = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_2);
        ComboNode aa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aa).addConditionAnimation(dashAttack);
        ComboNode aaa = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_3);
        ComboNode aaa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aaa).addConditionAnimation(dashAttack);
        ComboNode aaaa = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_4).setCanBeInterrupt(false);
        ComboNode aaaa_ = ComboNode.create().addConditionAnimation(jumpAttack).addConditionAnimation(aaaa).addConditionAnimation(dashAttack);
        ComboNode ab = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_1_POLVORA).setNotCharge(true);
        ComboNode aab = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_2_POLVORA).setNotCharge(true);
        ComboNode aaab = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_3_POLVORA).setNotCharge(true);
        ComboNode aaaaa = ComboNode.createNode(() -> WOMAnimations.SOLAR_AUTO_4_POLVORA).setNotCharge(true);
        ComboNode aaaab = ComboNode.createNode(() -> WOMAnimations.SOLAR_OBSCURIDAD_IMPACTO).setNotCharge(true);
        ComboNode aaaaba = ComboNode.createNode(() -> WOMAnimations.SOLAR_OBSCURIDAD_DINAMITA).setNotCharge(true);
        ComboNode aaaabaa = ComboNode.createNode(() -> WOMAnimations.SOLAR_BRASERO_OBSCURIDAD).setNotCharge(true)
                .addTimeEvent(new TimeStampedEvent(1.5F, (entityPatch -> {
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2));
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 200, 2));
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2));
                    entityPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 200, 2));
                })));
        ComboNode aaaabab = ComboNode.createNode(() -> WOMAnimations.SOLAR_BRASERO_INFIERNO).setNotCharge(true);
        ComboNode da = ComboNode.createNode(() -> WOMAnimations.TORMENT_AUTO_4).setNotCharge(true);
        ComboNode dab = ComboNode.createNode(() -> WOMAnimations.SOLAR_BRASERO_CREMATORIO).setNotCharge(true)
                .addTimeEvent(new TimeStampedEvent(0.3F, entityPatch -> {
                    if (entityPatch.getOriginal().level() instanceof ServerLevel serverLevel) {
                        LivingEntity entity = entityPatch.getOriginal();
                        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.0, 0.0, 0.0, 0.1);
                        serverLevel.playSound(null, entity.getX(), entity.getY() + 0.75, entity.getZ(), SoundEvents.PLAYER_HURT_ON_FIRE, SoundSource.BLOCKS, 1.0F, 0.5F);
                    }
                }))
                .addTimeEvent(new TimeStampedEvent(1.0F, SMCCombatBehaviors.PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE))
                .addTimeEvent(new TimeStampedEvent(1.5F, SMCCombatBehaviors.PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE))
                .addTimeEvent(new TimeStampedEvent(2.0F, SMCCombatBehaviors.PLAY_SOLAR_BRASERO_CREMATORIO_PARTICLE));
        a.key2(ab);
        a.key1(aa_);
        aa.key1(aaa_);
        aa.key2(aab);
        aaa.key1(aaaa_);
        aaa.key2(aaab);
        aaaa.key1(aaaaa);
        aaaa.key2(aaaab);
        ab.key1(aa_);
        aab.key1(aaa_);
        aaab.key1(aaaa_);
        aaaaa.key1(a);
        aaaab.key2(aaaab);
        aaaab.key1(aaaaba);
        aaaaba.key1(aaaabaa);
        aaaaba.key2(aaaabab);
        jumpAttack.key1(da);
        jumpAttack.key2(da);
        root.key1(a);
        root.key2(dashAttack);
        dashAttack.key1(da);
        dashAttack.key2(da);
        da.key1(a);
        da.key2(dab);
        dab.key1(a);
        SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(SkilletManCoreMod.MOD_ID);
        DIAMOND_SKILLET_COMBO = registryWorker.build("diamond_skillet_combo", DiamondSkilletCombo::new, DiamondSkilletCombo.createComboBasicAttack().setCombo(root).setShouldDrawGui(false));
    }
}
