package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.DirtPlateItem;
import com.p1nero.smc.item.custom.GuoChaoBoxItem;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.p1nero.smc.item.custom.DirtPlateItem.giveBack;

@Mixin(value = PlateItem.class)
public abstract class PlateItemMixin extends Item {
    @Shadow(remap = false) protected abstract void giveBack(ItemStack foodStack, CookedFoodData food, PlateItem.ReturnTarget target);

    public PlateItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "giveBack", at = @At("HEAD"), remap = false, cancellable = true)
    private void smc$giveBack(ItemStack foodStack, CookedFoodData food, PlateItem.ReturnTarget target, CallbackInfo ci) {
        if ((PlateItem) (Object) this instanceof GuoChaoBoxItem) {
            foodStack.getOrCreateTag().putBoolean(SkilletManCoreMod.GUO_CHAO, true);
        }
        DirtPlateItem.giveBack(foodStack, food, target);
        ci.cancel();
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockEntity var5 = level.getBlockEntity(ctx.getClickedPos());
        if (var5 instanceof CuisineSkilletBlockEntity be) {
            if (be.cookingData.contents.isEmpty()) {
                cir.setReturnValue(InteractionResult.PASS);
            } else {
                if (!level.isClientSide()) {
                    CookingData data = be.cookingData;
                    data.stir(level.getGameTime(), 0);
                    CookedFoodData food = new CookedFoodData(data);
                    ItemStack foodStack = BaseCuisineRecipe.findBestMatch(level, food);
                    ctx.getItemInHand().shrink(1);

                    boolean poisoned = be.baseItem.getOrCreateTag().getBoolean(SkilletManCoreMod.POISONED_SKILLET);
                    if(poisoned) {
                        foodStack.getOrCreateTag().putBoolean(SkilletManCoreMod.POISONED_SKILLET, true);
                    }

                    if (player != null) {
                        this.giveBack(foodStack, food, new PlateItem.PlayerTarget(player));
                    } else {
                        this.giveBack(foodStack, food, new PlateItem.BlockTarget(ctx));
                    }
                    be.cookingData = new CookingData();
                    be.sync();
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        } else {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

}
