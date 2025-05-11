package com.p1nero.smc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;
import java.util.Objects;

import static dev.xkmc.cuisinedelight.content.item.SpatulaItem.ANIM_TIME;

@Mixin(value = SpatulaItem.class)
public abstract class SpatulaItemMixin {

    @Shadow(remap = false)
    private static int getReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 ? 20 : 0;
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void smc$useOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        if(Objects.requireNonNull(EpicFightCapabilities.getEntityPatch(ctx.getPlayer(), PlayerPatch.class)).isBattleMode()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockEntity var5 = level.getBlockEntity(ctx.getClickedPos());
        if (var5 instanceof CuisineSkilletBlockEntity be) {
            if (!be.cookingData.contents.isEmpty()) {
                if (!level.isClientSide()) {
                    be.stir(level.getGameTime(), getReduction(ctx.getItemInHand()));
                    if (player != null) {
                        player.getCooldowns().addCooldown((SpatulaItem) (Object) this, ANIM_TIME);
                    }
                } else if (player != null) {
                    CuisineSkilletItem.playSound(player, level, ModSounds.BLOCK_SKILLET_SIZZLE.get());
                }
            }
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    /**
     * 省略shift
     */
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void smc$appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag, CallbackInfo ci){
        list.add(LangData.ENCH_SILK.get());
        ci.cancel();
    }

}
