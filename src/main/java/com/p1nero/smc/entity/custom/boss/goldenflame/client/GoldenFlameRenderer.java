package com.p1nero.smc.entity.custom.boss.goldenflame.client;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GoldenFlameRenderer extends HumanoidMobRenderer<GoldenFlame, HumanoidModel<GoldenFlame>> {
    public GoldenFlameRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public boolean shouldRender(@NotNull GoldenFlame goldenFlame, @NotNull Frustum frustum, double p_115470_, double p_115471_, double p_115472_) {
        if(!goldenFlame.shouldRender()){
            return false;
        }
        return super.shouldRender(goldenFlame, frustum, p_115470_, p_115471_, p_115472_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GoldenFlame entity) {
        return ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/entity/golden_flame.png");
    }
}
