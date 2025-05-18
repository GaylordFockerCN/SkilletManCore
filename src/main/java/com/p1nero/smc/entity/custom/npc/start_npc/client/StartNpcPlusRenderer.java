package com.p1nero.smc.entity.custom.npc.start_npc.client;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

public class StartNpcPlusRenderer extends VillagerRenderer {
    private static final ResourceLocation VILLAGER_PLUS = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/entity/villager/villager_plus.png");
    public StartNpcPlusRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Villager villager) {
        return VILLAGER_PLUS;
    }
}
