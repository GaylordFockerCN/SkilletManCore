package com.p1nero.smc.entity.custom.npc;

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
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 不会乱跑的村民
 */
public class VillagerWithoutBrain extends Villager {
    public VillagerWithoutBrain(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return this.brainProvider().makeBrain(dynamic);
    }

    public void refreshBrain(@NotNull ServerLevel serverLevel) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(serverLevel, this);
        this.brain = brain.copyWithoutBehaviors();
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
        if(source.isCreativePlayer() || this.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
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
