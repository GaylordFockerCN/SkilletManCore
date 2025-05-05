package com.p1nero.smc.mixin;

import dev.xkmc.cuisinedelight.events.EventListeners;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 换到{@link com.p1nero.smc.event.ItemListeners#onTooltip(ItemTooltipEvent)}
 */
@Mixin(EventListeners.class)
public class CDEventListenersMixin {
    @Inject(method = "onTooltip", at = @At("HEAD"), cancellable = true, remap = false)
    private static void smc$onTooltip(ItemTooltipEvent event, CallbackInfo ci){
        ci.cancel();
    }
}
