package com.p1nero.smc.mixin;

import com.google.common.collect.ImmutableMap;
import com.p1nero.smc.gameasset.SMCMobCombatBehaviors;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.Map;

@Mixin(value = HumanoidMobPatch.class, remap = false)
public abstract class HumanoidMobPatchMixin<T extends PathfinderMob> extends MobPatch<T> {
    @Shadow protected Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> weaponAttackMotions;

    /**
     * 修改原史诗战斗怪的连招逻辑
     */
    @Inject(method = "setWeaponMotions", at = @At("TAIL"))
    private void smc$setWeaponMotions(CallbackInfo ci){
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.AXE, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.HOE, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.PICKAXE, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SHOVEL, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SWORD, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, SMCMobCombatBehaviors.HUMANOID_ONEHAND_TOOLS, CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_DUAL_SWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_GREATSWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.UCHIGATANA, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_KATANA));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.LONGSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_LONGSWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.TACHI, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_TACHI));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SPEAR, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, SMCMobCombatBehaviors.HUMANOID_SPEAR_ONEHAND, CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_SPEAR_TWOHAND));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.FIST, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_FIST));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.DAGGER, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, SMCMobCombatBehaviors.HUMANOID_ONEHAND_DAGGER, CapabilityItem.Styles.TWO_HAND, SMCMobCombatBehaviors.HUMANOID_TWOHAND_DAGGER));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.RANGED, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_FIST));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.TRIDENT, ImmutableMap.of(CapabilityItem.Styles.COMMON, SMCMobCombatBehaviors.HUMANOID_SPEAR_ONEHAND));
    }
}
