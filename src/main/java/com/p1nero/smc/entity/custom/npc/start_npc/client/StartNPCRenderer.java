package com.p1nero.smc.entity.custom.npc.start_npc.client;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class StartNPCRenderer extends HumanoidMobRenderer<StartNPC, HumanoidModel<StartNPC>> {
    public StartNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull StartNPC entity) {
        return new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/entity/liu_guang.png");
    }
}
