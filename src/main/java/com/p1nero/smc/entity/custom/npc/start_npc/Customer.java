package com.p1nero.smc.entity.custom.npc.start_npc;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Customer extends SMCNpc {

    protected static final EntityDataAccessor<Boolean> TRADED = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Float> DISTANCE = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.FLOAT);//距炉子距离
    protected static final EntityDataAccessor<Boolean> IS_SPECIAL = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否是隐士，可以给予武林秘籍
    protected static final EntityDataAccessor<Integer> ID = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.INT);//村民编号，用于区别对话
    protected final BlockPos spawnPos;
    private int dieTimer = 0;
    public Customer(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        spawnPos = this.getOnPos();
    }

    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public void setTraded(boolean traded) {
        this.getEntityData().set(TRADED, traded);
        if(traded && !level().isClientSide) {
            dieTimer = 10;
        }
    }

    public boolean isTraded() {
        return this.getEntityData().get(TRADED);
    }

    public void setSpecial(boolean special) {
        this.getEntityData().set(IS_SPECIAL, special);
    }

    public boolean isSpecial() {
        return this.getEntityData().get(IS_SPECIAL);
    }

    public void setDistance(float distance) {
        this.getEntityData().set(DISTANCE, distance);
    }

    public float getDistance() {
        return this.getEntityData().get(DISTANCE);
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return this.isSpecial();
    }

    @Override
    public int getTeamColor() {
        if(isSpecial()) {
            return this.tickCount % 0xFFFFFF;//会五颜六色吗
        }
        return super.getTeamColor();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(TRADED, false);
        this.getEntityData().define(DISTANCE, 0.0F);
        this.getEntityData().define(IS_SPECIAL, false);
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    public void refreshBrain(@NotNull ServerLevel serverLevel) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(serverLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    private void registerBrainGoals(Brain<Villager> villagerBrain) {
        villagerBrain.addActivity(Activity.CORE, VillagerTasks.getSMCVillagerCorePackage(this));
        villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    public boolean inShopRange() {
        return this.getDistance() > this.position().distanceTo(this.getHomePos().getCenter());
    }

    @Nullable
    public MainCookBlockEntity getHomeBlockEntity(){
        BlockEntity blockEntity = level().getBlockEntity(getHomePos());
        return blockEntity instanceof MainCookBlockEntity mainCookBlockEntity ? mainCookBlockEntity : null;
    }

    public void finishTrade() {
        MainCookBlockEntity mainCookBlockEntity = this.getHomeBlockEntity();
        if(mainCookBlockEntity != null) {
            mainCookBlockEntity.onNPCFinishTrade(this);
        }
    }

    @Override
    public void onSecond() {
        super.onSecond();
        if(this.dieTimer > 0) {
            this.dieTimer --;
            if(this.dieTimer == 0) {
                this.discard();//直接消失，无需多言
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(!level().isClientSide) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
            //每五个顾客随机一个隐士
            if(smcPlayer.getLevel() % 5 == 0) {
                this.setSpecial(true);
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected CompoundTag getDialogData(CompoundTag compoundTag, ServerPlayer serverPlayer) {
        compoundTag.putBoolean("is_special", isSpecial());
        return super.getDialogData(compoundTag, serverPlayer);
    }

    /**
     * 一句寒暄，一个提交选项。
     * 如果是特殊村民则弹出 气场不凡 的提示
     */
    @Override
    public void openDialogueScreen(CompoundTag senderData) {

        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        //防止延迟，所以还是用nbt判断好
        if(senderData.getBoolean("is_special")) {

        }

        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {

        super.handleNpcInteraction(player, interactionID);
    }
}
