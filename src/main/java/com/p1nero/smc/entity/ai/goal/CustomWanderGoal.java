package com.p1nero.smc.entity.ai.goal;

import com.p1nero.smc.entity.api.IWanderableEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class CustomWanderGoal<T extends PathfinderMob & IWanderableEntity> extends MeleeAttackGoal {

    private final T wanderMob;
    @Nullable
    private LivingEntityPatch<?> patch;
    protected final double attackRadiusSqr;

    public CustomWanderGoal(T mob, double speedModifier, boolean longMemory, double attackRadius) {
        super(mob, speedModifier, longMemory);
        this.wanderMob = mob;
        patch = EpicFightCapabilities.getEntityPatch(wanderMob, LivingEntityPatch.class);
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    @Override
    public boolean canUse() {
        patch = EpicFightCapabilities.getEntityPatch(wanderMob, LivingEntityPatch.class);
        if(patch == null){
            return false;
        }
        System.out.println(wanderMob.getStrafingTime());
        return wanderMob.getStrafingTime() > 0 || super.canUse();
    }

    @Override
    public void start() {
        if(patch != null){
            patch.getEntityState().setState(EntityState.INACTION, true);
        }
    }

    public void tick() {
        System.out.println("using");
        if(patch == null){
            return;
        }
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            if (!this.patch.getEntityState().turningLocked()) {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (!this.patch.getEntityState().movementLocked()) {
                boolean withDistance = this.attackRadiusSqr > this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                if (this.wanderMob.getStrafingTime() > 0) {
                    this.wanderMob.setStrafingTime(this.wanderMob.getStrafingTime() - 1);
                    this.mob.getNavigation().stop();
                    this.mob.lookAt(target, 30.0F, 30.0F);
                    this.mob.getMoveControl().strafe(withDistance && this.wanderMob.getStrafingForward() > 0.0F ? 0.0F : this.wanderMob.getStrafingForward(), this.wanderMob.getStrafingClockwise());
                } else if (withDistance) {
                    this.mob.getNavigation().stop();
                    if(patch != null){
                        patch.getEntityState().setState(EntityState.INACTION, false);
                    }
                } else {
                    if(patch != null){
                        patch.getEntityState().setState(EntityState.INACTION, false);
                    }
                    super.tick();
                }

            }
        }
    }

    @Override
    public void stop() {
        if(patch != null){
            patch.getEntityState().setState(EntityState.INACTION, false);
        }
    }

}
