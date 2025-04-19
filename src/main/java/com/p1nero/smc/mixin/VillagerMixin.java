package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.custom.npc.VanillaVillagerDialogue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁用村民交易，改为根据职业对话
 */
@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Shadow public abstract VillagerData getVillagerData();

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void smc$mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        VanillaVillagerDialogue.onVanillaNPCDialogue((Villager) (Object) this, this.getVillagerData(), player);
    }

}
