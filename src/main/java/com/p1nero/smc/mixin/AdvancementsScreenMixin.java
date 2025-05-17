package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import com.simibubi.create.Create;
import com.teamtea.eclipticseasons.EclipticSeasons;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementsScreen.class)
public abstract class AdvancementsScreenMixin extends Screen {

    protected AdvancementsScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "onAddAdvancementRoot", at = @At("HEAD"), cancellable = true)
    private void smc$onAddAdvancementRoot(Advancement advancement, CallbackInfo ci){
        if(!(advancement.getId().getNamespace().equals(SkilletManCoreMod.MOD_ID)
//                || advancement.getId().getNamespace().equals(EclipticSeasons.MODID)
        )){
            ci.cancel();
        }
    }

    @Inject(method = "onSelectedTabChanged", at = @At("HEAD"))
    private void smc$onSelectedTabChanged(Advancement p_97391_, CallbackInfo ci){
        if(this.minecraft != null && this.minecraft.player != null){
            LocalPlayer localPlayer = this.minecraft.player;
            localPlayer.playSound(SoundEvents.UI_BUTTON_CLICK.get());
        }
    }

}
