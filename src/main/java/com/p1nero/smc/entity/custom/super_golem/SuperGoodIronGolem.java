package com.p1nero.smc.entity.custom.super_golem;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class SuperGoodIronGolem extends IronGolem {
    public SuperGoodIronGolem(EntityType<? extends IronGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(5, new OfferFlowerGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false, (p_28879_) -> p_28879_ instanceof Enemy));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player p_28861_, @NotNull InteractionHand p_28862_) {
        ItemStack itemstack = p_28861_.getItemInHand(p_28862_);
        if (!itemstack.is(Items.IRON_INGOT)) {
            return InteractionResult.PASS;
        } else {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() == f) {
                return InteractionResult.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                if (!p_28861_.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 273.53)
                .add(Attributes.ATTACK_DAMAGE, 8.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.6f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .add(Attributes.ARMOR, 5.0F)
                .add(EpicFightAttributes.MAX_STRIKES.get(), 4)
                .add(EpicFightAttributes.WEIGHT.get(), 3)
                .add(EpicFightAttributes.IMPACT.get(), 6)
                .build();
    }

}
