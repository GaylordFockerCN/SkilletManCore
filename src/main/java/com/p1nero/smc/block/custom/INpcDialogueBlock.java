package com.p1nero.smc.block.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 可对话的方块
 */
public interface INpcDialogueBlock {
    @OnlyIn(Dist.CLIENT)
    void openDialogueScreen(CompoundTag senderData);
    void handleNpcInteraction(Player player, byte interactionID);

}
