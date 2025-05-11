package com.p1nero.smc.gameasset;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.epicfight.PotatoCannonWeaponCapability;
import com.p1nero.smc.gameasset.skill.SMCSkills;
import com.p1nero.smc.gameasset.skill.combo.SkilletCombos;
import com.p1nero.smc.gameasset.skill.combo.SpatulaCombos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMSkills;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CrossbowCapability;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCWeaponCapabilityPresets {
    public static final Function<Item, CapabilityItem.Builder> POTATO_CANNON = (item) ->
            RangedWeaponCapability.builder().zoomInType(CapabilityItem.ZoomInType.CUSTOM)
                    .addAnimationsModifier(LivingMotions.IDLE, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.KNEEL, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.WALK, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.RUN, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.SNEAK, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.SWIM, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.FLOAT, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.FALL, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.RELOAD, Animations.BIPED_CROSSBOW_RELOAD)
                    .addAnimationsModifier(LivingMotions.AIM, Animations.BIPED_CROSSBOW_AIM)
                    .addAnimationsModifier(LivingMotions.SHOT, Animations.BIPED_CROSSBOW_SHOT)
                    .constructor(PotatoCannonWeaponCapability::new);
    public static final Function<Item, CapabilityItem.Builder> IRON_SKILLET_V1 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.GREATSWORD_AUTO1,
                            Animations.GREATSWORD_AUTO2,
                            Animations.GREATSWORD_AIR_SLASH,
                            Animations.GREATSWORD_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> IRON_SKILLET_V3 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.GREATSWORD_AUTO1,
                            Animations.GREATSWORD_AUTO2,
                            Animations.GREATSWORD_DASH,
                            Animations.GREATSWORD_DASH,
                            Animations.GREATSWORD_DASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);


    public static final Function<Item, CapabilityItem.Builder> IRON_SKILLET_V5 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.GREATSWORD_AUTO1,
                            Animations.GREATSWORD_AUTO2,
                            Animations.GREATSWORD_DASH,
                            Animations.GREATSWORD_DASH,
                            Animations.GREATSWORD_DASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> EpicFightSkills.STEEL_WHIRLWIND)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SKILLET_V1 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.SOLAR_AUTO_3,
                            WOMAnimations.SOLAR_AUTO_3_POLVORA,
                            WOMAnimations.SOLAR_AUTO_2,
                            Animations.GREATSWORD_DASH,
                            Animations.GREATSWORD_DASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SKILLET_V3 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.SOLAR_AUTO_3,
                            WOMAnimations.SOLAR_AUTO_3_POLVORA,
                            WOMAnimations.SOLAR_AUTO_2,
                            WOMAnimations.RUINE_AUTO_3,
                            WOMAnimations.RUINE_AUTO_2)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SKILLET_V5 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.SOLAR_AUTO_3,
                            WOMAnimations.SOLAR_AUTO_3_POLVORA,
                            WOMAnimations.SOLAR_AUTO_2,
                            WOMAnimations.RUINE_AUTO_3,
                            WOMAnimations.RUINE_AUTO_2)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> SMCSkills.GOLDEN_SKILLET_INNATE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> DIAMOND_SKILLET = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SKILLET)
                    .hitSound(EpicFightSounds.BLUNT_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLUNT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.GREATSWORD_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> SkilletCombos.DIAMOND_SKILLET_COMBO)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> IRON_SPATULA_V1 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.LONGSWORD_AUTO1,
                            Animations.LONGSWORD_AUTO2,
                            Animations.LONGSWORD_DASH,
                            Animations.DAGGER_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);


    public static final Function<Item, CapabilityItem.Builder> IRON_SPATULA_V3 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.LONGSWORD_AUTO1,
                            Animations.LONGSWORD_AUTO2,
                            Animations.LONGSWORD_AUTO3,
                            WOMAnimations.HERRSCHER_AUTO_2,
                            Animations.LONGSWORD_DASH,
                            Animations.DAGGER_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> IRON_SPATULA_V5 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            Animations.LONGSWORD_AUTO1,
                            Animations.LONGSWORD_AUTO2,
                            Animations.LONGSWORD_AUTO3,
                            WOMAnimations.HERRSCHER_AUTO_2,
                            WOMAnimations.HERRSCHER_AUTO_3,
                            Animations.LONGSWORD_DASH,
                            Animations.DAGGER_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SPATULA_V1 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.AGONY_AUTO_1,
                            WOMAnimations.HERRSCHER_AUTO_3,
                            WOMAnimations.AGONY_AUTO_3,
                            Animations.LONGSWORD_DASH,
                            Animations.DAGGER_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SPATULA_V3 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.AGONY_AUTO_1,
                            WOMAnimations.HERRSCHER_AUTO_3,
                            WOMAnimations.AGONY_AUTO_3,
                            WOMAnimations.AGONY_CLAWSTRIKE,
                            WOMAnimations.HERRSCHER_VERDAMMNIS,
                            WOMAnimations.HERRSCHER_AUSROTTUNG)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> null)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> GOLDEN_SPATULA_V5 = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.AGONY_AUTO_1,
                            WOMAnimations.HERRSCHER_AUTO_3,
                            WOMAnimations.AGONY_AUTO_3,
                            WOMAnimations.AGONY_CLAWSTRIKE,
                            WOMAnimations.HERRSCHER_VERDAMMNIS,
                            WOMAnimations.HERRSCHER_AUSROTTUNG)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemStack -> SMCSkills.GOLDEN_SPATULA_INNATE))
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                    .comboCancel((style) -> false);

    public static final Function<Item, CapabilityItem.Builder> DIAMOND_SPATULA = (item) ->
            (CapabilityItem.Builder) WeaponCapability.builder().category(CapabilityItem.WeaponCategories.SWORD)
                    .styleProvider((playerPatch) -> CapabilityItem.Styles.TWO_HAND).collider(SMCColliders.SPATULA)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .hitParticle(EpicFightParticles.HIT_BLADE.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
                    .canBePlacedOffhand(false)
                    .newStyleCombo(
                            CapabilityItem.Styles.TWO_HAND,
                            WOMAnimations.HERRSCHER_AUSROTTUNG)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemStack -> SpatulaCombos.DIAMOND_SPATULA_COMBO))
                    .passiveSkill(WOMSkills.LUNAR_ECHO_PASSIVE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, WOMAnimations.STAFF_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                    .comboCancel((style) -> false);

    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v1"), IRON_SKILLET_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v3"), IRON_SKILLET_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v5"), IRON_SKILLET_V5);

        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_skillet_v1"), GOLDEN_SKILLET_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_skillet_v3"), GOLDEN_SKILLET_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_skillet_v5"), GOLDEN_SKILLET_V5);

        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "diamond_skillet"), DIAMOND_SKILLET);

        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v1"), IRON_SPATULA_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v3"), IRON_SPATULA_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v5"), IRON_SPATULA_V5);

        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_spatula_v1"), GOLDEN_SPATULA_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_spatula_v3"), GOLDEN_SPATULA_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "golden_spatula_v5"), GOLDEN_SPATULA_V5);

        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "diamond_spatula"), DIAMOND_SPATULA);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "potato_cannon"), POTATO_CANNON);
    }

}
