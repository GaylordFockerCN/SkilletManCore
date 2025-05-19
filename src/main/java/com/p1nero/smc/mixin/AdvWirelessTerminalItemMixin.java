package com.p1nero.smc.mixin;

import com.p1nero.smc.archive.DataManager;
import com.tom.storagemod.item.AdvWirelessTerminalItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 做引导
 */
@Mixin(value = AdvWirelessTerminalItem.class, remap = false)
public class AdvWirelessTerminalItemMixin {
    @Inject(method = "useOn", at = @At("RETURN"))
    private void smc$useOn(UseOnContext c, CallbackInfoReturnable<InteractionResult> cir){
        if(cir.getReturnValue() == InteractionResult.SUCCESS && !c.getLevel().isClientSide) {
            DataManager.showFirstPlaceWirelessTerminal.put(c.getPlayer(), false);
        }
    }
}
