package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PiShuangItem extends SimpleDescriptionFoilItem{
    public PiShuangItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if(level.getBlockEntity(blockPos) instanceof CuisineSkilletBlockEntity be) {
            be.baseItem.getOrCreateTag().putBoolean(SkilletManCoreMod.POISONED_SKILLET, true);
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
