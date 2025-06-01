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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import static com.simibubi.create.content.equipment.potatoCannon.PotatoCannonItem.getAmmo;

@Mixin(value = PotatoCannonItem.class)
public class PotatoCannonItemMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void smc$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack heldStack = player.getItemInHand(hand);
        heldStack.getOrCreateTag();//修之
        if(level.isClientSide){
            PotatoCannonItem.Ammo ammo = getAmmo(player, heldStack);
            if (ammo != null) {
                PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                if(playerPatch != null) {
                    playerPatch.getClientAnimator().playReboundAnimation();
                }
            }
        }
    }
}
