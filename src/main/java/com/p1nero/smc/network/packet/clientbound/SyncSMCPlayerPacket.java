package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record SyncSMCPlayerPacket(CompoundTag data) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    public static SyncSMCPlayerPacket decode(FriendlyByteBuf buf) {
        return new SyncSMCPlayerPacket(buf.readNbt());
    }

    @Override
    public void execute(Player playerEntity) {
        if(Minecraft.getInstance().player != null) {
            SMCCapabilityProvider.getSMCPlayer(Minecraft.getInstance().player).loadNBTData(data);
        }
    }
}