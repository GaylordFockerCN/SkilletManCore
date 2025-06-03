package com.p1nero.smc.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 取消瞬移
 */
@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster implements NeutralMob {

    @Shadow protected abstract boolean teleport(double p_32544_, double p_32545_, double p_32546_);

    protected EnderManMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "teleport()Z", at = @At("HEAD"), cancellable = true)
    private void smc$teleport(CallbackInfoReturnable<Boolean> cir) {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 3.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(6) - 3);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 3.0D;
            cir.setReturnValue(this.teleport(d0, d1, d2));
        } else {
            cir.setReturnValue(false);
        }
    }
}
