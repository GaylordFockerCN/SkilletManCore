package com.p1nero.smc.capability;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.client.sound.player.RaidMusicPlayer;
import com.p1nero.smc.client.sound.player.WorkingMusicPlayer;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.CustomColorItemEntity;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.SyncSMCPlayerPacket;
import com.p1nero.smc.util.ItemUtil;
import com.p1nero.smc.util.gacha.ArmorGachaSystem;
import com.p1nero.smc.util.gacha.SkillBookGachaSystem;
import com.p1nero.smc.util.gacha.WeaponGachaSystem;
import com.yungnickyoung.minecraft.betterendisland.mixin.ServerLevelMixin;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.item.EpicFightItems;
import yesman.epicfight.world.item.SkillBookItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 记录飞行和技能使用的状态，被坑了，这玩意儿也分服务端和客户端...
 * 懒得换成DataKey了将就一下吧
 */
public class SMCPlayer {

    private int armorGachaingCount;
    private int armorPity4Star;
    private int armorPity5Star;

    public void setArmorGachaingCount(int armorGachaingCount) {
        this.armorGachaingCount = armorGachaingCount;
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

    public void setSkillBookGachaingCount(int skillBookGachaingCount) {
        this.skillBookGachaingCount = skillBookGachaingCount;
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

    public void setWeaponGachaingCount(int weaponGachaingCount) {
        this.weaponGachaingCount = weaponGachaingCount;
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

    private int specialCustomerMet;
    private boolean todayInRaid;
    private int morality = 0;
    private int level;
    private int stage;
    private int moneyCount;
    private boolean isWorking;
    private boolean isSpecialAlive;
    private int consumerLeft;
    private int levelUpLeft = 0;
    private int dodgeCounter = 0;
    private int parryCounter = 0;
    public static final int STAGE1_REQUIRE = 5;
    public static final int STAGE2_REQUIRE = 10;
    public static final int STAGE3_REQUIRE = 20;
    public static final List<PlateFood> STAGE0_FOOD_LIST = List.of(PlateFood.VEGETABLE_FRIED_RICE, PlateFood.FRIED_RICE, PlateFood.FRIED_PASTA, PlateFood.FRIED_MUSHROOM, PlateFood.VEGETABLE_PLATTER);
    public static final List<PlateFood> MEAT_AND_MIX = List.of(PlateFood.VEGETABLE_FRIED_RICE, PlateFood.VEGETABLE_PASTA, PlateFood.SCRAMBLED_EGG_AND_TOMATO,
            PlateFood.MEAT_WITH_VEGETABLES, PlateFood.FRIED_MEAT_AND_MELON, PlateFood.HAM_FRIED_RICE, PlateFood.MEAT_FRIED_RICE,
            PlateFood.MEAT_PASTA, PlateFood.MEAT_PLATTER);
    public static final List<PlateFood> STAGE1_FOOD_LIST = new ArrayList<>();
    public static final List<PlateFood> SEAFOOD_LIST = List.of(PlateFood.MEAT_WITH_SEAFOOD, PlateFood.SEAFOOD_FRIED_RICE, PlateFood.SEAFOOD_PASTA, PlateFood.SEAFOOD_PLATTER,
            PlateFood.SEAFOOD_WITH_VEGETABLES, PlateFood.MIXED_FRIED_RICE, PlateFood.MIXED_PASTA);
    public static final List<PlateFood> STAGE2_FOOD_LIST = new ArrayList<>();

    static {
        STAGE1_FOOD_LIST.addAll(STAGE0_FOOD_LIST);
        STAGE1_FOOD_LIST.addAll(MEAT_AND_MIX);
        STAGE1_FOOD_LIST.addAll(MEAT_AND_MIX);//增加概率

        STAGE2_FOOD_LIST.addAll(STAGE1_FOOD_LIST);
        STAGE2_FOOD_LIST.addAll(SEAFOOD_LIST);
        STAGE2_FOOD_LIST.addAll(SEAFOOD_LIST);
        STAGE2_FOOD_LIST.addAll(SEAFOOD_LIST);//增加概率
    }

    public static void addExperience(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.levelUpLeft++;
        if(smcPlayer.levelUpLeft > 1 + smcPlayer.stage) {
            smcPlayer.levelUpLeft = 0;
            levelUPPlayer(serverPlayer);
        } else {
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("level_up_left", 1 + smcPlayer.stage - smcPlayer.levelUpLeft + 1), false);
        }
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
        if(smcPlayer.parryCounter > 10) {
            SMCAdvancementData.finishAdvancement("parry_master", serverPlayer);
        }
        if(smcPlayer.parryCounter > 100){
            SMCAdvancementData.finishAdvancement("parry_master2", serverPlayer);
        }
        if(smcPlayer.parryCounter > 1000){
            SMCAdvancementData.finishAdvancement("parry_master3", serverPlayer);
        }
    }

    public static void addDodgeCount(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.dodgeCounter += 1;
        if(smcPlayer.dodgeCounter > 10) {
            SMCAdvancementData.finishAdvancement("dodge_master", serverPlayer);
        }
        if(smcPlayer.dodgeCounter > 100){
            SMCAdvancementData.finishAdvancement("dodge_master2", serverPlayer);
        }
        if(smcPlayer.dodgeCounter > 1000){
            SMCAdvancementData.finishAdvancement("dodge_master3", serverPlayer);
        }
    }

    public static void levelUPPlayer(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int currentLevel = smcPlayer.level;
        if(smcPlayer.isTrialRequired()) {
            return;
        }
        smcPlayer.setLevel(currentLevel + 1);
        ItemUtil.addItem(serverPlayer, Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), true);
        SMCPlayer.addMoney(200, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("shop_upgrade", smcPlayer.level), false);
        int nextStageLeft = smcPlayer.getNextStageLeft();
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("next_grade_left", nextStageLeft), false);
    }

