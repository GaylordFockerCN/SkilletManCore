package com.p1nero.smc.item.custom.spatulas;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.SMCSpatulaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoldenSpatulaBase extends SMCSpatulaItem {
    public GoldenSpatulaBase(Properties pProperties) {
        super(pProperties);
        this.cooldown = 15;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().withStyle(ChatFormatting.YELLOW);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(SkilletManCoreMod.getInfo("golden_spatula_tip"));
    }
}
