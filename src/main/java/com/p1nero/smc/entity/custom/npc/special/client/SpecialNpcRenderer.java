package com.p1nero.smc.entity.custom.npc.special.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.special.SpecialNpc;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.utils.math.QuaternionUtils;

public class SpecialNpcRenderer extends VillagerRenderer {
    public static ItemStack icon;
   public SpecialNpcRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderNameTag(@NotNull Villager villager, @NotNull Component component, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int p_114502_) {
        super.renderNameTag(villager, component, poseStack, bufferSource, p_114502_);
        if(villager instanceof SpecialNpc specialNpc && villager.isAlive() && !specialNpc.isTalked()) {

            if(icon == null) {
                icon = SMCRegistrateItems.TASK_TIP_ICON.get().getDefaultInstance();
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, villager.getBbHeight() + 1, 0.0F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(QuaternionUtils.YP.rotationDegrees(180));
            poseStack.scale(0.7F, 0.7F, 0.7F);
            BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(icon);
            Minecraft.getInstance().getItemRenderer().render(icon, ItemDisplayContext.GUI,
                    false, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }
    }

}
