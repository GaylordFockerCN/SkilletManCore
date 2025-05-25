package com.p1nero.smc.network.packet.serverbound;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.npc.me.P1nero;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.BasePacket;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import com.p1nero.smc.worldgen.portal.SMCTeleporter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public record EndScreenCallbackPacket() implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    public static EndScreenCallbackPacket decode(FriendlyByteBuf buf) {
        return new EndScreenCallbackPacket();
    }

    @Override
    public void execute(Player player) {
        if (player instanceof ServerPlayer serverPlayer && player.getServer() != null) {
            ServerLevel overworld = player.getServer().getLevel(Level.OVERWORLD);
            if (overworld != null) {
                if(player.level().dimension() != Level.OVERWORLD) {
                    serverPlayer.changeDimension(overworld, new SMCTeleporter(overworld.getSharedSpawnPos()));
                }
                if(!DataManager.bossKilled.get(serverPlayer)) {
                    serverPlayer.setRespawnPosition(Level.OVERWORLD, overworld.getSharedSpawnPos(), 0.0F, false, true);
                    SMCPlayer.addMoney(200000, serverPlayer);
                    SMCAdvancementData.finishAdvancement("end", serverPlayer);
                    DataManager.bossKilled.put(serverPlayer, true);
                    Vec3 view = serverPlayer.getViewVector(1.0F).normalize().scale(2);
                    P1nero p1nero = new P1nero(serverPlayer, serverPlayer.position().add(view.x, 2, view.z));
                    if(serverPlayer.serverLevel().addFreshEntity(p1nero)){
                        p1nero.setConversingPlayer(serverPlayer);
                        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(p1nero.getId(), new CompoundTag()), serverPlayer);
                    }
                }
            }
        }
    }
}