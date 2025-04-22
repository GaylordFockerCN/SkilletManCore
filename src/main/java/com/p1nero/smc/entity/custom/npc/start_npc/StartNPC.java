package com.p1nero.smc.entity.custom.npc.start_npc;

import com.cazsius.solcarrot.item.SOLCarrotItems;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.gameasset.skill.SMCSkills;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.*;

/**
 * 凝渊人，引导的npc
 */
public class StartNPC extends SMCNpc {
    protected static final EntityDataAccessor<Integer> INCOME = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//收入
    protected static final EntityDataAccessor<Integer> INCOME_SPEED = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//收入速度 / 店铺等级
    protected static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//状态

    public static final Set<ItemStack> STAPLE_SET = new HashSet<>();
    public static final Set<ItemStack> VEG_SET = new HashSet<>();
    public static final Set<ItemStack> MEAT_SET = new HashSet<>();
    public static final Set<ItemStack> SEAFOOD_SET = new HashSet<>();

    public static void initIngredients() {
        Collections.addAll(STAPLE_SET, IngredientConfig.get().getAll(FoodType.CARB));
        Collections.addAll(VEG_SET, IngredientConfig.get().getAll(FoodType.VEG));
        Collections.addAll(MEAT_SET, IngredientConfig.get().getAll(FoodType.MEAT));
        Collections.addAll(SEAFOOD_SET, IngredientConfig.get().getAll(FoodType.SEAFOOD));
    }

    public static final int EMPTY = 0;
    public static final int HIRED = 1;
    public static final int GUIDER = 2;
    private final Component name;
    public final String totalSleepingString = "Zzz.......";
    private String currentSleepingString = "";
    @Nullable
    private ServerPlayer lastPushPlayer;

    public StartNPC(EntityType<? extends StartNPC> entityType, Level level) {
        super(entityType, level);
        name = Component.translatable(this.getType().getDescriptionId());
    }

