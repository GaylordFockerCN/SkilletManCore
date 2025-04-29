package com.p1nero.smc.mixin;

import com.p1nero.smc.client.gui.screen.entity_dialog.animal.ChickenDialogScreenHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin extends AgeableMob {

    protected AnimalMixin(EntityType<? extends AgeableMob> p_146738_, Level p_146739_) {
        super(p_146738_, p_146739_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand p_27585_, CallbackInfoReturnable<InteractionResult> cir) {
        if ((Animal)(Object) this instanceof Chicken chicken && chicken.level().isClientSide) {
            ChickenDialogScreenHandler.addDialogScreen(chicken);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
