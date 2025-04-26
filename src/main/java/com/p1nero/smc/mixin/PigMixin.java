package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.screen.entity_dialog.animal.PigDialogScreenHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pig.class)
public class PigMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand p_29490_, CallbackInfoReturnable<InteractionResult> cir){
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PIG_AMBIENT, SoundSource.BLOCKS, 1, 1);
        if(player.level().isClientSide) {
            PigDialogScreenHandler.addPigDialogScreen((Pig) (Object) this);
        } else {
            SMCCapabilityProvider.getSMCPlayer(player).setCurrentTalkingEntity((Pig) (Object) this);
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
