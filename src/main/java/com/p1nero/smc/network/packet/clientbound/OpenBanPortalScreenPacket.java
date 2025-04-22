package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.info_screen.BanPortalScreenHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record OpenBanPortalScreenPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static OpenBanPortalScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenBanPortalScreenPacket();
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            BanPortalScreenHandler.addScreen();
        }
    }
}