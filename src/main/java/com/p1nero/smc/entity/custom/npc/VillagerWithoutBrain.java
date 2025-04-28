package com.p1nero.smc.entity.custom.npc;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * 不会乱跑的村民
 */
public class VillagerWithoutBrain extends SMCNpc {
    public VillagerWithoutBrain(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    public void refreshBrain(@NotNull ServerLevel serverLevel) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(serverLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    private void registerBrainGoals(Brain<Villager> villagerBrain) {
        villagerBrain.addActivity(Activity.CORE, VillagerTasks.getSMCVillagerCorePackage(this));
        villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    /**
     * 仅有对话，不活动，不受伤
     */
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        //TODO 根据村民职业对话，同原版
        return super.mobInteract(player, hand);
    }

    @Override
    public void openDialogueScreen(CompoundTag senderData) {

    }
}
