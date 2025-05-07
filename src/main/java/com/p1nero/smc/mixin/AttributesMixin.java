package com.p1nero.smc.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 突破血量上限
 */
@Mixin(Attributes.class)
public class AttributesMixin {
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void modifyMaxHealth(String name, Attribute attribute, CallbackInfoReturnable<Attribute> cir){
        if(name.equals("generic.max_health")){
            cir.setReturnValue(Registry.register(BuiltInRegistries.ATTRIBUTE, name, (new RangedAttribute("attribute.key.generic.max_health", 20.0, 1.0, Double.MAX_VALUE)).setSyncable(true)));
        }
    }
}
