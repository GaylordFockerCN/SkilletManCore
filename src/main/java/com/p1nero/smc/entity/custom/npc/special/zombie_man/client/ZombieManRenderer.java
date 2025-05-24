package com.p1nero.smc.entity.custom.npc.special.zombie_man.client;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.npc.special.zombie_man.ZombieMan;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

public class ZombieManRenderer extends HumanoidMobRenderer<ZombieMan, HumanoidModel<ZombieMan>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/entity/zombie_sunglass.png");

    public ZombieManRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ZombieMan zombieMan) {
        return TEXTURE;
    }

}
