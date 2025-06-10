package com.p1nero.smc.mixin;

import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.init.data.LangData;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.item.SkilletItem;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

@Mixin(value = CuisineSkilletItem.class)
public abstract class CuisineSkilletItemMixin extends SkilletItem {

    public CuisineSkilletItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    /**
     * 史诗战斗下use无用
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void smc$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack skilletStack = player.getItemInHand(hand);
//        if (!level.isClientSide()) {
//            //战斗模式不可用
//            if(EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode()){
//                cir.setReturnValue(InteractionResultHolder.fail(skilletStack));
//            }
//        }
        cir.setReturnValue(InteractionResultHolder.fail(skilletStack));
    }

    /**
     * 省略shift
     */
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    public void smc$appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag, CallbackInfo ci) {
        list.add(LangData.USE_SPATULA.get(CDItems.SPATULA.asStack().getHoverName().copy().withStyle(ChatFormatting.YELLOW)));
        list.add(LangData.USE_PLATE.get(CDItems.PLATE.asStack().getHoverName().copy().withStyle(ChatFormatting.YELLOW)));
        list.add(LangData.ENCH_FIRE.get());
        list.add(LangData.ENCH_EFFICIENCY.get());
        ci.cancel();
    }
}
