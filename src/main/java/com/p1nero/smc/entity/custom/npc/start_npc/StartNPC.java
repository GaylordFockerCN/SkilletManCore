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
import com.p1nero.smc.entity.custom.npc.customer.FakeCustomer;
import com.p1nero.smc.event.ServerEvents;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.AddWaypointPacket;
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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.p1nero.smc.block.entity.MainCookBlockEntity.PROFESSION_LIST;

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
    public static final Set<ItemStack> STAPLE_SET2 = new HashSet<>();
    public static final Set<ItemStack> VEG_SET2 = new HashSet<>();
    public static final Set<ItemStack> MEAT_SET2 = new HashSet<>();
    public static final Set<ItemStack> SEAFOOD_SET2 = new HashSet<>();
    public static final List<Item> FOODS_NEED_CUT = new ArrayList<>();

    public static void initIngredients() {
        Collections.addAll(STAPLE_SET, IngredientConfig.get().getAll(FoodType.CARB));
        Collections.addAll(VEG_SET, IngredientConfig.get().getAll(FoodType.VEG));
        Collections.addAll(MEAT_SET, IngredientConfig.get().getAll(FoodType.MEAT));
        Collections.addAll(SEAFOOD_SET, IngredientConfig.get().getAll(FoodType.SEAFOOD));
        STAPLE_SET.removeIf(itemStack -> itemStack.is(ModItems.RAW_PASTA.get()));
        VEG_SET.removeIf(itemStack -> itemStack.is(Items.BROWN_MUSHROOM) || itemStack.is(Items.RED_MUSHROOM) || itemStack.is(ModItems.CABBAGE_LEAF.get()));
        MEAT_SET.removeIf(itemStack -> itemStack.is(ModItems.BACON.get()) || itemStack.is(ModItems.COOKED_BACON.get())
                || itemStack.is(ModItems.MUTTON_CHOPS.get()) || itemStack.is(ModItems.COOKED_MUTTON_CHOPS.get()) ||
                itemStack.is(ModItems.CHICKEN_CUTS.get()) || itemStack.is(ModItems.COOKED_CHICKEN_CUTS.get()));
        SEAFOOD_SET.removeIf(itemStack -> itemStack.is(ModItems.SALMON_SLICE.get()) || itemStack.is(ModItems.COOKED_SALMON_SLICE.get())
                || itemStack.is(ModItems.COD_SLICE.get()) || itemStack.is(ModItems.COOKED_COD_SLICE.get()));

        FOODS_NEED_CUT.addAll(List.of(ModItems.BROWN_MUSHROOM_COLONY.get(), ModItems.RED_MUSHROOM_COLONY.get(), ModItems.CABBAGE.get(), Items.MELON,
                Items.BEEF, Items.PORKCHOP, Items.MUTTON, Items.COOKED_MUTTON, Items.CHICKEN, Items.COOKED_CHICKEN,
                Items.SALMON, Items.COOKED_SALMON, Items.COD, Items.COOKED_COD,
                ModItems.WHEAT_DOUGH.get()
        ));

        STAPLE_SET2.addAll(List.of(ModItems.RICE.get().getDefaultInstance(), ModItems.WHEAT_DOUGH.get().getDefaultInstance()));
        VEG_SET2.addAll(List.of(ModItems.BROWN_MUSHROOM_COLONY.get().getDefaultInstance(), ModItems.RED_MUSHROOM_COLONY.get().getDefaultInstance()));
        VEG_SET2.addAll(List.of(Items.MELON.getDefaultInstance(), ModItems.CABBAGE.get().getDefaultInstance(), ModItems.TOMATO.get().getDefaultInstance()));
        MEAT_SET2.addAll(List.of(Items.EGG.getDefaultInstance(), ModItems.HAM.get().getDefaultInstance(), Items.PORKCHOP.getDefaultInstance(), Items.BEEF.getDefaultInstance(), Items.MUTTON.getDefaultInstance(), Items.CHICKEN.getDefaultInstance()));
        SEAFOOD_SET2.addAll(List.of(Items.SALMON.getDefaultInstance(), Items.COD.getDefaultInstance()));

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

        if (this.getOwner() instanceof ServerPlayer serverPlayer && this.isHired() && this.isWorkingTime()) {
            this.getEntityData().set(INCOME, this.getIncome() + this.getIncomeSpeed());

            if (this.tickCount % 300 == 0) {
                BlockPos centerPos = this.getHomePos();
                double centerX = centerPos.getX() + 0.5;
                double centerZ = centerPos.getZ() + 0.5;

                double angle = Math.random() * 2 * Math.PI;
                double radius = this.getRandom().nextInt(15, 20);

                double spawnX = centerX + Math.cos(angle) * radius;
                double spawnZ = centerZ + Math.sin(angle) * radius;

                BlockPos spawnPos = ServerEvents.getSurfaceBlockPos(((ServerLevel) this.level()), (int) spawnX, (int) spawnZ);
                FakeCustomer customer = new FakeCustomer(this, spawnPos.getCenter());
                customer.setHomePos(this.getHomePos());
                customer.setSpawnPos(spawnPos);
                customer.getNavigation().moveTo(customer.getNavigation().createPath(this.getHomePos(), 3), 1.0);
                VillagerProfession profession = PROFESSION_LIST.get(customer.getRandom().nextInt(PROFESSION_LIST.size()));//随机抽个职业，换皮肤好看
                customer.setVillagerData(customer.getVillagerData().setType(VillagerType.byBiome(serverPlayer.serverLevel().getBiome(this.getOnPos()))).setProfession(profession));
                level().addFreshEntity(customer);
            }
        }

        currentSleepingString = totalSleepingString.substring(0, (this.tickCount / 20) % totalSleepingString.length());
    }

    /**
     * 用level.isDay的话双端不同步
     */
    public boolean isWorkingTime() {
        long currentTime = this.level().getDayTime() % 24000;
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
                TreeNode takeMoney = new TreeNode(dialogueComponentBuilder.ans(3), dialogueComponentBuilder.opt(5))
                        .addExecutable((byte) 4);
                TreeNode main = new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(7))
                        .addChild(takeMoney)//全部取出
                        .addLeaf(dialogueComponentBuilder.opt(6, this.getUpgradeNeed()), (byte) 5)//升级
                        .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3);//告辞
                takeMoney.addChild(main);
                builder.setAnswerRoot(main);//告辞
            } else {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.ans(9))
                        .addLeaf(dialogueComponentBuilder.opt(9), (byte) 3)
                        .addLeaf(dialogueComponentBuilder.opt(10), (byte) 7));
            }

        } else if (isGuider()) {

            TreeNode ticketExchange = new TreeNode(dialogueComponentBuilder.ans(11), dialogueComponentBuilder.opt(16))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(17))//武器
                            .addLeaf(dialogueComponentBuilder.opt(18, 160), (byte) 16)
                            .addLeaf(dialogueComponentBuilder.opt(19, 1499), (byte) 17))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(24))//盔甲
                            .addLeaf(dialogueComponentBuilder.opt(18, 160), (byte) 26)
                            .addLeaf(dialogueComponentBuilder.opt(19, 1499), (byte) 27))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(20))
                            .addLeaf(dialogueComponentBuilder.opt(18, 1600), (byte) 18)
                            .addLeaf(dialogueComponentBuilder.opt(19, 14999), (byte) 19))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(21))
                            .addLeaf(dialogueComponentBuilder.opt(18, 1600), (byte) 20)
                            .addLeaf(dialogueComponentBuilder.opt(19, 16000), (byte) 21))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(22))
                            .addLeaf(dialogueComponentBuilder.opt(18, 1600), (byte) 22)
                            .addLeaf(dialogueComponentBuilder.opt(19, 16000), (byte) 23))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(23))
                            .addLeaf(dialogueComponentBuilder.opt(18, 1600), (byte) 24)
                            .addLeaf(dialogueComponentBuilder.opt(19, 16000), (byte) 25));


            TreeNode foodBuyer = new TreeNode(dialogueComponentBuilder.ans(10), dialogueComponentBuilder.opt(11))
                    .addLeaf(dialogueComponentBuilder.opt(12), (byte) 12)
                    .addLeaf(dialogueComponentBuilder.opt(13), (byte) 13);
            int stage = senderData.getInt("player_stage");

            if (stage >= 1) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(14), (byte) 14);
            }

            if (stage >= 2) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(15), (byte) 15);
            }

            //入职给予新手福利，锅铲和锅，建筑方块，初始食材订购机等等
            if (senderData.getBoolean("first_gift_got")) {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.ans(1))
                        .addChild(ticketExchange)
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.ans(6), dialogueComponentBuilder.opt(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.ans(7), dialogueComponentBuilder.opt(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.ans(8), dialogueComponentBuilder.opt(8))
                                                .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3))))
                        .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3)); //告辞
            } else {
                builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.ans(1))
                        .addLeaf(dialogueComponentBuilder.opt(3), (byte) 6) //新手福利
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.ans(6), dialogueComponentBuilder.opt(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.ans(7), dialogueComponentBuilder.opt(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.ans(8), dialogueComponentBuilder.opt(8))
                                                .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3))))
                        .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3)); //告辞
            }
        } else {
            //初始态
            builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.ans(0))
                    .addLeaf(dialogueComponentBuilder.opt(0, 100), (byte) 1) //入职
                    .addLeaf(dialogueComponentBuilder.opt(1, getUpgradeNeed()), (byte) 2) //雇佣
                    .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3)); //告辞
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
                addShopToMap(player);
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
                addShopToMap(player);
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
            ItemUtil.addItem(player, SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.asStack(), true);
            ItemUtil.addItem(player, SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get().getDefaultInstance(), true);
            ItemUtil.addItem(player, ModItems.CUTTING_BOARD.get().getDefaultInstance(), true);
            ItemUtil.addItem(player, ModItems.IRON_KNIFE.get().getDefaultInstance(), true);
            ItemUtil.addItem(player, SOLCarrotItems.FOOD_BOOK.get(), 1);
            ItemUtil.addItem(player, CDItems.PLATE.asStack(10), true);
            ItemUtil.addItem(player, Blocks.CRAFTING_TABLE.asItem(), 1);
            ItemUtil.addItem(player, Blocks.STONECUTTER.asItem(), 1);
            ItemUtil.addItem(player, Blocks.JUKEBOX.asItem(), 1);
