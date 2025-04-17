package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.block.custom.INpcDialogueBlock;
import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

/**
 * This packet is sent to the server whenever the player chooses an important action in the NPC dialogue.
 */
public record NpcBlockPlayerInteractPacket(BlockPos pos, byte interactionID) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos());
        buf.writeByte(this.interactionID());
    }

    public static NpcBlockPlayerInteractPacket decode(FriendlyByteBuf buf) {
        return new NpcBlockPlayerInteractPacket(buf.readBlockPos(), buf.readByte());
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (playerEntity != null && playerEntity.getServer() != null) {
            BlockEntity blockEntity = playerEntity.level().getBlockEntity(pos);
            if(blockEntity instanceof INpcDialogueBlock npcDialogueBlock) {
                npcDialogueBlock.handleNpcInteraction(playerEntity, interactionID);
            }
        }
    }
}
