package com.p1nero.smc.capability;

import com.jesz.createdieselgenerators.CDGBlocks;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.client.sound.player.RaidMusicPlayer;
import com.p1nero.smc.client.sound.player.WorkingMusicPlayer;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.CustomColorItemEntity;
import com.p1nero.smc.event.ServerEvents;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.OpenEndScreenPacket;
import com.p1nero.smc.network.packet.clientbound.SyncSMCPlayerPacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.EntityUtil;
import com.p1nero.smc.util.ItemUtil;
import com.p1nero.smc.util.gacha.ArmorGachaSystem;
import com.p1nero.smc.util.gacha.SkillBookGachaSystem;
import com.p1nero.smc.util.gacha.WeaponGachaSystem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.tom.storagemod.Content;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.kenddie.fantasyarmor.item.FAItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import org.jetbrains.annotations.Nullable;
import reascer.wom.main.WeaponsOfMinecraft;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.minimap.XaeroMinimap;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.item.EpicFightItems;
import yesman.epicfight.world.item.SkillBookItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 记录飞行和技能使用的状态，被坑了，这玩意儿也分服务端和客户端...
 * 懒得换成DataKey了将就一下吧
 */
public class SMCPlayer {

    //翻炒QTE计算，给1/3的时间可以翻
    public static final float MAX_SPATULA_TIME = 20;
    private int currentSpatulaIndex;
    private int currentSpatulaIndex0;
    private long lastSpatulaInteractTime;
    private int lastClientCombo;
    private boolean dir;//true 左 false 右

    public float getCurrentSpatulaIndex(float partialTick) {
        return Mth.lerp(partialTick, currentSpatulaIndex0, currentSpatulaIndex);
    }

    public boolean inCorrectStirTime() {
        return currentSpatulaIndex > MAX_SPATULA_TIME / 3 && currentSpatulaIndex < MAX_SPATULA_TIME * 2 / 3;
    }

    public long getLastSpatulaInteractTime() {
        return lastSpatulaInteractTime;
    }

    public void setLastSpatulaInteractTime(long lastSpatulaInteractTime) {
        this.lastSpatulaInteractTime = lastSpatulaInteractTime;
    }

    //=============================================================
    private int armorGachaingCount;
    private int armorPity4Star;
    private int armorPity5Star;

    public void addArmorGachaingCount(int armorGachaingCount) {
        this.armorGachaingCount += armorGachaingCount;
    }

    public int getArmorPity4Star() {
        return armorPity4Star;
    }

    public void setArmorPity4Star(int armorPity4Star) {
        this.armorPity4Star = armorPity4Star;
    }

    public int getArmorPity5Star() {
        return armorPity5Star;
    }

    public void setArmorPity5Star(int armorPity5Star) {
        this.armorPity5Star = armorPity5Star;
    }

    public void incrementAmorPity4Star() {
        armorPity4Star++;
    }

    public void incrementArmorPity5Star() {
        armorPity5Star++;
    }

    public void resetArmorPity4Star() {
        armorPity4Star = 0;
    }

    public void resetArmorPity5Star() {
        armorPity5Star = 0;
    }

    //============================================================================================
    private int skillBookGachaingCount;
    private int skillBookPity;

    public void addSkillBookGachaingCount(int skillBookGachaingCount) {
        this.skillBookGachaingCount += skillBookGachaingCount;
    }

    public int getSkillBookPity() {
        return skillBookPity;
    }

    public void setSkillBookPity(int skillBookPity) {
        this.skillBookPity = skillBookPity;
    }

    public void incrementSkillBookPity4Star() {
        skillBookPity++;
    }

    public void resetSkillBookPity() {
        skillBookPity = 0;
    }

    //============================================================================================
    private int weaponGachaingCount;
    private int weaponPity4Star;
    private int weaponPity5Star;

    public void addWeaponGachaingCount(int weaponGachaingCount) {
        this.weaponGachaingCount += weaponGachaingCount;
    }

    public int getWeaponPity4Star() {
        return weaponPity4Star;
    }

    public void setWeaponPity4Star(int weaponPity4Star) {
        this.weaponPity4Star = weaponPity4Star;
    }

    public int getWeaponPity5Star() {
        return weaponPity5Star;
    }

    public void setWeaponPity5Star(int weaponPity5Star) {
        this.weaponPity5Star = weaponPity5Star;
    }

    public void incrementWeaponPity4Star() {
        weaponPity4Star++;
    }

    public void incrementWeaponPity5Star() {
        weaponPity5Star++;
    }

    public void resetWeaponPity4Star() {
        weaponPity4Star = 0;
    }

    public void resetWeaponPity5Star() {
        weaponPity5Star = 0;
    }
    //============================================================================================

    @Nullable
    private UUID collaboratorUUID = null;

    @Nullable
    public UUID getCollaboratorUUID() {
        return collaboratorUUID;
    }

    @Nullable
    public Player getCollaborator(Player self) {
        UUID uuid = this.getCollaboratorUUID();
        if (uuid != null) {
            return self.level().getPlayerByUUID(uuid);
        }
        return null;
    }

