package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.datagen.loot.SMCLootTableProvider;
import com.p1nero.smc.datagen.sound.SMCSoundGenerator;
import com.p1nero.smc.datagen.tags.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Client
        generator.addProvider(event.includeClient(), new SMCBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new SMCItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new SMCLangGenerator(output));
        generator.addProvider(event.includeClient(), new SMCSoundGenerator(output, helper));

        //Server
        generator.addProvider(event.includeServer(), new SMCRecipeGenerator(output));
        generator.addProvider(event.includeServer(), SMCLootTableProvider.create(output));
        generator.addProvider(event.includeServer(), new SMCAdvancementData(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new SMCEntityTagGenerator(output, lookupProvider, helper));

        SMCBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new SMCBlockTagGenerator(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new SMCItemTagGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new SMCPoiTypeTagsProvider(output, lookupProvider, helper));

        //应该不需要维度
//        DatapackBuiltinEntriesProvider datapackProvider = new SMCWorldGenProvider(output, lookupProvider);
//        CompletableFuture<HolderLookup.Provider> provider = datapackProvider.getRegistryProvider();
//        generator.addProvider(event.includeServer(), new SMCBiomeTagGenerator(output, provider, helper));
//        generator.addProvider(event.includeServer(), datapackProvider);
    }
}
