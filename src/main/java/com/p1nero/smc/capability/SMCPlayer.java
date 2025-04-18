package com.p1nero.smc.capability;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.SyncSMCPlayerPacket;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    private int consumerLeft;
    public static final int STAGE1_REQUIRE = 10;
    public static final int STAGE2_REQUIRE = 20;
    public static final int STAGE3_REQUIRE = 50;
    public static final List<PlateFood> TOTAL_ORDER_LIST = List.of(PlateFood.FRIED_RICE);
    private final List<ItemStack> currentOrderList = new ArrayList<>();

    public int getConsumerLeft() {
        return consumerLeft;
    }

    public void setConsumerLeft(int consumerLeft) {
        this.consumerLeft = consumerLeft;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public static void upgradePlayer(ServerPlayer serverPlayer) {
        //TODO 升级到一定等级解锁新阶段，并且获得对应奖励
    }

    public static void stageUp(ServerPlayer serverPlayer) {

    }

    public void unlockStage1(ServerPlayer serverPlayer){

    }
    public void unlockStage2(ServerPlayer serverPlayer){

    }
    public void unlockStage3(ServerPlayer serverPlayer){

    }

    public void updateFoodList(ServerPlayer serverPlayer){

        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("unlock_new_food"), false);
    }

    public List<ItemStack> getOrderList() {
        return currentOrderList;
    }

    public static void updateWorkingState(boolean isWorking, ServerPlayer serverPlayer){
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if(smcPlayer.isWorking != isWorking) {
            smcPlayer.isWorking = isWorking;
            if(isWorking) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("start_work"), true);
            } else {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("end_work"), true);
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
    public boolean getBoolean(String key){
        return data.getBoolean(key);
    }
    public double getDouble(String key){
        return data.getDouble(key);
    }
    public String getString(String key){
        return data.getString(key);
    }
    public void putBoolean(String key, boolean value){
        data.putBoolean(key, value);
    }
    public void putDouble(String key, double v){
        data.putDouble(key, v);
    }
    public void putString(String k, String v){
        data.putString(k, v);
    }
    public void setData(Consumer<CompoundTag> consumer) {
        consumer.accept(data);
    }

    public CompoundTag getData() {
        return data;
    }

    public CompoundTag saveNBTData(CompoundTag tag){
        tag.putInt("tradeLevel", level);
        tag.putInt("tradeStage", stage);
        tag.putInt("money_count", moneyCount);
        tag.putBoolean("working", isWorking);
        tag.putInt("consumerLeft", consumerLeft);
        tag.put("customDataManager", data);
        return tag;
    }

    public void loadNBTData(CompoundTag tag){
        level = tag.getInt("tradeLevel");
        stage = tag.getInt("tradeStage");
        moneyCount = tag.getInt("money_count");
        consumerLeft = tag.getInt("consumerLeft");
        isWorking = tag.getBoolean("working");
        data = tag.getCompound("customDataManager");
    }

    public void copyFrom(SMCPlayer old){
        data = old.data;
        this.level = old.level;
        this.stage = old.stage;
        this.moneyCount = old.moneyCount;
        this.consumerLeft = old.consumerLeft;
        this.isWorking = old.isWorking;
    }

    public void syncToClient(ServerPlayer serverPlayer) {
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncSMCPlayerPacket(saveNBTData(new CompoundTag())), serverPlayer);
    }

    public void tick(Player player){
        if(this.currentTalkingEntity != null && this.currentTalkingEntity.isAlive()){
            this.currentTalkingEntity.getLookControl().setLookAt(player);
            this.currentTalkingEntity.getNavigation().stop();
        }
    }

}
