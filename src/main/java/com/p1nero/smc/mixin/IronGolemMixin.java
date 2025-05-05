package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.screen.entity_dialog.golem.IronGolemDialogScreenHandler;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.super_golem.SuperBadIronGolem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolem.class)
public abstract class IronGolemMixin extends AbstractGolem {

    protected IronGolemMixin(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        if(this.getTarget() == player || player.getItemInHand(hand).is(Items.IRON_INGOT)) {
            return;
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_GOLEM_STEP, SoundSource.BLOCKS, 1, 1);
        if(player.level().isClientSide) {
            IronGolemDialogScreenHandler.addDialogScreen((IronGolem) (Object) this);
        } else {
            SMCCapabilityProvider.getSMCPlayer(player).setCurrentTalkingEntity((IronGolem) (Object) this);
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void smc$hurt(DamageSource damageSource, float v, CallbackInfoReturnable<Boolean> cir) {
        if(damageSource.getEntity() instanceof Player && this.getType() != SMCEntities.SUPER_GOLEM.get()) {
            cir.setReturnValue(false);
        }
    }

}
