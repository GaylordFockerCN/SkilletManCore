package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.client.gui.screen.info_screen.ContractScreenHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record OpenContractScreenPacket(Component playerName, UUID playerUUID) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeComponent(playerName);
        buf.writeUUID(playerUUID);
    }

    public static OpenContractScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenContractScreenPacket(buf.readComponent(), buf.readUUID());
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            ContractScreenHandler.addScreen(playerName, playerUUID);
        }
    }
}