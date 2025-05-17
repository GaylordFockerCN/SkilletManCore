package com.p1nero.smc.entity.custom.npc.special.virgil.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.entity.custom.npc.special.virgil.VirgilVillager;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.utils.math.QuaternionUtils;

public class VirgilVillagerRenderer extends IllagerRenderer<VirgilVillager> {
    public static ItemStack icon;
    private static final ResourceLocation VINDICATOR = ResourceLocation.parse("textures/entity/illager/vindicator.png");

    public VirgilVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected void renderNameTag(@NotNull VirgilVillager villager, @NotNull Component component, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int p_114502_) {
        super.renderNameTag(villager, component, poseStack, bufferSource, p_114502_);
        if (icon == null) {
            icon = SMCRegistrateItems.TASK_TIP_ICON.get().getDefaultInstance();
        }
        if(villager.isAlive() && !villager.isTalked() && !villager.isFighting()){
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

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull VirgilVillager villager) {
        return VINDICATOR;
    }
}