    public StartNPC(ServerLevel level, BlockPos pos) {
        this(SMCEntities.START_NPC.get(), level);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.setSpawnPos(pos);
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

    public boolean isHired() {
        return this.getEntityData().get(STATE) == HIRED;
    }

    public boolean isGuider() {
        return this.getEntityData().get(STATE) == GUIDER;
    }

    public void setState(int state) {
        this.getEntityData().set(STATE, state);
    }

    public int getIncome() {
        return this.getEntityData().get(INCOME);
    }

    public int getIncomeSpeed() {
        return this.getEntityData().get(INCOME_SPEED);
    }

    public void setIncomeSpeed(int incomeSpeed) {
        this.getEntityData().set(INCOME_SPEED, incomeSpeed);
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

    public int takeAllIncome() {
        return takeIncome(getIncome());
    }

    public void onSecond() {

        if (!level().isClientSide) {
            if (level().getBlockEntity(this.getHomePos()) instanceof MainCookBlockEntity mainCookBlockEntity) {
                if (mainCookBlockEntity.getStartNPC() != null && !mainCookBlockEntity.getStartNPC().is(this)) {
                    this.discard();
                }
            } else {
                this.discard();
            }

            if (this.position().distanceTo(this.getHomePos().getCenter()) > 2.9) {
                this.setPos(this.getSpawnPos().getCenter());
                if (lastPushPlayer != null) {
                    SMCAdvancementData.finishAdvancement("try_push", lastPushPlayer);
                }
            }
        }

        long currentTime = this.level().getDayTime();
        if (!level().isClientSide && this.isHired() && currentTime > 600 && currentTime < 12700) {
            this.getEntityData().set(INCOME, this.getIncome() + this.getIncomeSpeed());
        }

        currentSleepingString = totalSleepingString.substring(0, (this.tickCount / 20) % totalSleepingString.length());
    }

    public boolean isWorkingTime() {
        long currentTime = this.level().getDayTime();
        return currentTime > 600 && currentTime < 12700;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected CompoundTag getDialogData(CompoundTag compoundTag, ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        compoundTag = super.getDialogData(compoundTag, serverPlayer);
        compoundTag.putBoolean("first_gift_got", DataManager.firstGiftGot.get(serverPlayer));
        compoundTag.putInt("player_stage", smcPlayer.getStage());
        return compoundTag;
    }

    public int getUpgradeNeed() {
        return (int) (5000 * Math.pow(1.1, this.getIncomeSpeed()));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.getEntityData().get(STATE) != EMPTY) {
            if (!player.getUUID().equals(this.getOwnerUUID())) {
                player.displayClientMessage(SkilletManCoreMod.getInfo("already_has_owner"), true);
                return InteractionResult.FAIL;
            }
        }
        return super.mobInteract(player, hand);
    }

    /**
     * 入职或雇佣
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this, name.copy().append(": "));

        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);

        if (isHired()) {

            if (isWorkingTime()) {
                TreeNode takeMoney = new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(3), dialogueComponentBuilder.buildDialogueOption(5))
                        .execute((byte) 4);
                TreeNode main = new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(1), dialogueComponentBuilder.buildDialogueOption(7))
                        .addChild(takeMoney)//全部取出
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(6, this.getUpgradeNeed()), (byte) 5)//升级
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3);//告辞
                takeMoney.addChild(main);
                builder.setAnswerRoot(main);//告辞
            } else {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(9))
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(9), (byte) 3)
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(10), (byte) 7));
            }

        } else if (isGuider()) {

            TreeNode ticketExchange = new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(11), dialogueComponentBuilder.buildDialogueOption(16))
                    .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(12), dialogueComponentBuilder.buildDialogueOption(17))
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(18, 160), (byte) 16)
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(19, 1599), (byte) 17))
                    .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(12), dialogueComponentBuilder.buildDialogueOption(20))
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(18, 160), (byte) 18)
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(19, 1599), (byte) 19))
                    .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(12), dialogueComponentBuilder.buildDialogueOption(21))
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(18, 1600), (byte) 20)
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(19, 16000), (byte) 21))
                    .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(12), dialogueComponentBuilder.buildDialogueOption(22))
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(18, 1600), (byte) 22)
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(19, 16000), (byte) 23))
                    .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(12), dialogueComponentBuilder.buildDialogueOption(23))
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(18, 1600), (byte) 24)
                            .addLeaf(dialogueComponentBuilder.buildDialogueOption(19, 16000), (byte) 25));


            TreeNode foodBuyer = new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(10), dialogueComponentBuilder.buildDialogueOption(11))
                    .addLeaf(dialogueComponentBuilder.buildDialogueOption(12), (byte) 12)
                    .addLeaf(dialogueComponentBuilder.buildDialogueOption(13), (byte) 13);
            int stage = senderData.getInt("player_stage");

            if (stage > 1) {
                foodBuyer.addLeaf(dialogueComponentBuilder.buildDialogueOption(14), (byte) 14);
            }

            if (stage > 2) {
                foodBuyer.addLeaf(dialogueComponentBuilder.buildDialogueOption(15), (byte) 15);
            }

            //入职给予新手福利，锅铲和锅，建筑方块，初始食材订购机等等
            if (senderData.getBoolean("first_gift_got")) {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(1))
                        .addChild(ticketExchange)
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(6), dialogueComponentBuilder.buildDialogueOption(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(7), dialogueComponentBuilder.buildDialogueOption(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(8), dialogueComponentBuilder.buildDialogueOption(8))
                                                .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3))))
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3)); //告辞
            } else {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(1))
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(3), (byte) 6) //新手福利
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(6), dialogueComponentBuilder.buildDialogueOption(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(7), dialogueComponentBuilder.buildDialogueOption(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(8), dialogueComponentBuilder.buildDialogueOption(8))
                                                .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3))))
                        .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3)); //告辞
            }
        } else {
            //初始态
            builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(0))
                    .addLeaf(dialogueComponentBuilder.buildDialogueOption(0, 100), (byte) 1) //入职
                    .addLeaf(dialogueComponentBuilder.buildDialogueOption(1, 1000), (byte) 2) //雇佣
                    .addLeaf(dialogueComponentBuilder.buildDialogueOption(2), (byte) 3)); //告辞
        }

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    /**
     * 初始奖励
     */
    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        //购买
        if (interactionID == 1) {
            if (smcPlayer.getMoneyCount() < 100) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                SMCPlayer.consumeMoney(100, player);
                this.setState(GUIDER);
                this.setOwnerUUID(player.getUUID());
                SMCAdvancementData.finishAdvancement("start_work", player);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                player.displayClientMessage(dialogueComponentBuilder.buildEntityAnswer(2), false);
            }
        }

        //雇佣
        if (interactionID == 2) {
            if (smcPlayer.getMoneyCount() < getUpgradeNeed()) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                SMCPlayer.consumeMoney(getUpgradeNeed(), player);
                this.setState(HIRED);
                this.setOwnerUUID(player.getUUID());
                this.setIncomeSpeed(1);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                player.displayClientMessage(dialogueComponentBuilder.buildEntityAnswer(2), false);
            }
        }

