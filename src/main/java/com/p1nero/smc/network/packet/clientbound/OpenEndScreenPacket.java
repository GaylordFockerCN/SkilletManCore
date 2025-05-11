package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.SMCEndScreen;
import com.p1nero.smc.network.packet.BasePacket;
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
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            Minecraft.getInstance().setScreen(new SMCEndScreen(true, () -> {
                Minecraft.getInstance().player.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
                Minecraft.getInstance().setScreen(null);
            }));
        }
    }
}