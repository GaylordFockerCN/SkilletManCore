package com.p1nero.smc.entity.custom.npc.start_npc;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 凝渊人，引导的npc
 */
public class StartNPC extends SMCNpc {
    protected static final EntityDataAccessor<Integer> INCOME = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//收入
    protected static final EntityDataAccessor<Integer> INCOME_SPEED = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//收入速度
    protected static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//状态
    public static final int EMPTY = 0;
    public static final int HIRED = 1;
    public static final int GUIDER = 2;
    private final Component name;

    public StartNPC(EntityType<? extends StartNPC> entityType, Level level) {
        super(entityType, level);
        name = Component.translatable(this.getType().getDescriptionId()).withStyle(ChatFormatting.YELLOW);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(INCOME, 0);
        this.getEntityData().define(INCOME_SPEED, 1);
        this.getEntityData().define(STATE, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(INCOME, tag.getInt("income"));
        this.getEntityData().set(INCOME_SPEED, tag.getInt("income_speed"));
        this.getEntityData().set(STATE, tag.getInt("shop_state"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("income", this.getEntityData().get(INCOME));
        tag.putInt("income_speed", this.getEntityData().get(INCOME_SPEED));
        tag.putInt("shop_state", this.getEntityData().get(STATE));
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
        villagerBrain.addActivity(Activity.CORE, VillagerTasks.getSMCVillagerCorePackage());
        villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    public boolean isHired() {
        return this.getEntityData().get(STATE) == HIRED;
    }

    public boolean isGuider() {
        return this.getEntityData().get(STATE) == GUIDER;
    }

    public int getIncome() {
        return this.getEntityData().get(INCOME);
    }

    /**
     * 取钱
     */
    public int takeIncome(int count) {
        int current = this.getIncome();
        if (count <= current) {
            this.getEntityData().set(INCOME, current - count);
            return count;
        } else {
            this.getEntityData().set(INCOME, 0);
            return current;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            this.getEntityData().set(INCOME, this.getIncome() + 1);
        }
    }

    /**
     * 入职或雇佣
     */
    @Override
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this, name);

        DialogueComponentBuilder choiceBuilder = new DialogueComponentBuilder(this);

        if (isHired()) {
            TreeNode takeMoney = new TreeNode(choiceBuilder.buildDialogueAnswer(3), choiceBuilder.buildDialogueOption(5));
            TreeNode main = new TreeNode(choiceBuilder.buildDialogueAnswer(1), choiceBuilder.buildDialogueOption(7))
                    .addChild(takeMoney)
                    .addLeaf(choiceBuilder.buildDialogueOption(6), (byte) 5)//升级
                    .addLeaf(choiceBuilder.buildDialogueOption(2), (byte) 3);//告辞
            takeMoney.addChild(main);
            builder.setAnswerRoot(main);//告辞
        } else if (isGuider()) {
            //入职给予新手福利，锅铲和锅，建筑方块，初始食材订购机等等
            if (senderData.getBoolean("first_gift_got")) {
                builder.setAnswerRoot(new TreeNode(choiceBuilder.buildDialogueAnswer(1))
                        .addLeaf(choiceBuilder.buildDialogueOption(4), (byte) 2) //询问
                        .addLeaf(choiceBuilder.buildDialogueOption(2), (byte) 3)); //告辞
            } else {
                builder.setAnswerRoot(new TreeNode(choiceBuilder.buildDialogueAnswer(1))
                        .addLeaf(choiceBuilder.buildDialogueOption(3), (byte) 6) //新手福利
                        // 询问 TODO 补全对话
                        .addChild(new TreeNode(choiceBuilder.buildDialogueAnswer(666), choiceBuilder.buildDialogueOption(2))
                                .addChild(choiceBuilder.buildDialogueAnswer(666), choiceBuilder.buildDialogueOption(2)))
                        .addLeaf(choiceBuilder.buildDialogueOption(2), (byte) 3)); //告辞
            }
        } else {
            //初始态
            builder.setAnswerRoot(new TreeNode(choiceBuilder.buildDialogueAnswer(0))
                    .addLeaf(choiceBuilder.buildDialogueOption(0), (byte) 1) //入职
                    .addLeaf(choiceBuilder.buildDialogueOption(1), (byte) 2) //雇佣
                    .addLeaf(choiceBuilder.buildDialogueOption(2), (byte) 3)); //告辞
        }

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    /**
     * 初始奖励
     */
    @Override
    public void handleNpcInteraction(Player player, byte interactionID) {
        DialogueComponentBuilder answerBuilder = new DialogueComponentBuilder(this);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        //购买
        if (interactionID == 1) {
            if (smcPlayer.getMoneyCount() < 100) {
                this.playSound(SoundEvents.VILLAGER_NO);
            } else {
                //TODO 扣钱
                this.playSound(SoundEvents.VILLAGER_TRADE);
                player.displayClientMessage(answerBuilder.buildEntityAnswer(2), false);
            }
        }

        //雇佣
        if (interactionID == 2) {
            if (smcPlayer.getMoneyCount() < 10000) {
                this.playSound(SoundEvents.VILLAGER_NO);
            } else {
                //TODO 扣钱
                this.playSound(SoundEvents.VILLAGER_TRADE);
                player.displayClientMessage(answerBuilder.buildEntityAnswer(2), false);
            }
        }

        //全部取出
        if (interactionID == 4) {

        }

        //升级
        if (interactionID == 5) {

        }

        if(interactionID == 6) {
            player.displayClientMessage(answerBuilder.buildEntityAnswer(5), false);
            //TODO 给予初始物品

            ItemUtil.addItem(player, CDItems.SKILLET.asItem(), 1);
            ItemUtil.addItem(player, CDItems.SPATULA.asItem(), 1);

            player.playSound(SoundEvents.PLAYER_LEVELUP);
        }

        this.setConversingPlayer(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.getName();
    }

    @Override
    public @NotNull Component getName() {
        if (isHired()) {
            return Component.translatable(this.getType().getDescriptionId() + "_hired");
        } else if (isGuider()) {
            return Component.translatable(this.getType().getDescriptionId() + "_guider");
        }

        return Component.translatable(this.getType().getDescriptionId() + "_empty");
    }

    /**
     * 推不动
     */
    @Override
    public boolean isPushable() {
        return false;
    }

}