    public void clearCollaborator() {
        this.collaboratorUUID = null;
    }

    public void setCollaborator(ServerPlayer player) {
        this.collaboratorUUID = player.getUUID();
    }

    private int tickAfterBossDieLeft;
    private int specialCustomerMet;
    private boolean todayInRaid;
    private int morality = 0;
    private int level;
    private int stage = 0;
    private int moneyCount;
    private int moneyInSeason;
    private boolean isWorking;
    private boolean isSpecialAlive;
    private int consumerLeft;
    private int levelUpLeft = 0;
    private int dodgeCounter = 0;
    private int parryCounter = 0;
    public static final int STAGE1_REQUIRE = 5;
    public static final int STAGE2_REQUIRE = 12;
    public static final int STAGE3_REQUIRE = 20;
    public static final List<PlateFood> STAGE0_FOOD_LIST = List.of(PlateFood.VEGETABLE_FRIED_RICE, PlateFood.FRIED_RICE, PlateFood.FRIED_PASTA, PlateFood.FRIED_MUSHROOM, PlateFood.VEGETABLE_PLATTER);
    public static final List<PlateFood> MEAT_AND_MIX = List.of(PlateFood.VEGETABLE_FRIED_RICE, PlateFood.VEGETABLE_PASTA, PlateFood.SCRAMBLED_EGG_AND_TOMATO,
            PlateFood.MEAT_WITH_VEGETABLES, PlateFood.FRIED_MEAT_AND_MELON, PlateFood.HAM_FRIED_RICE, PlateFood.MEAT_FRIED_RICE,
            PlateFood.MEAT_PASTA, PlateFood.MEAT_PLATTER);
    public static final List<PlateFood> STAGE1_FOOD_LIST = new ArrayList<>();
    public static final List<PlateFood> SEAFOOD_LIST = List.of(PlateFood.MEAT_WITH_SEAFOOD, PlateFood.SEAFOOD_FRIED_RICE, PlateFood.SEAFOOD_PASTA, PlateFood.SEAFOOD_PLATTER,
            PlateFood.SEAFOOD_WITH_VEGETABLES, PlateFood.MIXED_FRIED_RICE, PlateFood.MIXED_PASTA);
    public static final List<PlateFood> STAGE2_FOOD_LIST = new ArrayList<>();
    public static final List<List<Item>> LEVEL_UP_ITEMS = new ArrayList<>();
    private static List<ItemStack> RANDOM_PROP = null;

    static {
        STAGE1_FOOD_LIST.addAll(STAGE0_FOOD_LIST);
        STAGE1_FOOD_LIST.addAll(MEAT_AND_MIX);
        STAGE1_FOOD_LIST.addAll(MEAT_AND_MIX);//增加概率

        STAGE2_FOOD_LIST.addAll(STAGE1_FOOD_LIST);
        STAGE2_FOOD_LIST.addAll(SEAFOOD_LIST);
        STAGE2_FOOD_LIST.addAll(SEAFOOD_LIST);//增加概率

        LEVEL_UP_ITEMS.add(List.of(SMCRegistrateItems.IRON_SKILLET_LEVEL5.get()));//5
        LEVEL_UP_ITEMS.add(List.of(SMCRegistrateItems.GOLDEN_SPATULA_V5.get()));//10
        LEVEL_UP_ITEMS.add(List.of(SMCRegistrateItems.GOLDEN_SKILLET_V5.get()));//15
        LEVEL_UP_ITEMS.add(List.of(AllItems.POTATO_CANNON.get()));//20
        LEVEL_UP_ITEMS.add(List.of(SMCRegistrateItems.DIAMOND_SKILLET_V4.get()));//25
        LEVEL_UP_ITEMS.add(List.of(SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get(), FAItems.HERO_HELMET.get(), FAItems.HERO_CHESTPLATE.get(), FAItems.HERO_LEGGINGS.get(), FAItems.HERO_BOOTS.get()));//30
    }

