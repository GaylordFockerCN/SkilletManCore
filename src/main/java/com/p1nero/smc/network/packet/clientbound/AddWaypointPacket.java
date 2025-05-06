package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;
import xaero.minimap.XaeroMinimap;

import java.io.IOException;

@SuppressWarnings("deprecation")
public record AddWaypointPacket(String name, BlockPos pos) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeBlockPos(pos);
    }

    public static AddWaypointPacket decode(FriendlyByteBuf buf) {
        return new AddWaypointPacket(buf.readUtf(), buf.readBlockPos());
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) (Minecraft.getInstance().player.connection);
            XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
            WaypointsManager waypointsManager = session.getWaypointsManager();
            Waypoint instant = new Waypoint(pos.getX(), pos.getY() + 2, pos.getZ(), name, name.substring(0, 1), (int)(Math.random() * ModSettings.ENCHANT_COLORS.length), 0, false);
            waypointsManager.getWaypoints().getList().add(instant);
            try {
                XaeroMinimap.instance.getSettings().saveWaypoints(waypointsManager.getCurrentWorld());
            } catch (IOException ignored) {
            }
        }
    }
}
