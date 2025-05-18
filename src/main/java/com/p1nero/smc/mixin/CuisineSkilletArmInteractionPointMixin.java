package com.p1nero.smc.mixin;

import com.p1nero.create_cuisine.interaction_points.CuisineSkilletArmInteractionPoint;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.simibubi.create.content.logistics.box.PackageItem;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CuisineSkilletArmInteractionPoint.class, remap = false)
public abstract class CuisineSkilletArmInteractionPointMixin extends ArmInteractionPoint {

    public CuisineSkilletArmInteractionPointMixin(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
        super(type, level, pos, state);
    }

    @Inject(method = "insert", at = @At("HEAD"))
    private void smc$insert(ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (!simulate) {
            if (level.getBlockEntity(pos) instanceof CuisineSkilletBlockEntity && level instanceof ServerLevel serverLevel) {
                for (ServerPlayer serverPlayer : serverLevel.getPlayers(serverPlayer -> serverPlayer.position().distanceTo(pos.getCenter()) < 5)) {
                    if (stack.getItem() instanceof SpatulaItem) {
                        SMCAdvancementData.finishAdvancement("arm2", serverPlayer);
                    }
                    if(stack.getItem() instanceof PlateItem) {
                        SMCAdvancementData.finishAdvancement("arm3", serverPlayer);
                    }
                    if(stack.getItem() instanceof PackageItem || IngredientConfig.get().getEntry(stack) != null) {
                        SMCAdvancementData.finishAdvancement("arm1", serverPlayer);
                    }
                }
            }
        }
    }
}
