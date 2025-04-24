package com.p1nero.smc.gameasset.skill;

import com.p1nero.smc.gameasset.SMCAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

/**
 * 点燃周围敌人
 */
public class GoldenSpatulaInnate extends WeaponInnateSkill {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WeaponsOfMinecraft.MODID, "textures/gui/skills/demonic_ascension.png");
    public GoldenSpatulaInnate(Builder<?> builder) {
        super(builder);
        this.consumption = 120;
        this.maxStackSize = 1;
    }

    @Override
    public ResourceLocation getSkillTexture() {
        return TEXTURE;
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);
        executer.playAnimationSynchronized(SMCAnimations.ANTITHEUS_ASCENSION, 0.0F);
    }
}
