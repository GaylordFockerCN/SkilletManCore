package com.p1nero.smc.entity.custom.boss.goldenflame;

import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;

import java.util.Random;

/**
 * 神王追人火圈
 */
public class FlameCircleEntity extends Entity {
    private LivingEntity owner;

    private float radius, speed;

    private int lifeTime;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public FlameCircleEntity(EntityType<FlameCircleEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
        noPhysics = true;
    }

    public FlameCircleEntity(LivingEntity owner, Level level, float radius, float speed, int lifeTime) {
        super(SMCEntities.FLAME_CIRCLE.get(), level);
        setNoGravity(true);
        noPhysics = true;
        this.radius = radius;
        this.speed = speed;
        this.lifeTime = lifeTime;
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel serverLevel){
            Vec3 myPos = position();
            int cnt = (int) (radius * 10);
            float angleStep = (float) (2 * Math.PI / (radius * 10));
            for (int i = 0; i < cnt; i++) {
                float angle = i * angleStep;
                float x = (float) (myPos.x + radius * Math.cos(angle));
                float z = (float) (myPos.z + radius * Math.sin(angle));
                serverLevel.sendParticles(ParticleTypes.FLAME, x, myPos.y, z, 1, 0, 0, 0, 0);
            }

            int numberOf = 2;
            double r = 0.7;
            double t = 0.01;
            float power = 1.0F + (float)1 / 200.0F * 7.0F;

            for(int i = 0; i < numberOf; ++i) {
                double theta = 6.283185 * (new Random()).nextDouble();
                double phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                double x = r * Math.cos(phi) * Math.cos(theta);
                double y = r * Math.cos(phi) * Math.sin(theta);
                double z = r * Math.sin(phi);
                Vec3f direction = new Vec3f((float)x, (float)y, (float)z);
                OpenMatrix4f rotation = (new OpenMatrix4f()).rotate(-((float)Math.toRadians(owner.yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
                rotation.rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
                OpenMatrix4f.transform3v(rotation, direction, direction);
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, owner.getX() + (double)direction.x, owner.getY() + 0.1, owner.getZ() + (double)direction.z, 1, 0.0, 0.0099, 0.0, 1);
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, owner.getX() + (double)(direction.x * (1.0F + (new Random()).nextFloat() * power)), owner.getY() + 0.1, owner.getZ() + (double)(direction.z * (1.0F + (new Random()).nextFloat() * power)), 1, 0.0, (double)(0.1F + (new Random()).nextFloat() * 0.2F), 0.0, 1);

                for(int j = 0; j < 3 ; ++j) {
                    theta = 6.283185 * (new Random()).nextDouble();
                    phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                    x = r * Math.cos(phi) * Math.cos(theta);
                    y = r * Math.cos(phi) * Math.sin(theta);
                    z = r * Math.sin(phi);
                    direction = new Vec3f((float)x, (float)y, (float)z);
                    rotation = (new OpenMatrix4f()).rotate(-((float)Math.toRadians((double)owner.yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
                    rotation.rotate((float)Math.toRadians(90.0), new Vec3f(1.0F, 0.0F, 0.0F));
                    OpenMatrix4f.transform3v(rotation, direction, direction);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, owner.getX() + (double)(direction.x * power), owner.getY() + 0.1, owner.getZ() + (double)(direction.z * power), 1, 0.0, 0.0099, 0.0, 1);
                }
            }

            for(Entity entity : EntityUtil.getNearByEntities(this, ((int) radius))){
                entity.setSecondsOnFire(5);

            }

            if(tickCount > lifeTime){
                discard();
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
