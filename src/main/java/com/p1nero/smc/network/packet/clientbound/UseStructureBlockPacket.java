package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.block.entity.BetterStructureBlockEntity;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record UseStructureBlockPacket(int x, int y, int z) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static UseStructureBlockPacket decode(FriendlyByteBuf buf) {
        return new UseStructureBlockPacket(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void execute(Player playerEntity) {

        if(Minecraft.getInstance().level != null && Minecraft.getInstance().player != null){
            BetterStructureBlockEntity entity = ((BetterStructureBlockEntity) Minecraft.getInstance().level.getBlockEntity(new BlockPos(x, y, z)));
            if(entity != null){
                StructureBlockEditScreen screen = new StructureBlockEditScreen(entity);
                Minecraft.getInstance().setScreen(screen);
                screen.loadButton.onPress();
            }
        }

    }
}