//            ItemUtil.addItem(player, Blocks.OAK_WOOD.asItem(), 64);
//            ItemUtil.addItem(player, Blocks.STONE.asItem().asItem(), 64);
//            ItemUtil.addItem(player, Blocks.COBBLESTONE.asItem().asItem(), 64);
//            ItemUtil.addItem(player, Blocks.BRICKS.asItem().asItem(), 64);
            ItemUtil.addItem(player, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack(10), true);
            ItemUtil.addItem(player, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack(10), true);
            ItemUtil.addItem(player, SMCRegistrateItems.REDSTONE_RAFFLE.asStack(2), true);

            player.playSound(SoundEvents.PLAYER_LEVELUP);
        }

        if (interactionID == 7) {
            player.displayClientMessage(Component.literal("[").append(name.copy().withStyle(ChatFormatting.YELLOW)).append(Component.literal("]: Zzz....Zzz...Zzz...")), false);
            SMCAdvancementData.finishAdvancement("fake_sleep", player);
        }

        //主食大礼包
        if (interactionID == 12) {
            addIngredient(smcPlayer, player, STAPLE_SET2, 100, 10);
        }
        //果蔬大礼包
        if (interactionID == 13) {
            addIngredient(smcPlayer, player, VEG_SET2, 100, 10);
        }
        //肉类大礼包
        if (interactionID == 14) {
            addIngredient(smcPlayer, player, MEAT_SET2, 2000, 20);
        }
        //海鲜大礼包
        if (interactionID == 15) {
            addIngredient(smcPlayer, player, SEAFOOD_SET2, 5000, 20);
        }

        //武器抽奖券 1
        if (interactionID == 16) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 160, 1);
        }
        if (interactionID == 17) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 1499, 10);
        }
        //技能书抽奖券
        if (interactionID == 18) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 1600, 1);
        }
        if (interactionID == 19) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 14999, 10);
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
        //盔甲
        if (interactionID == 26) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack()), 160, 1);
        }
        if (interactionID == 27) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack()), 1499, 10);
        }

        this.setConversingPlayer(null);
    }

    public void addShopToMap(ServerPlayer serverPlayer){
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new AddWaypointPacket("A New Shop", this.getHomePos()), serverPlayer);
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
            AtomicBoolean contain = new AtomicBoolean(false);
            for (int i = 0; i < foodCount; i++) {
                ItemStack newItem = itemList.get(random.nextInt(itemList.size()));
                contain.set(false);
                applyItems.forEach(itemStack -> {
                    if(itemStack.is(newItem.getItem())){
                        itemStack.setCount(itemStack.getCount() + 1);
                        contain.set(true);
                    }
                });
                if(!contain.get()){
                    applyItems.add(newItem.copy());
                }
            }
            for (ItemStack itemStack : applyItems) {
                ItemUtil.addItem(player, itemStack, true);
            }
        }
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
