package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * This packet is sent to the server whenever the player chooses an important action in the NPC dialogue.
 */
public record NpcPlayerInteractPacket(int entityID, byte interactionID) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID());
        buf.writeByte(this.interactionID());
    }

    public static NpcPlayerInteractPacket decode(FriendlyByteBuf buf) {
        return new NpcPlayerInteractPacket(buf.readInt(), buf.readByte());
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (playerEntity != null && playerEntity.getServer() != null) {

            if(entityID == -1) {
                //来自非人

            } else {

                Entity entity = playerEntity.level().getEntity(this.entityID());
                if (entity instanceof NpcDialogue npc){
                    npc.handleNpcInteraction(playerEntity, this.interactionID());
                } else {
                    //普通村民的对话统一在此处理
                    if(entity instanceof Villager villager){

                    }
                }

            }
        }
    }
}
