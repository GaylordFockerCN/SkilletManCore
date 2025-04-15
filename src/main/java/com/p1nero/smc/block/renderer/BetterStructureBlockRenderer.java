package com.p1nero.smc.block.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BetterStructureBlockRenderer extends StructureBlockRenderer {

    public BetterStructureBlockRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    /**
     * 一直渲染，防止背对的时候没渲染
     */
    @Override
    public boolean shouldRender(@NotNull StructureBlockEntity pBlockEntity, @NotNull Vec3 pCameraPos) {
        if(Minecraft.getInstance().player != null){
            return Minecraft.getInstance().player.isCreative();
        }
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull StructureBlockEntity entity) {
        return true;
    }
}