    public int getNextStageLeft() {
        if(this.level < STAGE1_REQUIRE) {
            return STAGE1_REQUIRE - this.level;
        } else if(this.level < STAGE2_REQUIRE){
            return STAGE2_REQUIRE - this.level;
        } else if(this.level < STAGE3_REQUIRE) {
            return STAGE3_REQUIRE - this.level;
        } else {
            return -1;
        }
    }

    public static void stageUp(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int currentLevel = smcPlayer.level;
        if(currentLevel == STAGE1_REQUIRE || currentLevel == STAGE2_REQUIRE || currentLevel == STAGE3_REQUIRE) {
            smcPlayer.setLevel(currentLevel + 1);

            if(currentLevel == STAGE1_REQUIRE) {
                smcPlayer.unlockStage1(serverPlayer);
            }
            if(currentLevel == STAGE2_REQUIRE) {
                smcPlayer.unlockStage2(serverPlayer);
            }
            if(currentLevel == STAGE3_REQUIRE) {
                smcPlayer.unlockStage3(serverPlayer);
            }
        }
    }

    public static void defendSuccess(ServerPlayer serverPlayer) {
        DataManager.inRaid.put(serverPlayer, false);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int dayTime = (int) (serverPlayer.serverLevel().dayTime() / 24000);

        SMCAdvancementData.finishAdvancement("start_fight", serverPlayer);
        if(dayTime >= 30) {
            SMCAdvancementData.finishAdvancement("raid30d", serverPlayer);
        } else if(dayTime >= 20) {
            SMCAdvancementData.finishAdvancement("raid20d", serverPlayer);
        } else if(dayTime >= 10) {
            SMCAdvancementData.finishAdvancement("raid10d", serverPlayer);
        }
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_success_for_day", dayTime), false);
        addMoney(1600 * (1 + dayTime), serverPlayer);
        SMCPlayer.levelUPPlayer(serverPlayer);
    }

    public static void defendFailed(ServerPlayer serverPlayer) {
        DataManager.inRaid.put(serverPlayer, false);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_loss_tip"), false);
        int dayTime = (int) (serverPlayer.serverLevel().dayTime() / 24000);
        consumeMoney(100 * (1 + dayTime), serverPlayer);
    }

    public void unlockStage1(ServerPlayer serverPlayer) {
        this.stage = 1;
        addMoney(1000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE2_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("meat_available"), false);
        ItemUtil.addItem(serverPlayer, SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get(), 5, true);
        //TODO 解锁和牧师的对话，解锁最终boss战，弹窗引导对话
    }

