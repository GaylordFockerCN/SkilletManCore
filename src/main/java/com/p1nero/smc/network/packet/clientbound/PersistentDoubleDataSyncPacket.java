package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.archive.DataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public record PersistentDoubleDataSyncPacket(String key, boolean isLocked, double value) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(key));
        buf.writeBoolean(isLocked);
        buf.writeDouble(value);
    }

    public static PersistentDoubleDataSyncPacket decode(FriendlyByteBuf buf) {
        String key = buf.readComponent().getString();
        boolean isLocked = buf.readBoolean();
        double value = buf.readDouble();
        return new PersistentDoubleDataSyncPacket(key, isLocked, value);
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (isLocked) {
                DataManager.putData(player, key + "isLocked", true);
                return;
            }
            DataManager.putData(player, key, value);
            DataManager.putData(player, key + "isLocked", false);
        }
    }
}