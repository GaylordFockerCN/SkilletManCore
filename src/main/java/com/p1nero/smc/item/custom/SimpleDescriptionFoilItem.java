package com.p1nero.smc.item.custom;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 带物品说明和会发光
 */
public class SimpleDescriptionFoilItem extends SimpleDescriptionItem{

    public SimpleDescriptionFoilItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }
}
