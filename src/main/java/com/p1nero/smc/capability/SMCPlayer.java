package com.p1nero.smc.capability;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.client.sound.player.WorkingMusicPlayer;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.SyncSMCPlayerPacket;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 记录飞行和技能使用的状态，被坑了，这玩意儿也分服务端和客户端...
 * 懒得换成DataKey了将就一下吧
 */
public class SMCPlayer {
    private int level;
    private int stage;
    private int moneyCount;
    private boolean isWorking;
    private boolean isSpecialAlive;
    private int consumerLeft;
    private int levelUpLeft = 0;
    public static final int STAGE1_REQUIRE = 10;
    public static final int STAGE2_REQUIRE = 20;
    public static final int STAGE3_REQUIRE = 50;
    public static final List<PlateFood> STAGE0_FOOD_LIST = List.of(PlateFood.FRIED_RICE, PlateFood.FRIED_PASTA, PlateFood.FRIED_MUSHROOM,
            PlateFood.VEGETABLE_FRIED_RICE, PlateFood.VEGETABLE_PASTA, PlateFood.VEGETABLE_PLATTER);
    public static final List<PlateFood> STAGE1_FOOD_LIST = new ArrayList<>();
    public static final List<PlateFood> STAGE2_FOOD_LIST = new ArrayList<>();

    static {
        STAGE1_FOOD_LIST.addAll(STAGE0_FOOD_LIST);
        STAGE1_FOOD_LIST.addAll(List.of(PlateFood.SCRAMBLED_EGG_AND_TOMATO, PlateFood.MEAT_WITH_VEGETABLES, PlateFood.FRIED_MEAT_AND_MELON,
                PlateFood.HAM_FRIED_RICE, PlateFood.MEAT_FRIED_RICE, PlateFood.MEAT_PASTA, PlateFood.MEAT_PLATTER));
        STAGE2_FOOD_LIST.addAll(STAGE1_FOOD_LIST);
        STAGE2_FOOD_LIST.addAll(
                List.of(PlateFood.MEAT_WITH_SEAFOOD, PlateFood.SEAFOOD_FRIED_RICE, PlateFood.SEAFOOD_PASTA,
                        PlateFood.SEAFOOD_PLATTER, PlateFood.SEAFOOD_WITH_VEGETABLES, PlateFood.MIXED_FRIED_RICE, PlateFood.MIXED_PASTA));
    }

    public static void finishTransaction(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.levelUpLeft++;
        if(smcPlayer.levelUpLeft > smcPlayer.stage) {
            smcPlayer.levelUpLeft = 0;
            levelUPPlayer(serverPlayer);
        }
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

    public static void levelUPPlayer(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int currentLevel = smcPlayer.level;
        if(currentLevel == STAGE1_REQUIRE || currentLevel == STAGE2_REQUIRE || currentLevel == STAGE3_REQUIRE) {
            return;
        }
        smcPlayer.setLevel(currentLevel + 1);
        SMCPlayer.addMoney(200, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("shop_upgrade", smcPlayer.level), false);
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

    public void unlockStage1(ServerPlayer serverPlayer) {
        this.stage = 1;
        addMoney(1000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE2_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("meat_available"), false);

    }

    public void unlockStage2(ServerPlayer serverPlayer) {
        this.stage = 2;
        addMoney(5000, serverPlayer);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_game_stage", STAGE3_REQUIRE), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("seafood_available"), false);
        //TODO 解锁最终boss战
    }

    public void unlockStage3(ServerPlayer serverPlayer) {
        this.stage = 3;
        addMoney(20000, serverPlayer);
        //TODO 弹对话
    }

    public List<PlateFood> getOrderList() {
        return switch (this.getStage()) {
            case 1 -> STAGE1_FOOD_LIST;
            case 2 -> STAGE2_FOOD_LIST;
            default -> STAGE0_FOOD_LIST;
        };
    }

    public static void updateWorkingState(boolean isWorking, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (smcPlayer.isWorking != isWorking) {
            smcPlayer.isWorking = isWorking;
            if (isWorking) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("start_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            } else {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("end_work").withStyle(ChatFormatting.BOLD), true);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
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

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void setLevel(int level) {
        this.level = level;//TODO 双端
    }

    public void levelUp() {

    }

    public int getLevel() {
        return level;
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
        this.levelUpLeft = old.levelUpLeft;
        this.level = old.level;
        this.stage = old.stage;
        this.moneyCount = old.moneyCount;
        this.consumerLeft = old.consumerLeft;
        this.isWorking = old.isWorking;
        this.isSpecialAlive = old.isSpecialAlive;
    }

    public void syncToClient(ServerPlayer serverPlayer) {
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncSMCPlayerPacket(saveNBTData(new CompoundTag())), serverPlayer);
    }

    public void tick(Player player) {
        if(player.level().isClientSide){
            if(isWorking) {
                WorkingMusicPlayer.playWorkingMusic();
            } else {
                WorkingMusicPlayer.stopMusic();
            }
        } else {
            if (this.currentTalkingEntity != null && this.currentTalkingEntity.isAlive()) {
                this.currentTalkingEntity.getLookControl().setLookAt(player);
                this.currentTalkingEntity.getNavigation().stop();
            }
        }
    }

}