    public static void addExperience(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.levelUpLeft++;
        if (smcPlayer.levelUpLeft > 1 + smcPlayer.stage) {
            smcPlayer.levelUpLeft = 0;
            levelUPPlayer(serverPlayer);
        } else {
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("level_up_left", 1 + smcPlayer.stage - smcPlayer.levelUpLeft + 1), false);
        }
    }

    public void setTickAfterBossDieLeft(int tickAfterBossDieLeft) {
        this.tickAfterBossDieLeft = tickAfterBossDieLeft;
    }

    public int getSpecialCustomerMet() {
        return specialCustomerMet;
    }

    public void setSpecialCustomerMet(int specialCustomerMet) {
        this.specialCustomerMet = specialCustomerMet;
    }

    public void increaseSpecialCustomerMet() {
        this.specialCustomerMet++;
    }

    public boolean isTodayInRaid() {
        return todayInRaid;
    }

    public void setTodayInRaid(boolean inRaid) {
        this.todayInRaid = inRaid;
    }

    public int getMorality() {
        return morality;
    }

    public void addMorality() {
        this.morality++;
    }

    public void consumeMorality() {
        this.morality--;
    }

    public int getLevelUpLeft() {
        return levelUpLeft;
    }

    public void setLevelUpLeft(int levelUpLeft) {
        this.levelUpLeft = levelUpLeft;
    }

    public boolean isSpecialAlive() {
        return isSpecialAlive;
    }

    public void setSpecialAlive(boolean specialAlive) {
        isSpecialAlive = specialAlive;
    }

    public int getConsumerLeft() {
        return consumerLeft;
    }

    public void setConsumerLeft(int consumerLeft) {
        this.consumerLeft = consumerLeft;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public static void addParryCount(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.parryCounter += 1;
        if (smcPlayer.parryCounter > 10) {
            SMCAdvancementData.finishAdvancement("parry_master", serverPlayer);
        }
        if (smcPlayer.parryCounter > 100) {
            SMCAdvancementData.finishAdvancement("parry_master2", serverPlayer);
        }
        if (smcPlayer.parryCounter > 1000) {
            SMCAdvancementData.finishAdvancement("parry_master3", serverPlayer);
        }
    }

    public static void addDodgeCount(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.dodgeCounter += 1;
        if (smcPlayer.dodgeCounter > 10) {
            SMCAdvancementData.finishAdvancement("dodge_master", serverPlayer);
        }
        if (smcPlayer.dodgeCounter > 100) {
            SMCAdvancementData.finishAdvancement("dodge_master2", serverPlayer);
        }
        if (smcPlayer.dodgeCounter > 1000) {
            SMCAdvancementData.finishAdvancement("dodge_master3", serverPlayer);
        }
    }

    public static void levelUPPlayer(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int currentLevel = smcPlayer.level;
        if (smcPlayer.isTrialRequired()) {
            return;
        }
        smcPlayer.setLevel(currentLevel + 1);
        if (smcPlayer.level % 5 == 0) {
            SMCAdvancementData.finishAdvancement("level" + smcPlayer.level, serverPlayer);
            int index = smcPlayer.level / 5 - 1;
            if (index >= 0 && index < LEVEL_UP_ITEMS.size()) {
                for (Item item : LEVEL_UP_ITEMS.get(index)) {
                    ItemUtil.addItem(serverPlayer, item.getDefaultInstance(), true);
                }
            }
        }

        if (smcPlayer.isTrialRequired()) {
            DataManager.trailRequired.put(serverPlayer, true);
            DataManager.hintUpdated.put(serverPlayer, true);
        }

        ItemUtil.addItem(serverPlayer, Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), true);
        smcPlayer.getRandomProp(serverPlayer);
        SMCPlayer.addMoney(200, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("shop_upgrade", smcPlayer.level), false);
        int nextStageLeft = smcPlayer.getNextStageLeft();
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("next_grade_left", nextStageLeft), false);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void getRandomProp(ServerPlayer serverPlayer) {
        if (RANDOM_PROP == null) {
            ItemStack honeyHoney = Items.HONEY_BOTTLE.getDefaultInstance();
            honeyHoney.getOrCreateTag().putBoolean(SkilletManCoreMod.MUL, true);
            honeyHoney.setHoverName(SkilletManCoreMod.getInfo("honey_custom_name"));
            RANDOM_PROP = List.of(honeyHoney, SMCRegistrateItems.BAD_CAT.asStack(), SMCRegistrateItems.LUCKY_CAT.asStack(), SMCRegistrateItems.SUPER_CHEF_PILL.asStack(),
                    SMCRegistrateItems.RUMOR_ITEM.asItem().getDefaultInstance(), SMCRegistrateItems.GUO_CHAO.asStack(5), SMCRegistrateItems.PI_SHUANG.asStack());
        }
        ItemStack itemStack = RANDOM_PROP.get(serverPlayer.getRandom().nextInt(RANDOM_PROP.size()));
        ItemStack toAdd = itemStack.copy();
        ItemUtil.addItem(serverPlayer, toAdd, true);
    }

    public int getNextStageLeft() {
        if (this.level < STAGE1_REQUIRE) {
            return STAGE1_REQUIRE - this.level;
        } else if (this.level < STAGE2_REQUIRE) {
            return STAGE2_REQUIRE - this.level;
        } else if (this.level < STAGE3_REQUIRE) {
            return STAGE3_REQUIRE - this.level;
        } else {
            return -1;
        }
    }

    public static void stageUp(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        DataManager.trailRequired.put(serverPlayer, false);
        int currentLevel = smcPlayer.level;
        if (currentLevel == STAGE1_REQUIRE || currentLevel == STAGE2_REQUIRE || currentLevel == STAGE3_REQUIRE) {
            smcPlayer.setLevel(currentLevel + 1);

            if (currentLevel == STAGE1_REQUIRE) {
                smcPlayer.unlockStage1(serverPlayer);
            }
            if (currentLevel == STAGE2_REQUIRE) {
                smcPlayer.unlockStage2(serverPlayer);
            }
            if (currentLevel == STAGE3_REQUIRE) {
                smcPlayer.unlockStage3(serverPlayer);
            }
        }
    }

    public static void defendSuccess(ServerPlayer serverPlayer) {
        DataManager.inRaid.put(serverPlayer, false);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int dayTime = (int) (serverPlayer.serverLevel().dayTime() / 24000);

        SMCAdvancementData.finishAdvancement("start_fight", serverPlayer);
        if (dayTime >= 30) {
            SMCAdvancementData.finishAdvancement("raid30d", serverPlayer);
        } else if (dayTime >= 15) {
            SMCAdvancementData.finishAdvancement("raid15d", serverPlayer);
        } else if (dayTime >= 5) {
            SMCAdvancementData.finishAdvancement("raid5d", serverPlayer);
        }
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_success_for_day", dayTime), false);
        addMoney(1600 * (1 + dayTime), serverPlayer);
        SMCPlayer.addExperience(serverPlayer);
        tryBroadCastRank(serverPlayer);
        summonVillagers(serverPlayer);
        //送随机附魔书
        ItemStack book = Items.BOOK.getDefaultInstance();
        ItemStack enchantedBook = EnchantmentHelper.enchantItem(serverPlayer.getRandom(), book, 30 + serverPlayer.getRandom().nextInt(25), false);
        CustomColorItemEntity entity = ItemUtil.addItemEntity(serverPlayer, enchantedBook);
        entity.setTeamColor(0xfff66d);
        entity.setGlowingTag(true);
        entity.setDefaultPickUpDelay();
        playRareEffect(serverPlayer, enchantedBook);
    }

    public static void summonVillagers(ServerPlayer serverPlayer) {
        if (DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).size() <= 1) {
            List<Villager> villagers = EntityUtil.getNearByEntities(Villager.class, serverPlayer, 64);
            if (villagers.size() < 30) {
                for (int i = 0; i < 3; i++) {
                    EntityType.VILLAGER.spawn(serverPlayer.serverLevel(), serverPlayer.getOnPos(), MobSpawnType.MOB_SUMMONED);
                }
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("defend_success_add_villagers"), false);
            }
        }
    }

    public static void defendFailed(ServerPlayer serverPlayer) {
        DataManager.inRaid.put(serverPlayer, false);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_loss_tip"), false);
        int dayTime = (int) (serverPlayer.serverLevel().dayTime() / 24000);
        consumeMoney(800 * (1 + dayTime), serverPlayer);
        tryBroadCastRank(serverPlayer);
    }

    /**
     * 袭击完全结束后再播报
     */
    public static void tryBroadCastRank(ServerPlayer serverPlayer) {
        if (DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).size() <= 1) {
            ServerEvents.displayRankingListFor(serverPlayer);
        }
    }

    public void unlockStage1(ServerPlayer serverPlayer) {
        this.stage = 1;
        addMoney(1000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE2_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("meat_available"), false);
        ItemUtil.addItem(serverPlayer, SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get(), 5, true);
        ItemUtil.addItem(serverPlayer, ModItems.FEEDING_UPGRADE.get().getDefaultInstance(), true);
        ItemUtil.addItem(serverPlayer, Content.terminal.get().asItem().getDefaultInstance(), true);
        ItemUtil.addItem(serverPlayer, Content.connector.get().asItem().getDefaultInstance(), true);
        ItemUtil.addItem(serverPlayer, Content.advWirelessTerminal.get().asItem().getDefaultInstance(), true);
        DataManager.showFirstPlaceWirelessTerminal.put(serverPlayer, true);
        DataManager.hintUpdated.put(serverPlayer, true);
        SMCAdvancementData.finishAdvancement("level5_1", serverPlayer);
        SMCAdvancementData.finishAdvancement("level5_2", serverPlayer);
        SMCAdvancementData.finishAdvancement("level5_3", serverPlayer);
    }

    public void unlockStage2(ServerPlayer serverPlayer) {
        this.stage = 2;
        addMoney(5000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE3_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_stage2_info"), false);

        ItemUtil.addItem(serverPlayer, ModItems.ADVANCED_FEEDING_UPGRADE.get().getDefaultInstance(), true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.asStack(1), true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.asStack(1), true);
        ItemUtil.addItem(serverPlayer, AllBlocks.BASIN.asStack(), true);
        ItemUtil.addItem(serverPlayer, CDGBlocks.BASIN_LID.asStack(), true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.CREATE_RAFFLE.asStack(5), true);
        SMCAdvancementData.finishAdvancement("level10_1", serverPlayer);
        SMCAdvancementData.finishAdvancement("level10_2", serverPlayer);
    }

    public void unlockStage3(ServerPlayer serverPlayer) {
        this.stage = 3;
        addMoney(20000, serverPlayer);

        //屠龙套装
        for (Item item : List.of(FAItems.DRAGONSLAYER_HELMET.get(), FAItems.DRAGONSLAYER_CHESTPLATE.get(), FAItems.DRAGONSLAYER_LEGGINGS.get(), FAItems.DRAGONSLAYER_BOOTS.get())) {
            ItemUtil.addItem(serverPlayer, item.getDefaultInstance(), true);
        }
        SMCAdvancementData.finishAdvancement("level20_1", serverPlayer);
        SMCAdvancementData.finishAdvancement("level20_2", serverPlayer);
    }

    public List<PlateFood> getOrderList() {
        return switch (this.getStage()) {
            case 0 -> STAGE0_FOOD_LIST;
            case 1 -> STAGE1_FOOD_LIST;
            default -> STAGE2_FOOD_LIST;
        };
    }

    public static void updateWorkingState(boolean isWorking, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (smcPlayer.isWorking != isWorking) {
            if (isWorking && !DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).isEmpty()) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_no_work"), true);
                return;
            }
            smcPlayer.isWorking = isWorking;
            if (isWorking) {
                smcPlayer.setTodayInRaid(false);
                DataManager.inRaid.put(serverPlayer, false);
                if (!DataManager.firstWork.get(serverPlayer)) {
                    DataManager.firstWork.put(serverPlayer, true);
                    DataManager.hintUpdated.put(serverPlayer, true);
                }
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("start_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            } else {
                if (!DataManager.firstStopWork.get(serverPlayer)) {
                    DataManager.firstStopWork.put(serverPlayer, true);
                    DataManager.hintUpdated.put(serverPlayer, true);
                }
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("end_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                if (serverPlayer.serverLevel().isNight()) {
                    smcPlayer.setSpecialAlive(false);
                }
            }
            smcPlayer.syncToClient(serverPlayer);
        }
        Player collaborator = smcPlayer.getCollaborator(serverPlayer);
        if(collaborator instanceof ServerPlayer spCollaborator) {
            SMCPlayer smcCollaborator = SMCCapabilityProvider.getSMCPlayer(collaborator);
            if(smcCollaborator.isWorking != isWorking) {
                updateWorkingState(isWorking, spCollaborator);
            }
        }
    }

    public int getMoneyCount() {
        return moneyCount;
    }

    public int getMoneyInSeason() {
        return moneyInSeason;
    }

    public static void consumeMoney(double moneyCount, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.moneyCount -= (int) moneyCount;
        if (smcPlayer.moneyCount < 0) {
            SMCAdvancementData.finishAdvancement("no_money", serverPlayer);
        }
        serverPlayer.displayClientMessage(Component.literal("-" + (int) moneyCount).withStyle(ChatFormatting.BOLD, ChatFormatting.RED), false);
        smcPlayer.syncToClient(serverPlayer);
    }

    public static void addMoney(int count, ServerPlayer serverPlayer) {
        addMoney(count, serverPlayer, false);
    }

    public static void addMoney(int count, ServerPlayer serverPlayer, boolean ignoredCollaborator) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (!ignoredCollaborator && smcPlayer.getCollaborator(serverPlayer) instanceof ServerPlayer collaborator) {
            collaborator.displayClientMessage(SkilletManCoreMod.getInfo("from_collaborator"), false);
            addMoney(count / 2, collaborator, true);
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("from_collaborator"), false);
            addMoney(count / 2, serverPlayer, true);
            return;
        }
        smcPlayer.moneyCount += count;
        smcPlayer.moneyInSeason += count;
        serverPlayer.displayClientMessage(Component.literal("+" + count).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN), false);
        smcPlayer.syncToClient(serverPlayer);
        if (smcPlayer.moneyCount > 10000) {
            SMCAdvancementData.finishAdvancement("money10000", serverPlayer);
        }
        if (smcPlayer.moneyCount > 100000) {
            SMCAdvancementData.finishAdvancement("money100000", serverPlayer);
        }
        if (smcPlayer.moneyCount > 1000000) {
            SMCAdvancementData.finishAdvancement("money1000000", serverPlayer);
        }
        if (smcPlayer.moneyCount > 1000000000) {
            SMCAdvancementData.finishAdvancement("money1000000000", serverPlayer);
        }
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.EARN_MONEY.get(), serverPlayer.getSoundSource(), 0.4F, 1.0F);
    }

    public static boolean hasMoney(ServerPlayer serverPlayer, int moneyNeed) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        return smcPlayer.getMoneyCount() >= moneyNeed;
    }

    public static boolean hasMoney(ServerPlayer serverPlayer, int moneyNeed, boolean playSound) {
        if (!playSound) {
            return hasMoney(serverPlayer, moneyNeed);
        }
        if (hasMoney(serverPlayer, moneyNeed)) {
            serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            return true;
        }
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_NO, serverPlayer.getSoundSource(), 1.0F, 1.0F);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
        return false;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelSync(int level, ServerPlayer serverPlayer) {
        if (level < 0) {
            return;
        }
        if (this.level < level) {
            this.level = level;
            if (this.level < STAGE1_REQUIRE) {
                setStage(0);
            } else if (this.level < STAGE2_REQUIRE) {
                setStage(1);
            } else if (this.level < STAGE3_REQUIRE) {
                setStage(2);
            }
        } else {
            this.level = level;
            if (this.level > STAGE3_REQUIRE) {
                setStage(3);
            } else if (this.level > STAGE2_REQUIRE) {
                setStage(2);
            } else if (this.level > STAGE1_REQUIRE) {
                setStage(1);
            }
        }
        this.syncToClient(serverPlayer);
    }

    public int getLevel() {
        return level;
    }

    public double getLevelMoneyRate() {
        return 1 + 0.3 * level;
    }

    public boolean isTrialRequired() {
        return level == STAGE1_REQUIRE || level == STAGE2_REQUIRE || level == STAGE3_REQUIRE;
    }

    @Nullable
    private PathfinderMob currentTalkingEntity;

    public void setCurrentTalkingEntity(@Nullable PathfinderMob currentTalkingEntity) {
        this.currentTalkingEntity = currentTalkingEntity;
    }

    public @Nullable PathfinderMob getCurrentTalkingEntity() {
        return currentTalkingEntity;
    }

    private CompoundTag data = new CompoundTag();

    public boolean getBoolean(String key) {
        return data.getBoolean(key);
    }

    public double getDouble(String key) {
        return data.getDouble(key);
    }

    public String getString(String key) {
        return data.getString(key);
    }

    public void putBoolean(String key, boolean value) {
        data.putBoolean(key, value);
    }

    public void putDouble(String key, double v) {
        data.putDouble(key, v);
    }

    public void putString(String k, String v) {
        data.putString(k, v);
    }

    public void setData(Consumer<CompoundTag> consumer) {
        consumer.accept(data);
    }

    public CompoundTag getData() {
        return data;
    }

    public CompoundTag saveNBTData(CompoundTag tag) {
        tag.putInt("specialCustomerMet", specialCustomerMet);

        tag.putBoolean("inRaid", todayInRaid);

        tag.putInt("morality", morality);
        tag.putInt("armorGachaingCount", armorGachaingCount);
        tag.putInt("armorPity4Star", armorPity4Star);
        tag.putInt("armorPity5Star", armorPity5Star);

        tag.putInt("skillBookGachaingCount", skillBookGachaingCount);
        tag.putInt("skillBookPity", skillBookPity);

        tag.putInt("weaponGachaingCount", weaponGachaingCount);
        tag.putInt("weaponPity4Star", weaponPity4Star);
        tag.putInt("weaponPity5Star", weaponPity5Star);

        tag.putInt("dodgeCnt", dodgeCounter);
        tag.putInt("parryCnt", parryCounter);
        tag.putInt("levelUpLeft", levelUpLeft);
        tag.putInt("tradeLevel", level);
        tag.putInt("tradeStage", stage);
        tag.putInt("moneyInSeason", moneyInSeason);
        tag.putInt("money_count", moneyCount);
        tag.putBoolean("working", isWorking);
        tag.putInt("consumerLeft", consumerLeft);
        tag.put("customDataManager", data);
        if (collaboratorUUID != null) {
            tag.putUUID("collaboratorUUID", collaboratorUUID);
        }
        return tag;
    }

    public void loadNBTData(CompoundTag tag) {
        specialCustomerMet = tag.getInt("specialCustomerMet");
        todayInRaid = tag.getBoolean("inRaid");

        morality = tag.getInt("morality");

        armorGachaingCount = tag.getInt("armorGachaingCount");
        armorPity4Star = tag.getInt("armorPity4Star");
        armorPity5Star = tag.getInt("armorPity5Star");

        skillBookGachaingCount = tag.getInt("skillBookGachaingCount");
        skillBookPity = tag.getInt("skillBookPity");

        weaponGachaingCount = tag.getInt("weaponGachaingCount");
        weaponPity4Star = tag.getInt("weaponPity4Star");
        weaponPity5Star = tag.getInt("weaponPity5Star");

        dodgeCounter = tag.getInt("dodgeCnt");
        parryCounter = tag.getInt("parryCnt");
        level = tag.getInt("tradeLevel");
        levelUpLeft = tag.getInt("levelUpLeft");
        stage = tag.getInt("tradeStage");
        moneyInSeason = tag.getInt("moneyInSeason");
        moneyCount = tag.getInt("money_count");
        consumerLeft = tag.getInt("consumerLeft");
        isWorking = tag.getBoolean("working");
        data = tag.getCompound("customDataManager");
        if (tag.contains("collaboratorUUID")) {
            collaboratorUUID = tag.getUUID("collaboratorUUID");
        }
    }

    public void copyFrom(SMCPlayer old) {
        this.data = old.data;

        this.collaboratorUUID = old.collaboratorUUID;

        this.moneyInSeason = old.moneyInSeason;
        this.tickAfterBossDieLeft = old.tickAfterBossDieLeft;
        this.specialCustomerMet = old.specialCustomerMet;

        this.todayInRaid = old.todayInRaid;

        this.morality = old.morality;

        this.skillBookGachaingCount = old.skillBookGachaingCount;
        this.skillBookPity = old.skillBookPity;

        this.armorGachaingCount = old.armorGachaingCount;
        this.armorPity4Star = old.armorPity4Star;
        this.armorPity5Star = old.armorPity5Star;

        this.weaponGachaingCount = old.weaponGachaingCount;
        this.weaponPity5Star = old.weaponPity5Star;
        this.weaponPity4Star = old.weaponPity4Star;

        this.dodgeCounter = old.dodgeCounter;
        this.parryCounter = old.parryCounter;
        this.levelUpLeft = old.levelUpLeft;
        this.level = old.level;
        this.stage = old.stage;
        this.moneyCount = old.moneyCount;
        this.consumerLeft = old.consumerLeft;
        this.isWorking = old.isWorking;
        this.isSpecialAlive = old.isSpecialAlive;//复制就好，不持久化
    }

    public void syncToClient(ServerPlayer serverPlayer) {
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncSMCPlayerPacket(saveNBTData(new CompoundTag())), serverPlayer);
    }

    @SuppressWarnings("deprecation")
    public void tick(Player player) {

        int dayTime = (int) (player.level().getDayTime() / 24000);
        int dayTick = (int) (player.level().getDayTime() % 24000);


        //控制铲子位置
        if (player.getMainHandItem().getItem() instanceof SpatulaItem) {
            currentSpatulaIndex0 = currentSpatulaIndex;
            int time = dayTick % ((int) MAX_SPATULA_TIME + 1);
            if (dir) {
                currentSpatulaIndex = time;
            } else {
                currentSpatulaIndex = (int) (MAX_SPATULA_TIME - time);
            }
            if (currentSpatulaIndex == MAX_SPATULA_TIME) {
                dir = false;
            }
            if (currentSpatulaIndex == 0) {
                dir = true;
            }
        }

        if (player.level().isClientSide) {
            if (player.isLocalPlayer()) {
                int currentCombo = DataManager.spatulaCombo.get(player).intValue();
                if (currentCombo != lastClientCombo) {
                    lastClientCombo = currentCombo;
                    if (currentCombo == 0) {
                        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_NO, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    } else {
                        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        if (currentCombo % 10 == 0) {
                            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SMCSounds.VILLAGER_YES.get(), SoundSource.BLOCKS, currentCombo / 20.0F + 0.5F, 1.0F, false);
                        }
                    }
                }
                if (isWorking) {
                    if (!WorkingMusicPlayer.isRecordPlaying()) {
                        WorkingMusicPlayer.playWorkingMusic();
                    }
                } else {
                    WorkingMusicPlayer.stopMusic();
                }
                if (DataManager.inRaid.get(player)) {
                    RaidMusicPlayer.playRaidMusic();
                } else {
                    RaidMusicPlayer.stopMusic();
                }

                //土豆加农炮逻辑
                if (player.getMainHandItem().is(AllItems.POTATO_CANNON.get())) {
                    if (!player.getCooldowns().isOnCooldown(AllItems.POTATO_CANNON.get())) {
                        ClientEngine.getInstance().renderEngine.zoomIn();
                    } else {
                        ClientEngine.getInstance().renderEngine.zoomOut(40);
                    }
                }

                //移除过远的无效路标点
                IXaeroMinimapClientPlayNetHandler clientLevel = (IXaeroMinimapClientPlayNetHandler) ((LocalPlayer) player).connection;
                XaeroMinimapSession session = clientLevel.getXaero_minimapSession();
                WaypointsManager waypointsManager = session.getWaypointsManager();
                String name = SkilletManCoreMod.getInfoKey("no_owner_shop");
                if (waypointsManager.getWaypoints() != null) {
                    List<Waypoint> list = waypointsManager.getWaypoints().getList();
                    int beforeSize = list.size();
                    list.removeIf((waypoint -> {
                        Vec3 waypointPos = new Vec3(waypoint.getX(), waypoint.getY(), waypoint.getZ());
                        return waypoint.getName().equals(name) && player.position().distanceTo(waypointPos) > 200;
                    }));
                    if (list.size() != beforeSize) {
                        try {
                            XaeroMinimap.instance.getSettings().saveWaypoints(waypointsManager.getCurrentWorld());
                        } catch (IOException ignored) {
                        }
                    }
                }
            }

        } else {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel serverLevel = serverPlayer.serverLevel();
            if (serverPlayer.isInvulnerable() && !serverPlayer.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                serverPlayer.setInvulnerable(false);
            }
            //显示当天节气
            if (dayTick == 100) {
                SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(serverPlayer.serverLevel());
                serverPlayer.connection.send(new ClientboundSetTitleTextPacket(solarTerm.getTranslation().withStyle(solarTerm.getSeason().getColor())));
                if (dayTime % 7 == 0) {
                    moneyInSeason = 0;
                }
            }

            //Boss战后的返回倒计时
            if (tickAfterBossDieLeft > 0) {
                tickAfterBossDieLeft--;
                if (tickAfterBossDieLeft % 40 == 0) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.8F, 0.5F + tickAfterBossDieLeft / 400.0F);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 0.8F, 0.5F + tickAfterBossDieLeft / 400.0F);
                }
                player.displayClientMessage(SkilletManCoreMod.getInfo("second_after_boss_die_left", tickAfterBossDieLeft / 20).withStyle(ChatFormatting.BOLD, ChatFormatting.RED), true);
                if (tickAfterBossDieLeft == 0) {
                    PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenEndScreenPacket(), serverPlayer);//终末之诗
                }
            }

            //重置袭击状态
            if (DataManager.inRaid.get(player) && DummyEntityManager.getDummyEntities(serverLevel).isEmpty()) {
                DataManager.inRaid.put(player, false);
            }

            //控制当前对话实体看自己
            if (this.currentTalkingEntity != null && this.currentTalkingEntity.isAlive()) {
                this.currentTalkingEntity.getLookControl().setLookAt(player);
                this.currentTalkingEntity.getNavigation().stop();
                if (this.currentTalkingEntity.distanceTo(player) > 8) {
                    this.currentTalkingEntity = null;
                }
            }

            //抽卡系统
            if (player.tickCount % 20 == 0) {
                //1s抽出一个
                if (this.armorGachaingCount > 0) {
                    this.armorGachaingCount--;
                    ItemStack itemStack = ArmorGachaSystem.pull(serverPlayer);
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    if (ArmorGachaSystem.STAR5_LIST.stream().anyMatch(itemStackInPool -> itemStackInPool.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xfff66d);
                        entity.setGlowingTag(true);
                        playGoldEffect(serverPlayer, itemStack);
                    } else if (ArmorGachaSystem.STAR4_LIST.stream().anyMatch(itemStackInPool -> itemStackInPool.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect((ServerPlayer) player, itemStack);
                    } else {
                        playCommonEffect((ServerPlayer) player);
                    }
                }
                if (this.weaponGachaingCount > 0) {
                    this.weaponGachaingCount--;
                    ItemStack itemStack = WeaponGachaSystem.pull(serverPlayer);
                    if (itemStack.is(Items.BOW)) {
                        EnchantmentHelper.enchantItem(serverPlayer.getRandom(), itemStack, 30 + serverPlayer.getRandom().nextInt(25), false);
                    }
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    if (WeaponGachaSystem.STAR5_LIST.stream().anyMatch(itemStackInPool -> itemStackInPool.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xfff66d);
                        entity.setGlowingTag(true);
                        playGoldEffect(serverPlayer, itemStack);
                    } else if (WeaponGachaSystem.STAR4_LIST.stream().anyMatch(itemStack1 -> itemStack1.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect(serverPlayer, itemStack);
                    } else {
                        playCommonEffect(serverPlayer);
                    }
                }
                if (this.skillBookGachaingCount > 0) {
                    this.skillBookGachaingCount--;
                    ItemStack itemStack = SkillBookGachaSystem.pull((serverPlayer));
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    Skill skill = SkillBookItem.getContainSkill(itemStack);
                    if (skill != null && skill.getRegistryName().getNamespace().equals(WeaponsOfMinecraft.MODID)) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect(serverPlayer, itemStack);
                    } else {
                        playCommonEffect(serverPlayer);
                    }
                }
            }

        }
    }

    private void broadcastCommonGot(ServerPlayer player, ItemStack itemStack) {
        Component itemName = itemStack.is(EpicFightItems.SKILLBOOK.get()) ? itemStack.getDisplayName().copy().append(" - ").append(SkillBookItem.getContainSkill(itemStack).getDisplayName()) : itemStack.getDisplayName();
        player.server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.displayClientMessage(player.getName().copy().append(SkilletManCoreMod.getInfo("common_item_got", itemName)), false));
    }

    private static void playCommonEffect(ServerPlayer player) {
        SMCAdvancementData.finishAdvancement("first_gacha", player);
        if (!DataManager.firstGachaGot.get(player)) {
            DataManager.firstGachaGot.put(player, true);
            DataManager.hintUpdated.put(player, true);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5F, 2.0F);
        player.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 10, 1.0, 1.0, 1.0, 0.2);
    }

    private static void playRareEffect(ServerPlayer player, ItemStack itemStack) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 2.0F, 2.0F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 2.0F, 2.0F);

        Component itemName = itemStack.is(EpicFightItems.SKILLBOOK.get()) ? itemStack.getDisplayName().copy().append(" - ").append(SkillBookItem.getContainSkill(itemStack).getDisplayName()) : itemStack.getDisplayName();
        player.server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.displayClientMessage(player.getName().copy().append(SkilletManCoreMod.getInfo("rare_item_got", itemName)), false));
    }

    private static void playGoldEffect(ServerPlayer player, ItemStack itemStack) {
        SMCAdvancementData.finishAdvancement("first_5star_item", player);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 3.0F, 1.0F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 2.0F, 0.5F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 3.0F, 1.0F);
        player.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
        Component itemName = itemStack.is(EpicFightItems.SKILLBOOK.get()) ? itemStack.getDisplayName().copy().append(" - ").append(SkillBookItem.getContainSkill(itemStack).getDisplayName()) : itemStack.getDisplayName();
        player.server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.displayClientMessage(player.getName().copy().append(SkilletManCoreMod.getInfo("gold_item_got", itemName.copy().withStyle(ChatFormatting.GOLD))), false));
    }

}
