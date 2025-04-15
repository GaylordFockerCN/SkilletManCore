package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SMCAdvancementData extends ForgeAdvancementProvider {
    public SMCAdvancementData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of(new DOTEAdvancements()));

    }

    public static class DOTEAdvancements implements AdvancementGenerator {

        public final String pre = "advancement." + SkilletManCoreMod.MOD_ID + ".";
        private Consumer<Advancement> consumer;
        private ExistingFileHelper helper;
        @SuppressWarnings("unused")
        @Override
        public void generate(HolderLookup.@NotNull Provider provider, @NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper existingFileHelper) {
            this.consumer = consumer;
            this.helper = existingFileHelper;

            Advancement root = Advancement.Builder.advancement()
                    .display(CDItems.SKILLET.asItem(),
                            Component.translatable(pre + SkilletManCoreMod.MOD_ID),
                            Component.translatable(pre + SkilletManCoreMod.MOD_ID + ".desc"),
                            new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/block/bg.png"),
                            FrameType.TASK, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(SMCDimension.P_SKY_ISLAND_LEVEL_KEY))
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID), existingFileHelper);

//            //打boss的成就
//            Advancement seed = registerAdvancement(root, "seed", FrameType.TASK, DOTEItems.HOLY_RADIANCE_SEED.get());
//            Advancement core = registerAdvancement(seed, "core", FrameType.TASK, DOTEItems.CORE_OF_HELL.get());
//            Advancement goldenFlame = registerAdvancement(seed, "golden_flame", FrameType.TASK, DOTEItems.WITHERC.get());
//            Advancement end = registerAdvancement(goldenFlame, "book", FrameType.TASK, DOTEItems.BOOK_OF_ENDING.get());
//
//            //骑士线
//            Advancement knight = registerAdvancement(core, "knight", FrameType.TASK, DOTEItems.ROT_GREATSWORD.get());
//            Advancement loyal = registerAdvancement(knight, "loyal", FrameType.TASK, DOTEItems.WKNIGHT_CHESTPLATE.get());
//
//            //一般结局
//            Advancement unfinished = registerAdvancement(knight, "unfinished", FrameType.TASK, DOTEItems.ADGRAIN.get());
//
//            //终焉线
//            Advancement star = registerAdvancement(end, "star", FrameType.TASK, Items.NETHER_STAR);

        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden){
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(display,
                            Component.translatable(pre + name),
                            Component.translatable(pre + name + ".desc"),
                            null,
                            type, showToast, announceToChat, hidden)
                    .addCriterion(name, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), helper);
        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden, CriterionTriggerInstance instance){
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(display,
                            Component.translatable(pre + name),
                            Component.translatable(pre + name + ".desc"),
                            null,
                            type, true, true, true)
                    .addCriterion(name, instance)
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), helper);
        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display){
            return registerAdvancement(parent, name, type, display, true, true, true);
        }


    }

    public static void getAdvancement(String name, ServerPlayer serverPlayer){
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(SkilletManCoreMod.MOD_ID,name));
        if(advancement == null){
            SkilletManCoreMod.LOGGER.info("advancement:\"" + name + "\" is null!");
            return;
        }
        AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criteria : progress.getRemainingCriteria())
                serverPlayer.getAdvancements().award(advancement, criteria);
        }
    }

    public static boolean isDone(String name, ServerPlayer serverPlayer){
        Advancement _adv = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(SkilletManCoreMod.MOD_ID,name));
        if(_adv == null){
            SkilletManCoreMod.LOGGER.info("advancement:\""+name+"\" is null!");
            return false;
        }
        AdvancementProgress _ap = serverPlayer.getAdvancements().getOrStartProgress(_adv);
        return _ap.isDone();
    }

}
