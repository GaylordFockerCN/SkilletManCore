package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * 似乎不起作用，依然会渲染移动过程
 */
public record SyncPos0Packet(int id, double x, double y, double z) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static SyncPos0Packet decode(FriendlyByteBuf buf) {
        return new SyncPos0Packet(buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void execute(@Nullable Player player) {
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null){
            Entity entity = Minecraft.getInstance().level.getEntity(id);
            if(entity != null){
                entity.xo = x;
                entity.xOld = x;
                entity.yo = y;
                entity.yOld = y;
                entity.zo = z;
                entity.zOld = z;
            }
        }
    }
}