package com.p1nero.smc.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SMCAdvancementData extends ForgeAdvancementProvider {
    public static final String FOOD_ADV_PRE = "food_adv_unlock_";
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

            Advancement startWork = registerAdvancement(root, "start_work", FrameType.TASK, SMCRegistrateItems.SPATULA_V5, true, true, false);
            Advancement money1000 = registerAdvancement(startWork, "money1000", FrameType.TASK, Items.DIAMOND, true, true, false);
            Advancement money1000000 = registerAdvancement(money1000, "money1000000", FrameType.TASK, Items.EMERALD, true, true, false);
            Advancement money1000000000 = registerAdvancement(money1000000, "money1000000000", FrameType.TASK, Items.EMERALD_BLOCK, true, true, false);
            Advancement level1 = registerAdvancement(startWork, "level1", FrameType.TASK, CDItems.SPATULA, true, true, false);
            Advancement level2 = registerAdvancement(level1, "level2", FrameType.TASK, SMCRegistrateItems.GOLDEN_SPATULA, true, true, false);
            Advancement level3 = registerAdvancement(level2, "level3", FrameType.TASK, SMCRegistrateItems.DIAMOND_SPATULA, true, true, false);
            Advancement special_customer_1 = registerAdvancement(startWork, "special_customer_1", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement special_customer_2 = registerAdvancement(special_customer_1, "special_customer_2", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement special_customer_3 = registerAdvancement(special_customer_2, "special_customer_3", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement hijackCustomer = registerAdvancement(startWork, "hijack_customer", FrameType.TASK, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_30"))));
            Advancement dirtPlate = registerAdvancement(startWork, "dirt_plate", FrameType.TASK, SMCRegistrateItems.DIRT_PLATE);
            Advancement heShen = registerAdvancement(startWork, "he_shen", FrameType.TASK, SMCRegistrateItems.IRON_SKILLET_LEVEL5);
            Advancement twoKid = registerAdvancement(heShen, "two_kid", FrameType.TASK, SMCRegistrateItems.GOLDEN_SKILLET_V3);
            Advancement thief = registerAdvancement(twoKid, "thief", FrameType.TASK, SMCRegistrateItems.DIAMOND_SKILLET);
            Advancement virgil = registerAdvancement(thief, "virgil", FrameType.TASK, SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get());

            Advancement startFight = registerAdvancement(root, "start_fight", FrameType.TASK, Items.IRON_SWORD, true, true, false);
            Advancement dodgeMaster = registerAdvancement(startFight, "dodge_master", FrameType.TASK, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster = registerAdvancement(startFight, "parry_master", FrameType.TASK, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement dodgeMaster2 = registerAdvancement(dodgeMaster, "dodge_master2", FrameType.TASK, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster2 = registerAdvancement(parryMaster, "parry_master2", FrameType.TASK, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement dodgeMaster3 = registerAdvancement(dodgeMaster2, "dodge_master3", FrameType.TASK, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster3 = registerAdvancement(parryMaster2, "parry_master3", FrameType.TASK, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement raid10d = registerAdvancement(startFight, "raid10d", FrameType.TASK, Items.GOLDEN_SWORD, true, true, false);//抵御袭击天数
            Advancement raid20d = registerAdvancement(raid10d, "raid20d", FrameType.TASK, Items.DIAMOND_SWORD, true, true, false);//抵御袭击天数
            Advancement raid30d = registerAdvancement(raid20d, "raid30d", FrameType.TASK, Items.NETHERITE_SWORD, true, true, false);//抵御袭击天数

            Advancement startChangeVillager = registerAdvancement(root, "change_villager", FrameType.TASK, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_24"))), true, true, false);
            Advancement talkToCleric = registerAdvancement(startChangeVillager, "talk_to_cleric", FrameType.TASK, Items.ENDER_EYE, true, true, false);
            Advancement end = registerAdvancement(talkToCleric, "end", FrameType.CHALLENGE, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_3"))), true, true, false);
            //TODO
            Advancement trueEnd = registerAdvancement(end, "true_end", FrameType.CHALLENGE, Items.NETHER_STAR, true, true, true);

            Advancement firstGacha = registerAdvancement(root, "first_gacha", FrameType.TASK, ModItems.DOLL_MACHINE.get(), true, true, false);
            Advancement first5StarSkillet = registerAdvancement(firstGacha, "first_5star_skillet", FrameType.TASK, SMCRegistrateItems.IRON_SKILLET_LEVEL5, true, true, false);
            Advancement first5StarItem = registerAdvancement(firstGacha, "first_5star_item", FrameType.TASK, SMCRegistrateItems.DIAMOND_SPATULA_V5, true, true, false);

            Advancement dogNoEat = registerAdvancement(root, "dog_no_eat", FrameType.TASK, Items.BONE);
            Advancement noYourPower = registerAdvancement(root, "no_your_power", FrameType.TASK, Blocks.BARRIER);
            Advancement fakeSleep = registerAdvancement(root, "fake_sleep", FrameType.TASK, Items.RED_BED);
            Advancement tryPush = registerAdvancement(root, "try_push", FrameType.TASK, Blocks.PISTON);
            Advancement noMoney = registerAdvancement(root, "no_money", FrameType.TASK, Items.EMERALD);
            Advancement selfEat = registerAdvancement(root, "self_eat", FrameType.TASK, Items.BREAD);
            Advancement tooManyMouth = registerAdvancement(root, "too_many_mouth", FrameType.TASK, ModItems.DOLL_ICON.get());
            Advancement makeCustomerCry = registerAdvancement(root, "got_fox", FrameType.TASK, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_45"))));
            Advancement preCook = registerAdvancement(startWork, "pre_cook", FrameType.TASK, PlateFood.FRIED_RICE.item);


            Advancement foodsRoot = Advancement.Builder.advancement()
                    .display(CDItems.PLATE.asItem(),
                            Component.translatable(pre + SkilletManCoreMod.MOD_ID + "_food"),
                            Component.translatable(pre + SkilletManCoreMod.MOD_ID + "_food" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.TASK, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_food"), existingFileHelper);

            for(PlateFood plateFood : PlateFood.values()){
                String name = FOOD_ADV_PRE + plateFood.name().toLowerCase(Locale.ROOT);
                Advancement.Builder.advancement()
                        .parent(foodsRoot)
                        .display(plateFood.item,
                                plateFood.item.asStack().getDisplayName(),
                                SkilletManCoreMod.getInfo("food_adv_unlock_pre").append(plateFood.item.asStack().getDisplayName()),
                                null,
                                FrameType.TASK, true, true, false)
                        .addCriterion(name, new ImpossibleTrigger.TriggerInstance())
                        .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), existingFileHelper);
            }

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
                serverPlayer.getAdvancements().award(advancement, criteria);
            }
        }
    }

    public static void finishAdvancement(String name, ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(SkilletManCoreMod.MOD_ID, name));
        if (advancement == null) {
            SkilletManCoreMod.LOGGER.error("advancement:\"" + name + "\" is null!");
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
