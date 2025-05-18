package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.SMCEndScreen;
import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.network.packet.clientbound.helper.SMCClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.world.entity.player.Player;

public record OpenEndScreenPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static OpenEndScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenEndScreenPacket();
    }

    @Override
    public void execute(Player playerEntity) {
        SMCClientHandler.openEndScreen();
    }
}