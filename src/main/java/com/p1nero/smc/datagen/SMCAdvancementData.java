package com.p1nero.smc.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.gacha.ArmorGachaSystem;
import com.simibubi.create.AllItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.kenddie.fantasyarmor.config.FAConfig;
import net.kenddie.fantasyarmor.item.FAItems;
import net.kenddie.fantasyarmor.item.armor.lib.FAArmorItem;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.world.item.EpicFightItems;
import yesman.epicfight.world.item.SkillBookItem;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SMCAdvancementData extends ForgeAdvancementProvider {
    public static final String FOOD_ADV_PRE = "food_adv_unlock_";

    public static final String PRE = "advancement." + SkilletManCoreMod.MOD_ID + ".";
    public SMCAdvancementData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper) {
        super(output, registries, helper, List.of(new SMCAdvancements()));

    }

    public static class SMCAdvancements implements AdvancementGenerator {
        private Consumer<Advancement> consumer;
        private ExistingFileHelper helper;

        @SuppressWarnings("unused")
        @Override
        public void generate(HolderLookup.@NotNull Provider provider, @NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper existingFileHelper) {
            this.consumer = consumer;
            this.helper = existingFileHelper;

            Advancement root = Advancement.Builder.advancement()
                    .display(CDItems.SKILLET.asItem(),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.GOAL, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID), existingFileHelper);

            Advancement startWork = registerAdvancement(root, "start_work", FrameType.TASK, SMCRegistrateItems.SPATULA_V5, true, true, false);
            Advancement money1000 = registerAdvancement(startWork, "money1000", FrameType.GOAL, Items.DIAMOND, true, true, false);
            Advancement money1000000 = registerAdvancement(money1000, "money1000000", FrameType.GOAL, Items.EMERALD, true, true, false);
            Advancement money1000000000 = registerAdvancement(money1000000, "money1000000000", FrameType.GOAL, Items.EMERALD_BLOCK, true, true, false);
            Advancement stage1 = registerAdvancement(startWork, "stage1", FrameType.GOAL, CDItems.SPATULA, true, true, false);
            Advancement stage2 = registerAdvancement(stage1, "stage2", FrameType.GOAL, SMCRegistrateItems.GOLDEN_SPATULA, true, true, false);
            Advancement stage3 = registerAdvancement(stage2, "stage3", FrameType.GOAL, SMCRegistrateItems.DIAMOND_SPATULA, true, true, false);
            Advancement special_customer_1 = registerAdvancement(startWork, "special_customer_1", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement special_customer_2 = registerAdvancement(special_customer_1, "special_customer_2", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement special_customer_3 = registerAdvancement(special_customer_2, "special_customer_3", FrameType.TASK, Items.PUFFERFISH, true, true, false);
            Advancement hijackCustomer = registerAdvancement(startWork, "hijack_customer", FrameType.TASK, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_30"))));
            Advancement dirtPlate = registerAdvancement(startWork, "dirt_plate", FrameType.TASK, SMCRegistrateItems.DIRT_PLATE);
            Advancement heShen = registerAdvancement(startWork, "he_shen", FrameType.TASK, SMCRegistrateItems.IRON_SKILLET_LEVEL5);
            Advancement twoKid = registerAdvancement(heShen, "two_kid", FrameType.TASK, SMCRegistrateItems.GOLDEN_SKILLET_V3);
            Advancement thief = registerAdvancement(twoKid, "thief", FrameType.TASK, SMCRegistrateItems.DIAMOND_SKILLET);
            Advancement virgil = registerAdvancement(thief, "virgil", FrameType.TASK, SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get());
            Advancement noMoney = registerAdvancement(startWork, "no_money", FrameType.TASK, Items.EMERALD);

            Advancement startFight = registerAdvancement(root, "start_fight", FrameType.TASK, Items.IRON_SWORD, true, true, false);
            Advancement dodgeMaster = registerAdvancement(startFight, "dodge_master", FrameType.GOAL, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster = registerAdvancement(startFight, "parry_master", FrameType.GOAL, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement dodgeMaster2 = registerAdvancement(dodgeMaster, "dodge_master2", FrameType.GOAL, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster2 = registerAdvancement(parryMaster, "parry_master2", FrameType.GOAL, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement dodgeMaster3 = registerAdvancement(dodgeMaster2, "dodge_master3", FrameType.GOAL, SMCRegistrateItems.DODGE_ICON, true, true, false);//闪避10次， 100次， 1000次
            Advancement parryMaster3 = registerAdvancement(parryMaster2, "parry_master3", FrameType.GOAL, SMCRegistrateItems.PARRY_ICON, true, true, false);//招架10次， 100次， 1000次
            Advancement raid10d = registerAdvancement(startFight, "raid10d", FrameType.GOAL, Items.GOLDEN_SWORD, true, true, false);//抵御袭击天数
            Advancement raid20d = registerAdvancement(raid10d, "raid20d", FrameType.GOAL, Items.DIAMOND_SWORD, true, true, false);//抵御袭击天数
            Advancement raid30d = registerAdvancement(raid20d, "raid30d", FrameType.GOAL, Items.NETHERITE_SWORD, true, true, false);//抵御袭击天数

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
            Advancement selfEat = registerAdvancement(root, "self_eat", FrameType.TASK, Items.BREAD);
            Advancement tooManyMouth = registerAdvancement(root, "too_many_mouth", FrameType.TASK, ModItems.DOLL_ICON.get());
            Advancement makeCustomerCry = registerAdvancement(root, "got_fox", FrameType.TASK, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_45"))));
            Advancement preCook = registerAdvancement(startWork, "pre_cook", FrameType.TASK, PlateFood.FRIED_RICE.item);

            Advancement foodsRoot = Advancement.Builder.advancement()
                    .display(CDItems.PLATE.asItem(),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_food"),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_food" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.TASK, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_food"), existingFileHelper);

            List<PlateFood> list = Arrays.stream(PlateFood.values()).toList();
            Advancement last = null;
            for(int i = 0; i < list.size(); i++){
                PlateFood plateFood = list.get(i);
                String name = FOOD_ADV_PRE + plateFood.name().toLowerCase(Locale.ROOT);
                last = Advancement.Builder.advancement()
                        .parent(last == null ? foodsRoot : last)
                        .display(plateFood.item,
                                plateFood.item.asStack().getDisplayName(),
                                SkilletManCoreMod.getInfo("food_adv_unlock_pre").append(plateFood.item.asStack().getDisplayName()),
                                null,
                                FrameType.GOAL, true, true, false)
                        .addCriterion(name, new ImpossibleTrigger.TriggerInstance())
                        .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), existingFileHelper);

                if(i % 5 == 0) {
                    last = null;
                }
            }

            Advancement weaponRoot = Advancement.Builder.advancement()
                    .display(Items.IRON_SWORD,
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_weapon"),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_weapon" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.GOAL, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_weapon"), existingFileHelper);

            Advancement spatula = registerItemAdvancement(weaponRoot, CDItems.SPATULA);
            Advancement spatula2 = registerItemAdvancement(spatula, SMCRegistrateItems.SPATULA_V2);
            Advancement spatula3 = registerItemAdvancement(spatula2, SMCRegistrateItems.SPATULA_V3);
            Advancement spatula4 = registerItemAdvancement(spatula3, SMCRegistrateItems.SPATULA_V4);
            Advancement spatula5 = registerItemAdvancement(spatula4, SMCRegistrateItems.SPATULA_V5);
            Advancement gSpatula = registerItemAdvancement(spatula, SMCRegistrateItems.GOLDEN_SPATULA);
            Advancement gSpatula2 = registerItemAdvancement(gSpatula, SMCRegistrateItems.GOLDEN_SPATULA_V2);
            Advancement gSpatula3 = registerItemAdvancement(gSpatula2, SMCRegistrateItems.GOLDEN_SPATULA_V3);
            Advancement gSpatula4 = registerItemAdvancement(gSpatula3, SMCRegistrateItems.GOLDEN_SPATULA_V4);
            Advancement gSpatula5 = registerItemAdvancement(gSpatula4, SMCRegistrateItems.GOLDEN_SPATULA_V5);
            Advancement dSpatula = registerItemAdvancement(spatula, SMCRegistrateItems.DIAMOND_SPATULA);
            Advancement dSpatula2 = registerItemAdvancement(dSpatula, SMCRegistrateItems.DIAMOND_SPATULA_V2);
            Advancement dSpatula3 = registerItemAdvancement(dSpatula2, SMCRegistrateItems.DIAMOND_SPATULA_V3);
            Advancement dSpatula4 = registerItemAdvancement(dSpatula3, SMCRegistrateItems.DIAMOND_SPATULA_V4);
            Advancement dSpatula5 = registerItemAdvancement(dSpatula4, SMCRegistrateItems.DIAMOND_SPATULA_V5);
            Advancement skillet = registerItemAdvancement(weaponRoot, CDItems.SKILLET);
            Advancement skillet2 = registerItemAdvancement(skillet, SMCRegistrateItems.IRON_SKILLET_LEVEL2);
            Advancement skillet3 = registerItemAdvancement(skillet2, SMCRegistrateItems.IRON_SKILLET_LEVEL3);
            Advancement skillet4 = registerItemAdvancement(skillet3, SMCRegistrateItems.IRON_SKILLET_LEVEL4);
            Advancement skillet5 = registerItemAdvancement(skillet4, SMCRegistrateItems.IRON_SKILLET_LEVEL5);
            Advancement gSkillet = registerItemAdvancement(skillet, SMCRegistrateItems.GOLDEN_SKILLET);
            Advancement gSkillet2 = registerItemAdvancement(gSkillet, SMCRegistrateItems.GOLDEN_SKILLET_V2);
            Advancement gSkillet3 = registerItemAdvancement(gSkillet2, SMCRegistrateItems.GOLDEN_SKILLET_V3);
            Advancement gSkillet4 = registerItemAdvancement(gSkillet3, SMCRegistrateItems.GOLDEN_SKILLET_V4);
            Advancement gSkillet5 = registerItemAdvancement(gSkillet4, SMCRegistrateItems.GOLDEN_SKILLET_V5);
            Advancement dSkillet = registerItemAdvancement(skillet, SMCRegistrateItems.DIAMOND_SKILLET);
            Advancement dSkillet2 = registerItemAdvancement(dSkillet, SMCRegistrateItems.DIAMOND_SKILLET_V2);
            Advancement dSkillet3 = registerItemAdvancement(dSkillet2, SMCRegistrateItems.DIAMOND_SKILLET_V3);
            Advancement dSkillet4 = registerItemAdvancement(dSkillet3, SMCRegistrateItems.DIAMOND_SKILLET_V4);
            Advancement dSkillet5 = registerItemAdvancement(dSkillet4, SMCRegistrateItems.DIAMOND_SKILLET_V5);
            Advancement skilletSpatula = registerItemAdvancement(weaponRoot, SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get());


            Advancement armorRoot = Advancement.Builder.advancement()
                    .display(FAItems.HERO_HELMET.get(),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_armor"),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_armor" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.GOAL, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_armor"), existingFileHelper);
            FAConfig.showDescriptions = true;
            Advancement lastArmorAdv = null;
            List<ItemStack> allArmors = new ArrayList<>();
            ArmorGachaSystem.initItemList();
            allArmors.addAll(ArmorGachaSystem.STAR5_LIST);
            allArmors.addAll(ArmorGachaSystem.STAR4_LIST);
            for(int i = 0; i < allArmors.size(); i++){
                ItemStack itemStack = allArmors.get(i);
                if(itemStack.getItem() instanceof FAArmorItem faArmorItem){
                    String name = "armor_adv_" + itemStack.getItem().getDescriptionId();
                    MutableComponent desc = Component.empty();
                    List<Component> descList = new ArrayList<>();
                    itemStack.getItem().appendHoverText(itemStack, null, descList, TooltipFlag.NORMAL);
                    for(Component component : descList){
                        desc.append(component).append("\n");
                    }
                    desc.append("\n").append(SkilletManCoreMod.getInfo("wear_effect"));
                    for(MobEffectInstance mobEffect : faArmorItem.getFullSetEffects()){
                        desc.append("\n").append(mobEffect.getEffect().getDisplayName());
                    }
                    lastArmorAdv = Advancement.Builder.advancement()
                            .parent(lastArmorAdv == null ? armorRoot : lastArmorAdv)
                            .display(itemStack,
                                    itemStack.getDisplayName(),
                                    desc,
                                    null,
                                    FrameType.TASK, true, true, false)
                            .addCriterion(name, InventoryChangeTrigger.TriggerInstance.hasItems(itemStack.getItem()))
                            .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), existingFileHelper);

                    if((i + 1) % 4 == 0) {
                        lastArmorAdv = null;
                    }
                }
            }

            Advancement skillRoot = Advancement.Builder.advancement()
                    .display(EpicFightItems.SKILLBOOK.get(),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_skill"),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_skill" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.GOAL, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_skill"), existingFileHelper);

            AtomicReference<Advancement> lastSkillAdv = new AtomicReference<>(null);
            AtomicReference<SkillCategory> lastCategory = new AtomicReference<>(null);
            SkillManager.getSkills(skill -> skill.getCategory().learnable()).stream().sorted(Comparator.comparingInt(s -> s.getCategory().universalOrdinal())).forEach(skill -> {
                if(lastCategory.get() == null) {
                    lastCategory.set(skill.getCategory());
                }
                if(lastCategory.get() != skill.getCategory()){
                    lastSkillAdv.set(null);
                }
                String name = "skill_adv_" + skill.getRegistryName().getNamespace() + "_" + skill.getRegistryName().getPath();
                ItemStack stack = new ItemStack(EpicFightItems.SKILLBOOK.get());
                SkillBookItem.setContainingSkill(skill, stack);
                lastSkillAdv.set(Advancement.Builder.advancement()
                        .parent(lastSkillAdv.get() == null ? skillRoot : lastSkillAdv.get())
                        .display(stack,
                                Component.literal("========").append(skill.getDisplayName()).append("========"),
                                Component.translatable(skill.getTranslationKey() + ".tooltip"),
                                null,
                                FrameType.TASK, true, true, false)
                        .addCriterion(name, new ImpossibleTrigger.TriggerInstance())
                        .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), existingFileHelper));
                lastCategory.set(skill.getCategory());
            });


            Advancement levelRoot = Advancement.Builder.advancement()
                    .display(SMCRegistrateItems.TASK_TIP_ICON,
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_level"),
                            Component.translatable(PRE + SkilletManCoreMod.MOD_ID + "_level" + ".desc"),
                            new ResourceLocation("textures/block/bricks.png"),
                            FrameType.GOAL, false, false, false)
                    .addCriterion(SkilletManCoreMod.MOD_ID, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, SkilletManCoreMod.MOD_ID + "_level"), existingFileHelper);

            Advancement level5 = registerAdvancement(levelRoot, "level5", FrameType.GOAL, SMCRegistrateItems.IRON_SKILLET_LEVEL5, true, true, false);
            Advancement level5_1 = registerAdvancement(level5, "level5_1", FrameType.GOAL, net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.FEEDING_UPGRADE.get(), true, true, false);//突破奖励
            Advancement level5_2 = registerAdvancement(level5, "level5_2", FrameType.GOAL, SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get(), true, true, false);//突破奖励
            Advancement level10 = registerAdvancement(level5_2, "level10", FrameType.GOAL, SMCRegistrateItems.GOLDEN_SPATULA_V5, true, true, false);
            Advancement level10_1 = registerAdvancement(level10, "level10_1", FrameType.GOAL, net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.ADVANCED_FEEDING_UPGRADE.get(), true, true, false);
            Advancement level10_2 = registerAdvancement(level10, "level10_2", FrameType.GOAL, AllItems.GOGGLES, true, true, false);
            Advancement level15 = registerAdvancement(level10_2, "level15", FrameType.GOAL, SMCRegistrateItems.GOLDEN_SKILLET_V5, true, true,  false);
            Advancement level20 = registerAdvancement(level15, "level20", FrameType.GOAL, SMCRegistrateItems.DIAMOND_SPATULA_V4, true, true, false);
            Advancement level20_1 = registerAdvancement(level20, "level20_1", FrameType.GOAL, FAItems.DRAGONSLAYER_HELMET.get(), true, true, false);//突破奖励
            Advancement level20_2 = registerAdvancement(level20, "level20_2", FrameType.GOAL, FAItems.DRAGONSLAYER_CHESTPLATE.get(), true, true, false);//突破奖励
            Advancement level25 = registerAdvancement(level20_2, "level25", FrameType.GOAL, SMCRegistrateItems.DIAMOND_SKILLET_V4, true, true, false);
            Advancement level30 = registerAdvancement(level25, "level30", FrameType.GOAL, SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get(), true, true, false);


        }

        public Advancement registerItemAdvancement(Advancement parent, ItemLike display) {
            String disc = "item." + display.asItem();
            ItemStack itemStack = display.asItem().getDefaultInstance();
            MutableComponent desc = Component.translatable(PRE + disc + ".desc");
//            List<Component> descList = new ArrayList<>();
//            itemStack.getItem().appendHoverText(itemStack, null, descList, TooltipFlag.NORMAL);
//            for(Component component : descList){
//                desc.append("\n").append(component);
//            }
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(display,
                            display.asItem().getName(itemStack),
                            desc,
                            null,
                            FrameType.GOAL, true, true, false)
                    .addCriterion(disc, InventoryChangeTrigger.TriggerInstance.hasItems(itemStack.getItem()))
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, disc), helper);
        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden) {
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(display,
                            Component.translatable(PRE + name),
                            Component.translatable(PRE + name + ".desc"),
                            null,
                            type, showToast, announceToChat, hidden)
                    .addCriterion(name, new ImpossibleTrigger.TriggerInstance())
                    .save(consumer, new ResourceLocation(SkilletManCoreMod.MOD_ID, name), helper);
        }

        public Advancement registerAdvancement(Advancement parent, String name, FrameType type, ItemLike display, boolean showToast, boolean announceToChat, boolean hidden, CriterionTriggerInstance instance) {
            return Advancement.Builder.advancement()
                    .parent(parent)
                    .display(display,
                            Component.translatable(PRE + name),
                            Component.translatable(PRE + name + ".desc"),
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
