package com.p1nero.smc.gameasset.skill;

import com.p1nero.smc.gameasset.SMCAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.effect.EpicFightMobEffects;

/**
 * 点燃周围敌人
 */
public class GoldenSpatulaInnate extends WeaponInnateSkill {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WeaponsOfMinecraft.MODID, "textures/gui/skills/demonic_ascension.png");
    public GoldenSpatulaInnate(Builder<?> builder) {
        super(builder);
    }

    @Override
    public ResourceLocation getSkillTexture() {
        return TEXTURE;
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);
        executer.playAnimationSynchronized(SMCAnimations.ANTITHEUS_ASCENSION, 0.15F);
        executer.getOriginal().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2));
    }
}
