package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.client.gui.BossBarHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * 同步客户端的uuid
 */
public record SyncUuidPacket(UUID serverUuid, int id) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(serverUuid);
        buf.writeInt(id);
    }

    public static SyncUuidPacket decode(FriendlyByteBuf buf) {
        return new SyncUuidPacket(buf.readUUID(), buf.readInt());
    }

    @Override
    public void execute(@Nullable Player player) {
        BossBarHandler.BOSSES.put(serverUuid, id);
    }
}