package com.p1nero.smc.mixin;

import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.item.custom.SMCSpatulaItem;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.init.data.LangData;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
public abstract class SpatulaItemMixin extends Item {

    public SpatulaItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Shadow(remap = false)
    private static int getReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 ? 20 : 0;
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void smc$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack spatulaStack = player.getItemInHand(hand);
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack skilletStack = player.getItemInHand(otherHand);
        if (!skilletStack.is(CDItems.SKILLET.get())) {
            cir.setReturnValue(InteractionResultHolder.pass(spatulaStack));
        } else {
            if(!level.isClientSide){
                player.getCooldowns().addCooldown(this, 20);
                player.getCooldowns().addCooldown(CDItems.SKILLET.get(), 20);
            }
            CookingData data = CuisineSkilletItem.getData(skilletStack);
            if (data != null && SMCSpatulaItem.checkInCorrectTime(player)) {
                if (!level.isClientSide()) {
                    data.stir(level.getGameTime(), getReduction(spatulaStack));
                    CuisineSkilletItem.setData(skilletStack, data);
                } else {
                    CuisineSkilletItem.playSound(player, level, ModSounds.BLOCK_SKILLET_ADD_FOOD.get());
                }

                cir.setReturnValue(InteractionResultHolder.success(spatulaStack));
            } else {
                cir.setReturnValue(InteractionResultHolder.fail(spatulaStack));
            }
        }
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
            if (player != null && !level.isClientSide) {
                player.getCooldowns().addCooldown((SpatulaItem) (Object) this, ANIM_TIME);
            }
            if (!be.cookingData.contents.isEmpty() && SMCSpatulaItem.checkInCorrectTime(player)) {
                if (!level.isClientSide()) {
                    be.stir(level.getGameTime(), getReduction(ctx.getItemInHand()));
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
