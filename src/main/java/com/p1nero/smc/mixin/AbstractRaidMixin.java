package com.p1nero.smc.mixin;

import hungteen.htlib.common.world.raid.AbstractRaid;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRaid.class)
public class AbstractRaidMixin {

    @Inject(method = "joinRaid", at = @At("HEAD"), remap = false)
    private void smc$joinRaid(int wave, Entity raider, CallbackInfo ci) {
        raider.setGlowingTag(true);
    }
}
