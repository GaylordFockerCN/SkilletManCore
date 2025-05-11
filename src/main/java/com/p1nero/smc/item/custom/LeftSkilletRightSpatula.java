package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.item.WeaponItem;

import java.util.List;

public class LeftSkilletRightSpatula extends WeaponItem {
    public LeftSkilletRightSpatula(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getName(itemStack).copy().withStyle(ChatFormatting.GOLD);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(SkilletManCoreMod.getInfo("need_shift_see_combo").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(itemStack, level, list, flag);
    }
}
