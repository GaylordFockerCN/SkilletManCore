package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

public record OpenVillagerDialogPacket(int villagerId) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.villagerId());
    }

    public static OpenVillagerDialogPacket decode(FriendlyByteBuf buf) {
        return new OpenVillagerDialogPacket(buf.readInt());
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(villagerId);
            if(entity instanceof Villager villager) {
                VillagerDialogScreenHandler.addDialogScreen(villager);
            }
        }
    }
}