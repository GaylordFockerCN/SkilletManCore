package com.p1nero.smc.network.packet.serverbound;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.worldgen.portal.SMCTeleporter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record EndScreenCallbackPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static EndScreenCallbackPacket decode(FriendlyByteBuf buf) {
        return new EndScreenCallbackPacket();
    }

    @Override
    public void execute(Player player) {
        if (player instanceof ServerPlayer serverPlayer && player.getServer() != null) {
            ServerLevel overworld = player.getServer().getLevel(Level.OVERWORLD);
            if (overworld != null) {
                if(player.level().dimension() != Level.OVERWORLD) {
                    serverPlayer.changeDimension(overworld, new SMCTeleporter(overworld.getSharedSpawnPos()));
                }
                serverPlayer.setRespawnPosition(Level.OVERWORLD, overworld.getSharedSpawnPos(), 0.0F, false, true);
                SMCPlayer.addMoney(200000, serverPlayer);
                SMCAdvancementData.finishAdvancement("end", serverPlayer);
            }
        }
    }
}