package com.p1nero.smc.entity.custom.boss.goldenflame.client;

import com.p1nero.smc.entity.custom.boss.goldenflame.FlameCircleEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlameCircleRenderer extends EntityRenderer<FlameCircleEntity> {
    public FlameCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(@NotNull FlameCircleEntity p_114491_, @NotNull Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FlameCircleEntity blackHoleEntity) {
        return new ResourceLocation("");
    }
}
