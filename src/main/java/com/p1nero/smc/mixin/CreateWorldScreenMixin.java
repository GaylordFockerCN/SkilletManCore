package com.p1nero.smc.mixin;

import com.p1nero.smc.worldgen.biome.SMCBiomeProvider;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Optional;

/**
 * 用于在创建地图的时候获取存档名。因为创建地图和进入世界是两个地方，保存位置不同。
 */
@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Shadow @Final
    WorldCreationUiState uiState;

    @Inject(method = "createNewWorldDirectory()Ljava/util/Optional;", at = @At("HEAD"))
    private void injected(CallbackInfoReturnable<Optional<LevelStorageSource.LevelStorageAccess>> cir){
        SMCBiomeProvider.worldName = new File(uiState.getTargetFolder()).getName();
        SMCBiomeProvider.LOGGER.info("On Create New World Sync : " + SMCBiomeProvider.worldName + " >> TCRBiomeProvider.worldName");
    }
}