    public void unlockStage2(ServerPlayer serverPlayer) {
        this.stage = 2;
        addMoney(5000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE3_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_stage2_info"), false);
    }

    public void unlockStage3(ServerPlayer serverPlayer) {
        this.stage = 3;
        addMoney(20000, serverPlayer);
        //TODO 弹对话
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
            if(isWorking && !DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).isEmpty()) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("raid_no_work"), true);
                return;
            }
            smcPlayer.isWorking = isWorking;
            if (isWorking) {
                smcPlayer.setTodayInRaid(false);
                DataManager.inRaid.put(serverPlayer, false);
                if(!DataManager.firstWork.get(serverPlayer)){
                    DataManager.firstWork.put(serverPlayer, true);
                }
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("start_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            } else {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("end_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                if(serverPlayer.serverLevel().isNight()) {
                    smcPlayer.setSpecialAlive(false);
                }
            }
            smcPlayer.syncToClient(serverPlayer);
        }
    }

    public int getMoneyCount() {
        return moneyCount;
    }

    public static void consumeMoney(int moneyCount, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.moneyCount -= moneyCount;
        serverPlayer.displayClientMessage(Component.literal("-" + moneyCount).withStyle(ChatFormatting.BOLD, ChatFormatting.RED), false);
        smcPlayer.syncToClient(serverPlayer);
    }

    public static void addMoney(int count, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.moneyCount += count;
        serverPlayer.displayClientMessage(Component.literal("+" + count).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN), false);
        smcPlayer.syncToClient(serverPlayer);
        if(smcPlayer.moneyCount > 1000) {
            SMCAdvancementData.finishAdvancement("money1000", serverPlayer);
        }
        if(smcPlayer.moneyCount > 1000000){
            SMCAdvancementData.finishAdvancement("money1000000", serverPlayer);
        }
        if(smcPlayer.moneyCount > 1000000000){
            SMCAdvancementData.finishAdvancement("money1000000000", serverPlayer);
        }
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.EARN_MONEY.get(), serverPlayer.getSoundSource(), 0.4F, 1.0F);
    }

    public static boolean hasMoney(ServerPlayer serverPlayer, int moneyNeed) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        return smcPlayer.getMoneyCount() >= moneyNeed;
    }

    public static boolean hasMoney(ServerPlayer serverPlayer, int moneyNeed, boolean playSound) {
        if(!playSound) {
            return hasMoney(serverPlayer, moneyNeed);
        }
        if(hasMoney(serverPlayer, moneyNeed)) {
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
        if(level < 0) {
            return;
        }
        if(this.level < level) {
            this.level = level;
            if(this.level < STAGE1_REQUIRE) {
                setStage(0);
            } else if(this.level < STAGE2_REQUIRE) {
                setStage(1);
            } else if(this.level < STAGE3_REQUIRE) {
                setStage(2);
            }
        } else {
            this.level = level;
            if(this.level > STAGE3_REQUIRE) {
                setStage(3);
            } else if(this.level > STAGE2_REQUIRE) {
                setStage(2);
            } else if(this.level > STAGE1_REQUIRE) {
                setStage(1);
            }
        }
        this.syncToClient(serverPlayer);
    }

    public int getLevel() {
        return level;
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
        tag.putInt("money_count", moneyCount);
        tag.putBoolean("working", isWorking);
        tag.putInt("consumerLeft", consumerLeft);
        tag.put("customDataManager", data);
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
        moneyCount = tag.getInt("money_count");
        consumerLeft = tag.getInt("consumerLeft");
        isWorking = tag.getBoolean("working");
        data = tag.getCompound("customDataManager");
    }

    public void copyFrom(SMCPlayer old) {
        this.data = old.data;
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

    public void tick(Player player) {
        if(player.level().isClientSide){
            if(isWorking) {
                if(!WorkingMusicPlayer.isRecordPlaying()) {
                    WorkingMusicPlayer.playWorkingMusic();
                }
            } else {
                WorkingMusicPlayer.stopMusic();
            }
            if(DataManager.inRaid.get(player)){
                RaidMusicPlayer.playRaidMusic();
            } else {
                RaidMusicPlayer.stopMusic();
            }

        } else {

            if(DataManager.inRaid.get(player) && DummyEntityManager.getDummyEntities(((ServerLevel) player.level())).isEmpty()){
                DataManager.inRaid.put(player, false);
            }

            if (this.currentTalkingEntity != null && this.currentTalkingEntity.isAlive()) {
                this.currentTalkingEntity.getLookControl().setLookAt(player);
                this.currentTalkingEntity.getNavigation().stop();
                if(this.currentTalkingEntity.distanceTo(player) > 8) {
                    this.currentTalkingEntity = null;
                }
            }

            if(player.tickCount % 20 == 0) {
                //1s抽出一个
                if(this.armorGachaingCount > 0) {
                    this.armorGachaingCount--;
                    ItemStack itemStack = ArmorGachaSystem.pull(((ServerPlayer) player));
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    if(ArmorGachaSystem.STAR5_LIST.contains(itemStack)) {
                        entity.setTeamColor(0xfff66d);
                        entity.setGlowingTag(true);
                        playGoldEffect((ServerPlayer) player, itemStack);
                    } else if(ArmorGachaSystem.STAR4_LIST.contains(itemStack)) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect((ServerPlayer) player, itemStack);
                    } else {
                        playCommonEffect((ServerPlayer) player);
                    }
                }
                if(this.weaponGachaingCount > 0) {
                    this.weaponGachaingCount--;
                    ItemStack itemStack = WeaponGachaSystem.pull(((ServerPlayer) player));
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    if(WeaponGachaSystem.STAR5_LIST.stream().anyMatch(itemStackInPool -> itemStackInPool.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xfff66d);
                        entity.setGlowingTag(true);
                        playGoldEffect((ServerPlayer) player, itemStack);
                    } else if(WeaponGachaSystem.STAR4_LIST.stream().anyMatch(itemStack1 -> itemStack1.is(itemStack.getItem()))) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect((ServerPlayer) player, itemStack);
                    } else {
                        playCommonEffect((ServerPlayer) player);
                    }
                }
                if(this.skillBookGachaingCount > 0) {
                    this.skillBookGachaingCount--;
                    ItemStack itemStack = SkillBookGachaSystem.pull(((ServerPlayer) player));
                    CustomColorItemEntity entity = ItemUtil.addItemEntity(player, itemStack);
                    if(SkillBookGachaSystem.RARE_LIST.contains(itemStack)) {
                        entity.setTeamColor(0xc000ff);
                        entity.setGlowingTag(true);
                        playRareEffect((ServerPlayer) player, itemStack);
                    } else {
                        playCommonEffect((ServerPlayer) player);
                    }
                }
            }

        }
    }
    private void playCommonEffect(ServerPlayer player) {
        SMCAdvancementData.finishAdvancement("first_gacha", player);
        if(!DataManager.firstGachaGot.get(player)) {
            DataManager.firstGachaGot.put(player, true);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5F, 2.0F);
        player.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 10, 1.0, 1.0, 1.0, 0.2);
    }
    private void playRareEffect(ServerPlayer player, ItemStack itemStack) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 2.0F, 2.0F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 2.0F, 2.0F);

        Component itemName = itemStack.is(EpicFightItems.SKILLBOOK.get()) ? itemStack.getDisplayName().copy().append(" - ").append(SkillBookItem.getContainSkill(itemStack).getDisplayName()) : itemStack.getDisplayName();
        player.server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.displayClientMessage(player.getName().copy().append(SkilletManCoreMod.getInfo("rare_item_got", itemName)), false));
    }

    private void playGoldEffect(ServerPlayer player, ItemStack itemStack) {
        SMCAdvancementData.finishAdvancement("first_5star_item", player);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 3.0F, 1.0F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 2.0F, 0.5F);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 3.0F, 1.0F);
        player.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
        Component itemName = itemStack.is(EpicFightItems.SKILLBOOK.get()) ? itemStack.getDisplayName().copy().append(" - ").append(SkillBookItem.getContainSkill(itemStack).getDisplayName()) : itemStack.getDisplayName();
        player.server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.displayClientMessage(player.getName().copy().append(SkilletManCoreMod.getInfo("gold_item_got", itemName.copy().withStyle(ChatFormatting.GOLD))), false));
    }

}
