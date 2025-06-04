package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.util.ScoreClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public record HandleScorePacket(String score) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(score);
    }

    public static HandleScorePacket decode(FriendlyByteBuf buf) {
        return new HandleScorePacket(buf.readUtf());
    }

    @Override
    public void execute(@Nullable Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ScoreClient.sendScore(score, serverPlayer);
        }
    }
}
