package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.info_screen.FastKillBossScreenHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record OpenFastKillBossScreenPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static OpenFastKillBossScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenFastKillBossScreenPacket();
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            FastKillBossScreenHandler.addScreen();
        }
    }
}