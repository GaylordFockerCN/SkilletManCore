package com.p1nero.smc.item.custom;

import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class SMCCuisineSkilletItem extends CuisineSkilletItem {
    public SMCCuisineSkilletItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(itemStack, level, player);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
