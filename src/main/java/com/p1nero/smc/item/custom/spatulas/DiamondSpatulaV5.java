package com.p1nero.smc.item.custom.spatulas;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DiamondSpatulaV5 extends DiamondSpatulaBase {
    public DiamondSpatulaV5(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull MutableComponent getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().withStyle(ChatFormatting.AQUA).append(Component.literal(" ⭐⭐⭐⭐⭐").withStyle(ChatFormatting.GOLD));
    }

}
