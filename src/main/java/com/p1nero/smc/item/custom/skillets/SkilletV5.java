package com.p1nero.smc.item.custom.skillets;

import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.item.custom.SMCCuisineSkilletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkilletV5 extends SMCCuisineSkilletItem {
    public SkilletV5(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().append(Component.literal(" ⭐⭐⭐⭐⭐").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(itemStack, level, player);
        if(player instanceof ServerPlayer serverPlayer) {
            SMCAdvancementData.getAdvancement("first_5star_skillet", serverPlayer);
        }
    }
}
