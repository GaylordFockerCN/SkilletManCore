package com.p1nero.smc.mixin;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.screen.entity_dialog.animal.CatDialogScreenHandler;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.effect.SMCEffects;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.item.custom.BadCat;
import com.p1nero.smc.item.custom.LuckyCat;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {

    @Shadow public abstract void setLying(boolean p_28182_);

    protected CatMixin(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void smc$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CAT_AMBIENT, SoundSource.BLOCKS, 1, 1);
        ItemStack useItem = player.getItemInHand(hand);

        if(this.getOwner() == player) {
            if(useItem.getItem() instanceof LuckyCat) {
                LuckyCat.summonCustomers(useItem, player, (Cat) (Object) this);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            if(useItem.getItem() instanceof BadCat) {
                BadCat.onInteractCat(useItem, player, (Cat) (Object) this);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
        }

        if(player.level().isClientSide) {
            CatDialogScreenHandler.addDialogScreen((Cat) (Object) this);
        } else {
            SMCCapabilityProvider.getSMCPlayer(player).setCurrentTalkingEntity((Cat) (Object) this);
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void smc$tick(CallbackInfo ci){
        if(this.hasEffect(SMCEffects.BAD_CAT.get()) && !level().isClientSide) {
            this.setLying(false);
            this.setInSittingPose(false);
            List<Customer> customers = EntityUtil.getNearByEntities(Customer.class, this, 16);
            if(!customers.isEmpty()) {
                customers.sort(Comparator.comparingDouble(this::distanceTo));
                customers.forEach(customer -> {
                    if(!customer.isTraded() && this.getOwner() != customer.getOwner()) {
                        this.getNavigation().moveTo(customer, 2.0);
                        this.getLookControl().setLookAt(customer, 10.0F, this.getMaxHeadXRot());
                        if(this.distanceTo(customer) < 1.5) {
                            this.playAmbientSound();
                            customer.setTraded(true);
                        }
                    }
                });
            }
        }
    }
}
