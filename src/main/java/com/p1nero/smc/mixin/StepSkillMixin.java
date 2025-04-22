package com.p1nero.smc.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.dodge.StepSkill;

/**
 * 修复八向跨步
 */
@Mixin(value = StepSkill.class, remap = false)
public class StepSkillMixin {

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "getExecutionPacket", at = @At("HEAD"), cancellable = true)
    private void smc$getExecutionPacket(LocalPlayerPatch executor, FriendlyByteBuf args, CallbackInfoReturnable<Object> cir) {
        Input input = executor.getOriginal().input;
        float pulse = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(executor.getOriginal()), 0.0F, 1.0F);
        input.tick(false, pulse);
        int forward = input.up ? 1 : 0;
        int backward = input.down ? -1 : 0;
        int left = input.left ? 1 : 0;
        int right = input.right ? -1 : 0;
        int vertic = forward + backward;
        int horizon = left + right;
        float yRot = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
        float degree = (float) (-(90 * horizon * (1 - Math.abs(vertic)) + 45 * vertic * horizon)) + yRot;
        int animation;
        if (vertic == 0) {
            if (horizon == 0) {
                animation = 0;
            } else {
                animation = horizon >= 0 ? 2 : 3;
            }
        } else {
            animation = vertic >= 0 ? 0 : 1;
        }

        CPExecuteSkill packet = new CPExecuteSkill(SkillSlots.DODGE.universalOrdinal());
        packet.getBuffer().writeInt(animation);
        packet.getBuffer().writeFloat((vertic == 0 && horizon != 0) ? yRot : degree);
        cir.setReturnValue(packet);
    }
}
