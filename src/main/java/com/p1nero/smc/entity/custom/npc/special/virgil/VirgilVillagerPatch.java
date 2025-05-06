package com.p1nero.smc.entity.custom.npc.special.virgil;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.p1nero.smc.gameasset.SMCAnimations;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.entitypatch.mob.VindicatorPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Set;

public class VirgilVillagerPatch extends VindicatorPatch<VirgilVillager> {

    @Override
    public void initAnimator(Animator animator) {
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.SIT, SMCAnimations.POWER_SIT);
    }

    //TODO 换攻击模板
    protected void setWeaponMotions() {
        super.setWeaponMotions();
//        this.weaponLivingMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, Set.of(Pair.of(LivingMotions.WALK, Animations.ILLAGER_WALK), Pair.of(LivingMotions.CHASE, Animations.BIPED_WALK_TWOHAND))));
//        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.AXE, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.VINDICATOR_ONEHAND));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.VINDICATOR_ONEHAND));
    }

    @Override
    protected void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if(!this.getOriginal().isFighting()){
            this.setAttakTargetSync(null);
        }
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        if(this.getOriginal().isSitting()){
            this.currentLivingMotion = LivingMotions.SIT;
            this.currentCompositeMotion = this.currentLivingMotion;
        } else {
            super.updateMotion(considerInaction);
        }
    }
}
