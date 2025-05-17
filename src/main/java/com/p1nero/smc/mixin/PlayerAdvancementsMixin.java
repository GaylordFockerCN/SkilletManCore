package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

/**
 * 让图鉴全显示
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Shadow @Final private Set<Advancement> visible;

    @Shadow @Final private Map<Advancement, AdvancementProgress> progress;

    @Shadow @Final private Set<Advancement> progressChanged;

    @Shadow public abstract AdvancementProgress getOrStartProgress(Advancement p_135997_);

    @Inject(method = "updateTreeVisibility", at = @At("HEAD"))
    private void smc$updateTreeVisibility(Advancement advancement, Set<Advancement> advancements, Set<ResourceLocation> resourceLocations, CallbackInfo ci){
        String path = advancement.getId().getPath();
        if(advancement.getId().getNamespace().equals(SkilletManCoreMod.MOD_ID)
                && (path.contains("_shop") || path.contains("_level") || path.contains("_skill")
                || path.contains("_armor") || path.contains("_weapon") || path.contains("_food"))){
            smc$setVisible(advancement, advancements);
        }
    }

    @Unique
    private void smc$setVisible(Advancement advancement, Set<Advancement> advancements){
        this.getOrStartProgress(advancement);
        if(this.visible.add(advancement)){
            advancements.add(advancement);
            if (this.progress.containsKey(advancement)) {
                this.progressChanged.add(advancement);
                for(Advancement subAdvancement : advancement.getChildren()){
                    smc$setVisible(subAdvancement, advancements);
                }
            }
        }
    }

}
