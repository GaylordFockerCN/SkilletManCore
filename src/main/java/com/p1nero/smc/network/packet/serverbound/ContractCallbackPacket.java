package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record ContractCallbackPacket(UUID playerUUID) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
    }

    public static ContractCallbackPacket decode(FriendlyByteBuf buf) {
        return new ContractCallbackPacket(buf.readUUID());
    }

    @Override
    public void execute(Player player) {
        if (player instanceof ServerPlayer serverPlayer && player.getServer() != null) {
            Player target = serverPlayer.serverLevel().getPlayerByUUID(playerUUID);
            if(target instanceof ServerPlayer targetS) {
                SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
                if(smcPlayer.getCollaborator(serverPlayer) instanceof ServerPlayer oldCollaborator) {
                    SMCCapabilityProvider.getSMCPlayer(oldCollaborator).clearCollaborator();
                    oldCollaborator.displayClientMessage(SkilletManCoreMod.getInfo("collaborate_loss", serverPlayer.getDisplayName()).withStyle(ChatFormatting.RED), false);
                }
                smcPlayer.setCollaborator(targetS);
                SMCPlayer targetSMCPlayer = SMCCapabilityProvider.getSMCPlayer(targetS);
                if(targetSMCPlayer.getCollaborator(serverPlayer) instanceof ServerPlayer oldCollaborator2) {
                    SMCCapabilityProvider.getSMCPlayer(oldCollaborator2).clearCollaborator();
                    oldCollaborator2.displayClientMessage(SkilletManCoreMod.getInfo("collaborate_loss", targetS.getDisplayName()).withStyle(ChatFormatting.RED), false);
                }
                targetSMCPlayer.setCollaborator(serverPlayer);
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("collaborate_with", targetS.getDisplayName()).withStyle(ChatFormatting.GREEN), true);
                targetS.displayClientMessage(SkilletManCoreMod.getInfo("collaborate_with", serverPlayer.getDisplayName()).withStyle(ChatFormatting.GREEN), true);
            }
        }
    }
}