package com.p1nero.smc.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.VillageStructures;

import java.util.List;

import static vectorwing.farmersdelight.common.world.VillageStructures.addBuildingToPool;

@Mixin(value = VillageStructures.class, remap = false)
public abstract class VillageStructuresMixin {

    @Shadow
    private static void addNewRuleToProcessorList(ResourceLocation targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
    }

    @Inject(method = "addNewVillageBuilding", at = @At("HEAD"))
    private static void smc$addNewVillageBuilding(ServerAboutToStartEvent event, CallbackInfo ci){
        if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
            Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).get();
            Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).get();

            VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), FarmersDelight.MODID + ":village/houses/plains_compost_pile", 5);
            VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), FarmersDelight.MODID + ":village/houses/snowy_compost_pile", 3);
            VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/savanna/houses"), FarmersDelight.MODID + ":village/houses/savanna_compost_pile", 4);
            VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/desert/houses"), FarmersDelight.MODID + ":village/houses/desert_compost_pile", 3);
            VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), FarmersDelight.MODID + ":village/houses/taiga_compost_pile", 4);
        }

        if (Configuration.GENERATE_VILLAGE_FARM_FD_CROPS.get()) {
            Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();

            StructureProcessor temperateCropProcessor = new RuleProcessor(List.of(
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.TOMATO_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState())
            ));

            StructureProcessor coldCropProcessor = new RuleProcessor(List.of(
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.POTATOES, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.POTATOES, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState())
            ));

            StructureProcessor aridCropProcessor = new RuleProcessor(List.of(
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
                    new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.TOMATO_CROP.get().defaultBlockState())
            ));

            addNewRuleToProcessorList(new ResourceLocation("minecraft:farm_plains"), temperateCropProcessor, processorLists);
            addNewRuleToProcessorList(new ResourceLocation("minecraft:farm_savanna"), aridCropProcessor, processorLists);
            addNewRuleToProcessorList(new ResourceLocation("minecraft:farm_snowy"), coldCropProcessor, processorLists);
            addNewRuleToProcessorList(new ResourceLocation("minecraft:farm_taiga"), temperateCropProcessor, processorLists);
            addNewRuleToProcessorList(new ResourceLocation("minecraft:farm_desert"), aridCropProcessor, processorLists);
        }
    }
}
