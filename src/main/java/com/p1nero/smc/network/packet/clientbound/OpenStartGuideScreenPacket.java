package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.info_screen.StartGuideScreenHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record OpenStartGuideScreenPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static OpenStartGuideScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenStartGuideScreenPacket();
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            StartGuideScreenHandler.addStartGuideScreen();
        }
    }
}