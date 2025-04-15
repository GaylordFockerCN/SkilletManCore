package com.p1nero.smc.network.packet.clientbound;

import com.p1nero.smc.DOTEConfig;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * 发给客户端，广播任务完成，用于一些没有玩家对象的地方。
 */
public record BroadcastMessagePacket(Component message, boolean actionBar) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeComponent(message);
        buf.writeBoolean(actionBar);
    }

    public static BroadcastMessagePacket decode(FriendlyByteBuf buf) {
        return new BroadcastMessagePacket(buf.readComponent(), buf.readBoolean());
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null && DOTEConfig.BROADCAST_DIALOG.get()){
            LocalPlayer player = Minecraft.getInstance().player;
            player.displayClientMessage(message, false);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1);
        }
    }
}
