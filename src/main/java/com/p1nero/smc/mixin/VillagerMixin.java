package com.p1nero.smc.mixin;

import com.p1nero.smc.entity.custom.npc.VanillaVillagerDialogue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁用村民交易，改为根据职业对话
 */
@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

    public VillagerMixin(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
        super(p_35267_, p_35268_);
    }

    @Shadow public abstract VillagerData getVillagerData();

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void smc$mobInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        VanillaVillagerDialogue.onVanillaNPCDialogue((Villager) (Object) this, this.getVillagerData(), player);
    }

    /**
     * 降低频率，不然太吵了
     */
    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void smc$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if(this.getRandom().nextInt(6) != 1) {
            cir.setReturnValue(null);
        }
    }

}
