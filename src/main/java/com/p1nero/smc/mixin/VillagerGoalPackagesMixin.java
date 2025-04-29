package com.p1nero.smc.mixin;

import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让村民响应自定义袭击
 */
@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {

    @Inject(method = "raidExistsAndActive", at = @At("HEAD"), cancellable = true)
    private static void smc$raidExistsAndActive(ServerLevel serverLevel, LivingEntity p_260163_, CallbackInfoReturnable<Boolean> cir){
        if(!DummyEntityManager.getDummyEntities(serverLevel).isEmpty()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "raidExistsAndNotVictory", at = @At("HEAD"), cancellable = true)
    private static void smc$raidExistsAndNotVictory(ServerLevel serverLevel, LivingEntity p_259384_, CallbackInfoReturnable<Boolean> cir){
        if(!DummyEntityManager.getDummyEntities(serverLevel).isEmpty()){
            cir.setReturnValue(true);
        }
    }

}
