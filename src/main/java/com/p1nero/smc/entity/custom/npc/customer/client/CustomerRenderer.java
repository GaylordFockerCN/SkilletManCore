package com.p1nero.smc.entity.custom.npc.customer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

public class CustomerRenderer extends VillagerRenderer {
    public CustomerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderNameTag(@NotNull Villager villager, @NotNull Component component, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int p_114502_) {
        super.renderNameTag(villager, component, poseStack, bufferSource, p_114502_);
        if(villager instanceof Customer customer && !customer.getOrder().isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(customer.getOrder());
            Minecraft.getInstance().getItemRenderer().render(customer.getOrder(), ItemDisplayContext.GUI, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }

    }
}
