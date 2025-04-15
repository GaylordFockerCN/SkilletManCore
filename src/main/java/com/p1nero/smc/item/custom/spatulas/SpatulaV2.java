package com.p1nero.smc.item.custom.spatulas;

import com.p1nero.smc.item.custom.SMCSpatulaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpatulaV2 extends SMCSpatulaItem {
    public SpatulaV2(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().append(Component.literal(" ⭐⭐").withStyle(ChatFormatting.GREEN));
    }

}
