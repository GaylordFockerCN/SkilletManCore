package com.p1nero.smc.entity.custom.super_golem.client;

import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;
import org.jetbrains.annotations.NotNull;

public class SuperGolemRenderer<T extends IronGolem> extends MobRenderer<T, IronGolemModel<T>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");
    public SuperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T superBadIronGolem) {
        return GOLEM_LOCATION;
    }
}
