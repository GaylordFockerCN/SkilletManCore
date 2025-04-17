package com.p1nero.smc.network.packet.clientbound;
import com.p1nero.smc.block.custom.INpcDialogueBlock;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * 不带SkinID的Packet
 */
public record NPCBlockDialoguePacket(BlockPos pos, CompoundTag tag) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos());
        buf.writeNbt(this.tag());
    }

    public static NPCBlockDialoguePacket decode(FriendlyByteBuf buf) {
        return new NPCBlockDialoguePacket(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof INpcDialogueBlock npc) {
                npc.openDialogueScreen(this.tag());
            }
        }
    }
}