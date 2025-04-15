package com.p1nero.smc.mixin;

import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.item.SkilletItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = CuisineSkilletItem.class, remap = false)
public abstract class CuisineSkilletItemMixin extends SkilletItem {

    public CuisineSkilletItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void smc$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack skilletStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            //战斗模式不可用
            if(EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode()){
                cir.setReturnValue(InteractionResultHolder.fail(skilletStack));
            }
        }
    }

}
