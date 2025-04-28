package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.TimeCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {

    @Inject(method = "setTime", at = @At("HEAD"), cancellable = true)
    private static void smc$setTime(CommandSourceStack stack, int p_139079_, CallbackInfoReturnable<Integer> cir) {
        stack.sendFailure(SkilletManCoreMod.getInfo("set_time_not_allowed"));
        cir.setReturnValue(0);
    }

}
