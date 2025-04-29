package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import dev.xkmc.cuisinedelight.init.data.LangData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModSounds;

/**
 * 禁用未切的食物
 */
@Mixin(value = CuisineSkilletBlock.class)
public class CuisineSkilletBlockMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void smc$use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CuisineSkilletBlockEntity be) {
            ItemStack heldStack = player.getItemInHand(hand);
            IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(heldStack);

            if(StartNPC.FOODS_NEED_CUT.contains(heldStack.getItem())) {
                if(!level.isClientSide) {
                    player.displayClientMessage(SkilletManCoreMod.getInfo("foods_need_cut"), true);
                }
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            if (config != null) {
                if (!be.canCook()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(LangData.MSG_NO_HEAT.get(), true);
                    }

                    cir.setReturnValue(InteractionResult.FAIL);
                    return;
                }

                if (be.cookingData.contents.size() >= CDConfig.COMMON.maxIngredient.get()) {
                    if (!level.isClientSide()) {
                        ((ServerPlayer)player).sendSystemMessage(LangData.MSG_FULL.get(), true);
                    }

                    cir.setReturnValue(InteractionResult.FAIL);
                    return;
                }

                if (!level.isClientSide) {
                    int count = 1 + be.baseItem.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                    if (be.slowCook()) {
                        be.cookingData.setSpeed(0.5F);
                    }

                    ItemStack add = heldStack.split(count);
                    be.cookingData.addItem(add, level.getGameTime());
                    ItemStack remain = add.getCraftingRemainingItem();
                    remain.setCount(add.getCount());
                    player.getInventory().placeItemBackInInventory(remain);
                    be.sync();
                } else {
                    CuisineSkilletItem.playSound(player, level, ModSounds.BLOCK_SKILLET_ADD_FOOD.get());
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
        }

        cir.setReturnValue(InteractionResult.PASS);
    }

}
