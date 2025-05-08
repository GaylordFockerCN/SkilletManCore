package com.p1nero.smc.mixin;

import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.world.raid.AbstractRaid;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRaid.class)
public abstract class AbstractRaidMixin {

    @Shadow(remap = false) protected abstract void onLoss();

    @Inject(method = "joinRaid", at = @At("HEAD"), remap = false)
    private void smc$joinRaid(int wave, Entity raider, CallbackInfo ci) {
        raider.setGlowingTag(true);
    }

    @Inject(method = "validTick", at = @At(value = "INVOKE", target = "Lhungteen/htlib/common/world/raid/AbstractRaid;remove()V"), remap = false)
    private void smc$validTick(IRaidComponent raid, IWaveComponent wave, CallbackInfo ci){
        this.onLoss();
    }

}
