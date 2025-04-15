package com.p1nero.smc.gameasset;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCWeaponCapabilityPresets {

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

    @SubscribeEvent
    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v1"), IRON_SKILLET_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v3"), IRON_SKILLET_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_skillet_v5"), IRON_SKILLET_V5);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v1"), IRON_SPATULA_V1);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v3"), IRON_SPATULA_V3);
        event.getTypeEntry().put(new ResourceLocation(SkilletManCoreMod.MOD_ID, "iron_spatula_v5"), IRON_SPATULA_V5);
    }

}
