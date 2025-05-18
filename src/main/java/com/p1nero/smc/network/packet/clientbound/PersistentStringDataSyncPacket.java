package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.network.packet.clientbound.helper.SMCClientHandler;
import hungteen.htlib.util.helper.DistHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public record PersistentStringDataSyncPacket(String key, boolean isLocked, String value) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeComponent(Component.literal(key));
        buf.writeBoolean(isLocked);
        buf.writeComponent(Component.literal(value));
    }

    public static PersistentStringDataSyncPacket decode(FriendlyByteBuf buf) {
        String key = buf.readComponent().getString();
        boolean isLocked =  buf.readBoolean();
        String value = buf.readComponent().getString();
        return new PersistentStringDataSyncPacket(key, isLocked, value);
    }

    @Override
    public void execute(Player playerEntity) {
        DistHelper.runClient(() -> () -> {
            SMCClientHandler.syncStringData(key, isLocked, value);
        });
    }
}