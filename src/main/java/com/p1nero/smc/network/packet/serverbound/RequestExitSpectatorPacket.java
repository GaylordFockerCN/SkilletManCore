package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public record RequestExitSpectatorPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static RequestExitSpectatorPacket decode(FriendlyByteBuf buf) {
        return new RequestExitSpectatorPacket();
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (playerEntity != null && playerEntity.isSpectator() && playerEntity.level().dimension() == SMCDimension.P_SKY_ISLAND_LEVEL_KEY) {
//            Vec3i startPos = DOTEStructurePoses.START_POINT.getPos();
//            playerEntity.teleportTo(startPos.getX(), startPos.getY(), startPos.getZ());
//            if(playerEntity instanceof ServerPlayer serverPlayer){
//                serverPlayer.setGameMode(GameType.ADVENTURE);
//            }
        }
    }
}
