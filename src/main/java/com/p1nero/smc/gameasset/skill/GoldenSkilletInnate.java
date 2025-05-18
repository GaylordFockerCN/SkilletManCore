package com.p1nero.smc.gameasset.skill;

import com.p1nero.smc.item.custom.skillets.GoldenSkilletItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.Random;

/**
 * 点燃周围敌人
 */
public class GoldenSkilletInnate extends WeaponInnateSkill {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WeaponsOfMinecraft.MODID, "textures/gui/skills/true_berserk.png");
    public GoldenSkilletInnate(Builder<?> builder) {
        super(builder);
    }

    @Override
    public ResourceLocation getSkillTexture() {
        return TEXTURE;
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);
        executer.playAnimationSynchronized(WOMAnimations.TORMENT_BERSERK_CONVERT, 0.15F);
        executer.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 200, 2));
        executer.getOriginal().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2));
        executer.getOriginal().level().playSound(null, executer.getOriginal(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void executeOnClient(LocalPlayerPatch executor, FriendlyByteBuf args) {
        super.executeOnClient(executor, args);
        if(!(executor.getOriginal().getMainHandItem().getItem() instanceof GoldenSkilletItem)) {
            return;
        }
        float target_x = (float) executor.getOriginal().getX();
        float target_y = (float) executor.getOriginal().getY() + 0.2F;
        float target_z = (float) executor.getOriginal().getZ();
        int n = 80;
        double r = 0.6;
        double t = 0.05;

        int i;
        double theta;
        double phi;
        double x;
        double y;
        double z;
        Vec3f direction;
        OpenMatrix4f rotation;
        for(i = 0; i < n * 2; ++i) {
            theta = 6.283185307179586 * (new Random()).nextDouble();
            phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
            x = r * Math.cos(phi) * Math.cos(theta);
            y = r * Math.cos(phi) * Math.sin(theta);
            z = r * Math.sin(phi);
            direction = new Vec3f((float)x, (float)y, (float)z);
            rotation = (new OpenMatrix4f()).rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
            OpenMatrix4f.transform3v(rotation, direction, direction);
            executor.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x, target_y, target_z, direction.x, direction.y, direction.z);
        }

        for(i = 0; i < n; ++i) {
            theta = 6.283185307179586 * (new Random()).nextDouble();
            phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
            x = r * Math.cos(phi) * Math.cos(theta);
            y = r * Math.cos(phi) * Math.sin(theta);
            z = r * Math.sin(phi);
            direction = new Vec3f((float)x * ((new Random()).nextFloat() + 0.5F) * 0.8F, (float)y * ((new Random()).nextFloat() + 0.5F) * 0.8F, (float)z * ((new Random()).nextFloat() + 0.5F) * 0.8F);
            rotation = (new OpenMatrix4f()).rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
            OpenMatrix4f.transform3v(rotation, direction, direction);
            executor.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x, target_y, target_z, direction.x, direction.y, direction.z);
        }

        for(i = 0; i < 60; ++i) {
            executor.getOriginal().level().addParticle(ParticleTypes.FLAME, target_x + ((new Random()).nextFloat() - 0.5F) * 1.2F, target_y + 0.2F, (target_z + ((new Random()).nextFloat() - 0.5F) * 1.2F), 0.0,((new Random()).nextFloat() * 1.5F), 0.0);
        }

        Level level = executor.getOriginal().level();
        Vec3 floorPos = WOMAnimations.ReuseableEvents.getfloor(executor, WOMAnimations.TORMENT_BERSERK_CONVERT, new Vec3f(0.0F, 0.0F, 0.0F), Armatures.BIPED.rootJoint);
        Vec3 weaponEdge = new Vec3(floorPos.x, floorPos.y, floorPos.z);
        LevelUtil.circleSlamFracture(executor.getOriginal(), level, weaponEdge, 4.0, false, true);
    }
}
