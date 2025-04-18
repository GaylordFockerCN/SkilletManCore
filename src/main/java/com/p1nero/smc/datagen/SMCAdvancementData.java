package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SMCAdvancementData extends ForgeAdvancementProvider {
    public SMCAdvancementData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of(new SMCAdvancements()));

    }

    public static class SMCAdvancements implements AdvancementGenerator {

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
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.TASK, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID), existingFileHelper);

            Advancement noYourPower = registerAdvancement(root, "no_your_power", FrameType.TASK, Blocks.BARRIER);
            Advancement first5StarSkillet = registerAdvancement(root, "first_5star_skillet", FrameType.TASK, SMCRegistrateItems.IRON_SKILLET_LEVEL5.get());
            Advancement fakeSleep = registerAdvancement(root, "fake_sleep", FrameType.TASK, Items.RED_BED);
            Advancement tryPush = registerAdvancement(root, "try_push", FrameType.TASK, Blocks.PISTON);
            Advancement noMoney = registerAdvancement(root, "no_money", FrameType.TASK, Blocks.PISTON);
            Advancement selfEat = registerAdvancement(root, "self_eat", FrameType.TASK, Blocks.PISTON);

        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden) {
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

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden, CriterionTriggerInstance instance) {
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

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display) {
            return registerAdvancement(parent, name, type, display, true, true, true);
        }

    }

    public static void finishAdvancement(Advancement advancement, ServerPlayer serverPlayer) {
        AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criteria : progress.getRemainingCriteria()) {
                SMCPlayer.addMoney(200, serverPlayer);
                serverPlayer.playSound(SoundEvents.PLAYER_LEVELUP);
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static void finishAdvancement(String name, ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(SkilletManCoreMod.MOD_ID, name));
        if (advancement == null) {
            SkilletManCoreMod.LOGGER.info("advancement:\"" + name + "\" is null!");
            return;
        }
        finishAdvancement(advancement, serverPlayer);
    }

    public static boolean isDone(String name, ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(SkilletManCoreMod.MOD_ID, name));
        if (advancement == null) {
            SkilletManCoreMod.LOGGER.info("advancement:\"" + name + "\" is null!");
            return false;
        }
        return isDone(advancement, serverPlayer);
    }

    public static boolean isDone(Advancement advancement, ServerPlayer serverPlayer) {
        AdvancementProgress advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        return advancementProgress.isDone();
    }

}
