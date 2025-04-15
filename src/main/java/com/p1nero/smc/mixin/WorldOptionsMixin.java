package com.p1nero.smc.mixin;

import com.p1nero.smc.worldgen.biome.BiomeMap;
import net.minecraft.world.level.levelgen.WorldOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 实现seed同步
 */
@Mixin(WorldOptions.class)
public class WorldOptionsMixin {

    @Shadow @Final private long seed;

    @Inject(method = "seed()J", at = @At("HEAD"))
    private void injected(CallbackInfoReturnable<Long> cir){
        BiomeMap.seed = this.seed;
    }
}
