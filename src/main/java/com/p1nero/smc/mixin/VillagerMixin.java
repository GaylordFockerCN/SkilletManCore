package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.OpenVillagerDialogPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁用村民交易，改为根据职业对话
 */
@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

    public VillagerMixin(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
        super(p_35267_, p_35268_);
    }

    /**
     * 取消原版村民交易，换自己的对话
     */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
        if (player instanceof ServerPlayer serverPlayer) {
            SMCCapabilityProvider.getSMCPlayer(serverPlayer).setCurrentTalkingEntity((Villager) (Object) this);
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenVillagerDialogPacket(this.getId()), serverPlayer);
        }
        cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide));
    }

    /**
     * 降低频率，不然太吵了
     */
    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void smc$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.getRandom().nextInt(6) != 1) {
            cir.setReturnValue(null);
        }
    }

}
