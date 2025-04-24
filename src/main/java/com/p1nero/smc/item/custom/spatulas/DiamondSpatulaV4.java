package com.p1nero.smc.item.custom.spatulas;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DiamondSpatulaV4 extends DiamondSpatulaBase {
    public DiamondSpatulaV4(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().withStyle(ChatFormatting.AQUA).append(Component.literal(" ⭐⭐⭐⭐").withStyle(ChatFormatting.DARK_PURPLE));
    }

}
