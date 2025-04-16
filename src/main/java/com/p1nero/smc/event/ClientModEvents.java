package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlockEntities;
import com.p1nero.smc.block.renderer.BetterStructureBlockRenderer;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.boss.goldenflame.client.BlackHoleRenderer;
import com.p1nero.smc.entity.custom.boss.goldenflame.client.FlameCircleRenderer;
import com.p1nero.smc.entity.custom.boss.goldenflame.client.GoldenFlamePatchedRenderer;
import com.p1nero.smc.entity.custom.boss.goldenflame.client.GoldenFlameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        //BOSS

        EntityRenderers.register(SMCEntities.GOLDEN_FLAME.get(), GoldenFlameRenderer::new);
        EntityRenderers.register(SMCEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
        EntityRenderers.register(SMCEntities.FLAME_CIRCLE.get(), FlameCircleRenderer::new);


        //NPC
        EntityRenderers.register(SMCEntities.START_NPC.get(), VillagerRenderer::new);

    }

    @SubscribeEvent
    public static void onRendererSetup(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(SMCBlockEntities.BETTER_STRUCTURE_BLOCK_ENTITY.get(), BetterStructureBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    /**
     * 需要先绑定Patch和 Armature
     * {@link SMCEntities#setArmature(ModelBuildEvent.ArmatureBuild)}
     * {@link SMCEntities#setPatch(EntityPatchRegistryEvent)}
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderPatched(PatchedRenderersEvent.Add event) {
        EntityRendererProvider.Context context = event.getContext();
        //BOSS
        event.addPatchedEntityRenderer(SMCEntities.GOLDEN_FLAME.get(), (entityType) -> new GoldenFlamePatchedRenderer(() -> Meshes.SKELETON, context, entityType).initLayerLast(context, entityType));
    }

}