        //全部取出
        if (interactionID == 4) {
            SMCPlayer.addMoney(this.takeAllIncome(), player);
            return;
        }

        //升级
        if (interactionID == 5) {
            if (smcPlayer.getMoneyCount() < getUpgradeNeed()) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                this.setIncomeSpeed(this.getIncomeSpeed() + 1);
                SMCPlayer.consumeMoney(getUpgradeNeed(), player);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                player.displayClientMessage(SkilletManCoreMod.getInfo("shop_upgrade", this.getIncomeSpeed()), false);
            }
        }

        if (interactionID == 6) {
            DataManager.firstGiftGot.put(player, true);
            player.displayClientMessage(dialogueComponentBuilder.buildEntityAnswer(5), false);
            ItemUtil.addItem(player, SOLCarrotItems.FOOD_BOOK.get(), 1);
            ItemUtil.addItem(player, CDItems.SKILLET.asItem(), 1);
            ItemUtil.addItem(player, CDItems.SPATULA.asItem(), 1);
            ItemUtil.addItem(player, CDItems.PLATE.asItem().asItem(), 10);
            ItemStack step = new ItemStack(EpicFightItems.SKILLBOOK.get());
            step.getOrCreateTag().putString("skill", EpicFightSkills.STEP.toString());
            ItemStack parrying = new ItemStack(EpicFightItems.SKILLBOOK.get());
            parrying.getOrCreateTag().putString("skill", EpicFightSkills.PARRYING.toString());
            ItemStack technician = new ItemStack(EpicFightItems.SKILLBOOK.get());
            technician.getOrCreateTag().putString("skill", EpicFightSkills.TECHNICIAN.toString());
            ItemStack dodgeDisplay = new ItemStack(EpicFightItems.SKILLBOOK.get());
            dodgeDisplay.getOrCreateTag().putString("skill", SMCSkills.BETTER_DODGE_DISPLAY.toString());
            ItemStack guard = new ItemStack(EpicFightItems.SKILLBOOK.get());
            guard.getOrCreateTag().putString("skill", EpicFightSkills.GUARD.toString());
            ItemUtil.addItem(player, step);
            ItemUtil.addItem(player, technician);
            ItemUtil.addItem(player, dodgeDisplay);
            ItemUtil.addItem(player, guard);
            ItemUtil.addItem(player, parrying);
            ItemUtil.addItem(player, Blocks.CRAFTING_TABLE.asItem(), 1);
            ItemUtil.addItem(player, Blocks.JUKEBOX.asItem(), 1);
            ItemUtil.addItem(player, Blocks.OAK_WOOD.asItem(), 64);
            ItemUtil.addItem(player, Blocks.STONE.asItem().asItem(), 64);
            ItemUtil.addItem(player, Blocks.COBBLESTONE.asItem().asItem(), 64);
            ItemUtil.addItem(player, Blocks.BRICKS.asItem().asItem(), 64);

            player.playSound(SoundEvents.PLAYER_LEVELUP);
        }

        if (interactionID == 7) {
            player.displayClientMessage(Component.literal("[").append(name.copy().withStyle(ChatFormatting.YELLOW)).append(Component.literal("]: Zzz....Zzz...Zzz...")), false);
            SMCAdvancementData.finishAdvancement("fake_sleep", player);
        }

        //主食大礼包
        if (interactionID == 12) {
            addIngredient(smcPlayer, player, STAPLE_SET, 100, 10);
        }
        //果蔬大礼包
        if (interactionID == 13) {
            addIngredient(smcPlayer, player, VEG_SET, 100, 10);
        }
        //肉类大礼包
        if (interactionID == 14) {
            addIngredient(smcPlayer, player, MEAT_SET, 2000, 20);
        }
        //海鲜大礼包
        if (interactionID == 15) {
            addIngredient(smcPlayer, player, SEAFOOD_SET, 5000, 20);
        }

        //武器抽奖券 1
        if (interactionID == 16) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 160, 1);
        }
        if (interactionID == 17) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 1599, 10);
        }
        //技能书抽奖券
        if (interactionID == 18) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 160, 1);
        }
        if (interactionID == 19) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 1599, 10);
        }
        //宠物
        if (interactionID == 20) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.PET_RAFFLE_TICKET.asStack()), 1600, 1);
        }
        if (interactionID == 21) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.PET_RAFFLE_TICKET.asStack()), 16000, 10);
        }
        //碟
        if (interactionID == 22) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DISC_RAFFLE_TICKET.asStack()), 1600, 1);
        }
        if (interactionID == 23) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DISC_RAFFLE_TICKET.asStack()), 16000, 10);
        }
        //玩偶
        if (interactionID == 24) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asStack()), 1600, 1);
        }
        if (interactionID == 25) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asStack()), 16000, 10);
        }

        this.setConversingPlayer(null);
    }

    public void addIngredient(SMCPlayer smcPlayer, ServerPlayer player, Set<ItemStack> itemStackSet, int moneyNeed, int foodCount) {
        if (smcPlayer.getMoneyCount() < moneyNeed) {
            this.playSound(SoundEvents.VILLAGER_NO);
            player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
        } else {
            SMCPlayer.consumeMoney(moneyNeed, player);
            this.playSound(SMCSounds.VILLAGER_YES.get());
            List<ItemStack> itemList = new ArrayList<>(itemStackSet);
            List<ItemStack> applyItems = new ArrayList<>();
            for (int i = 0; i < foodCount; i++) {
                applyItems.add(itemList.get(random.nextInt(itemList.size())));
            }
            for (ItemStack itemStack : applyItems) {
                ItemUtil.addItemEntity(player, itemStack);
            }
        }
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
            return Component.translatable(this.getType().getDescriptionId() + "_hired", getIncome(), isWorkingTime() ? getIncomeSpeed() : currentSleepingString);
        } else if (isGuider()) {
            return Component.translatable(this.getType().getDescriptionId() + "_guider");
        }

        return Component.translatable(this.getType().getDescriptionId() + "_empty");
    }

    /**
     * 小范围内可推，不能推出范围
     */
    @Override
    public boolean isPushable() {
        return this.position().distanceTo(this.getHomePos().getCenter()) < 3.5;
    }

    @Override
    public void push(@NotNull Entity entity) {
        super.push(entity);
        if (entity instanceof ServerPlayer player) {
            lastPushPlayer = player;
        }
    }
}
