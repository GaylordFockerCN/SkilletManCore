package com.p1nero.smc.entity.custom.npc;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.OpenVillagerDialogPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * 不会乱跑的村民
 */
public class VillagerWithoutBrain extends Villager {
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

    private void registerBrainGoals(Brain<Villager> p_35425_) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        p_35425_.addActivityWithConditions(Activity.WORK, VillagerGoalPackages.getWorkPackage(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
        p_35425_.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage(villagerprofession, 0.5F));
        p_35425_.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage(villagerprofession, 0.5F));
        p_35425_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_35425_.setDefaultActivity(Activity.IDLE);
        p_35425_.setActiveActivityIfPossible(Activity.IDLE);
        p_35425_.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    @Override
    public void tick() {
        super.tick();
        this.getNavigation().stop();
    }

    /**
     * 仅有对话，不活动，不受伤
     */
    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
        if (player instanceof ServerPlayer serverPlayer) {
            if(!this.isBaby() && this.getVillagerData().getProfession().equals(VillagerProfession.CLERIC)) {
                SMCAdvancementData.finishAdvancement("talk_to_cleric", serverPlayer);
            }

            SMCCapabilityProvider.getSMCPlayer(serverPlayer).setCurrentTalkingEntity(this);
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenVillagerDialogPacket(this.getId()), serverPlayer);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float value) {
        if(source.isCreativePlayer() || (this.getVillagerData().getProfession() == VillagerProfession.CLERIC && source.getEntity() instanceof Player)) {
            return super.hurt(source, value);
        }
        return false;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable(this.getType().getDescriptionId());
    }
}
