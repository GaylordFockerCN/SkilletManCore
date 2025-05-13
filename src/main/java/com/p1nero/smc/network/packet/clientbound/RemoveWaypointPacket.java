package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.util.WaypointUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import xaero.hud.minimap.waypoint.WaypointColor;

public record RemoveWaypointPacket(String name, BlockPos pos) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeBlockPos(pos);
    }

    public static RemoveWaypointPacket decode(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        BlockPos blockPos = buf.readBlockPos();
        return new RemoveWaypointPacket(name, blockPos);
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            WaypointUtil.removeWayPoint(pos, name);
        }
    }
}
