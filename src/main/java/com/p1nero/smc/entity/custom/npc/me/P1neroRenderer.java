package com.p1nero.smc.entity.custom.npc.me;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class P1neroRenderer extends HumanoidMobRenderer<P1nero, HumanoidModel<P1nero>> {

    private final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/entity/p1nero.png");
    public P1neroRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull P1nero p1nero) {
        return TEXTURE;
    }
}
