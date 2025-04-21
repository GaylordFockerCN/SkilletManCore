package com.p1nero.smc.mixin;

import com.p1nero.smc.datagen.SMCAdvancementData;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal {

    @Shadow public abstract void setTame(boolean p_30443_);

    @Shadow public abstract void setPersistentAngerTarget(@Nullable UUID p_30400_);

    @Shadow public abstract void setRemainingPersistentAngerTime(int p_30404_);

    protected WolfMixin(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.is(PlateFood.SUSPICIOUS_MIX.item.asItem())) {
            if(player.level().isClientSide) {
                cir.setReturnValue(InteractionResult.CONSUME);
            } else {
                if(this.random.nextBoolean()) {
                    this.setHealth(0);
                } else {
                    this.setTame(false);
                    this.setTarget(player);
                    this.setPersistentAngerTarget(player.getUUID());
                    this.setRemainingPersistentAngerTime(142857);
                }
                itemStack.shrink(1);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
        CookedFoodData cookedFoodData = BaseFoodItem.getData(itemStack);
        if(cookedFoodData != null && (cookedFoodData.toFoodData() == CookedFoodData.BAD || cookedFoodData.score < 60)) {
            if(player.level().isClientSide) {
                cir.setReturnValue(InteractionResult.CONSUME);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);//播放失败粒子
                SMCAdvancementData.finishAdvancement("dog_no_eat", ((ServerPlayer) player));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

}
