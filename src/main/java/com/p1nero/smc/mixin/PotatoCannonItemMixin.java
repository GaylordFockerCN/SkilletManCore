package com.p1nero.smc.mixin;

import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = PotatoCannonItem.class)
public class PotatoCannonItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResultHolder;success(Ljava/lang/Object;)Lnet/minecraft/world/InteractionResultHolder;"))
    private void smc$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        if(level.isClientSide){
            AbstractClientPlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, AbstractClientPlayerPatch.class);
            if(playerPatch != null) {
                playerPatch.getClientAnimator().playReboundAnimation();
            }
        }
    }
}
