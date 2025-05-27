package com.p1nero.smc.entity.custom.npc.start_npc;

import com.cazsius.solcarrot.item.SOLCarrotItems;
import com.p1nero.dialog_lib.api.component.DialogueComponentBuilder;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.block.entity.MainCookBlockEntity2;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.entity.custom.npc.customer.FakeCustomer;
import com.p1nero.smc.entity.custom.npc.special.zombie_man.ZombieMan;
import com.p1nero.smc.event.ServerEvents;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.AddWaypointPacket;
import com.p1nero.smc.network.packet.clientbound.OpenCreateGuideScreenPacket;
import com.p1nero.smc.network.packet.clientbound.RemoveWaypointPacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;
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
    protected static final EntityDataAccessor<Integer> SHOP_LEVEL = SynchedEntityData.defineId(StartNPC.class, EntityDataSerializers.INT);//是否升级过店铺

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
    protected final Component name;
    public final String totalSleepingString = "Zzz.......";
    protected String currentSleepingString = "";
    @Nullable
    protected ServerPlayer lastPushPlayer;

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
        this.getEntityData().define(SHOP_LEVEL, 1);
        this.getEntityData().define(INCOME, 0);
        this.getEntityData().define(INCOME_SPEED, 1);
        this.getEntityData().define(STATE, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.getEntityData().set(SHOP_LEVEL, tag.getInt("shop_level"));
        this.getEntityData().set(INCOME, tag.getInt("income"));
        this.getEntityData().set(INCOME_SPEED, tag.getInt("income_speed"));
        this.getEntityData().set(STATE, tag.getInt("shop_state"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("shop_level", this.getEntityData().get(SHOP_LEVEL));
        tag.putInt("income", this.getEntityData().get(INCOME));
        tag.putInt("income_speed", this.getEntityData().get(INCOME_SPEED));
        tag.putInt("shop_state", this.getEntityData().get(STATE));
    }

    public int getShopLevel() {
        return this.getEntityData().get(SHOP_LEVEL);
    }

    public void setShopLevel(int level) {
        if(level().getBlockEntity(this.getHomePos()) instanceof MainCookBlockEntity mainCookBlockEntity) {
            if(mainCookBlockEntity.getShopLevel() < level) {
                mainCookBlockEntity.setShopLevel(level);
            }
        }
        this.getEntityData().set(SHOP_LEVEL, level);
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

    public int getState() {
        return this.getEntityData().get(STATE);
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
            if(shouldRemoveWithoutMainBlock()){
                if (level().getBlockEntity(this.getHomePos()) instanceof MainCookBlockEntity mainCookBlockEntity) {
                    if (mainCookBlockEntity.getStartNPC() != null && !mainCookBlockEntity.getStartNPC().is(this)) {
                        this.discard();
                    }
                } else {
                    this.discard();
                }
            }

            if (this.position().distanceTo(this.getHomePos().getCenter()) > MainCookBlockEntity.SEARCH_DIS - 0.3) {
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

        if(this.isHired() && !this.isWorkingTime()) {
            currentSleepingString = totalSleepingString.substring(0, (this.tickCount / 20) % totalSleepingString.length());
        }
    }

    protected boolean shouldRemoveWithoutMainBlock() {
        return true;
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
    public SoundEvent getAmbientSound() {
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

    public int getUpgradeNpcNeed() {
        return (int) (5000 * Math.pow(1.1, this.getIncomeSpeed()));
    }

    public int getUpgradeShopNeed(){
        return (int) (200 * Math.pow(10, this.getShopLevel()));
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
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
        int moneyRate = (int) smcPlayer.getLevelMoneyRate();
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);

        TreeNode upgrade = null;
        if (getShopLevel() <= 3) {
            upgrade = new TreeNode(dialogueComponentBuilder.ans(13, Component.literal(String.valueOf(getUpgradeShopNeed())).withStyle(ChatFormatting.GREEN)), dialogueComponentBuilder.opt(25));
        }

        if (upgrade != null) {
            upgrade.addLeaf(dialogueComponentBuilder.opt(8), (byte) 28)
                    .addLeaf(dialogueComponentBuilder.opt(7));
        }

        if (isHired()) {

            if (isWorkingTime()) {
                TreeNode takeMoney = new TreeNode(dialogueComponentBuilder.ans(3), dialogueComponentBuilder.opt(5))
                        .addExecutable((byte) 4);
                TreeNode main = new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(7))
                        .addChild(takeMoney)//全部取出
                        .addLeaf(dialogueComponentBuilder.opt(6, this.getUpgradeNpcNeed()), (byte) 5);//升级
                if (upgrade != null) {
                    main.addChild(upgrade);
                }
                main.addLeaf(dialogueComponentBuilder.opt(2), (byte) 3);//告辞
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
                            .addLeaf(dialogueComponentBuilder.opt(18, (160 * moneyRate)), (byte) 16)
                            .addLeaf(dialogueComponentBuilder.opt(19, (1499 * moneyRate)), (byte) 17))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(24))//盔甲
                            .addLeaf(dialogueComponentBuilder.opt(18, (160 * moneyRate)), (byte) 26)
                            .addLeaf(dialogueComponentBuilder.opt(19, (1499 * moneyRate)), (byte) 27))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(20))
                            .addLeaf(dialogueComponentBuilder.opt(18, (1600 * moneyRate)), (byte) 18)
                            .addLeaf(dialogueComponentBuilder.opt(19, (14999 * moneyRate)), (byte) 19))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(21))
                            .addLeaf(dialogueComponentBuilder.opt(18, (16000 * moneyRate)), (byte) 20)
                            .addLeaf(dialogueComponentBuilder.opt(19, (160000 * moneyRate)), (byte) 21))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(22))
                            .addLeaf(dialogueComponentBuilder.opt(18, (1600 * moneyRate)), (byte) 22)
                            .addLeaf(dialogueComponentBuilder.opt(19, (16000 * moneyRate)), (byte) 23));
//                    .addChild(new TreeNode(dialogueComponentBuilder.ans(12), dialogueComponentBuilder.opt(23))
//                            .addLeaf(dialogueComponentBuilder.opt(18, (1600 * moneyRate)), (byte) 24)
//                            .addLeaf(dialogueComponentBuilder.opt(19, (16000 * moneyRate)), (byte) 25));


            TreeNode foodBuyer = new TreeNode(dialogueComponentBuilder.ans(10), dialogueComponentBuilder.opt(11))
                    .addLeaf(dialogueComponentBuilder.opt(12, (100 * moneyRate)), (byte) 12)
                    .addLeaf(dialogueComponentBuilder.opt(13, (100 * moneyRate)), (byte) 13);
            int stage = senderData.getInt("player_stage");

            if (stage >= 1) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(14, (2000 * moneyRate)), (byte) 14);
            }

            if (stage >= 2) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(15, (5000 * moneyRate)), (byte) 15);
            }

            TreeNode root = new TreeNode(dialogueComponentBuilder.ans(1));

            //入职给予新手福利，锅铲和锅，建筑方块，初始食材订购机等等
            if (senderData.getBoolean("first_gift_got")) {
                root.addChild(ticketExchange)
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.ans(6), dialogueComponentBuilder.opt(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.ans(7), dialogueComponentBuilder.opt(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.ans(8), dialogueComponentBuilder.opt(8))
                                                .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3))));
            } else {
                root = new TreeNode(dialogueComponentBuilder.ans(1))
                        .addLeaf(dialogueComponentBuilder.opt(3), (byte) 6) //新手福利
                        .addChild(foodBuyer)//食材订购机
                        // 新手帮助
                        .addChild(new TreeNode(dialogueComponentBuilder.ans(6), dialogueComponentBuilder.opt(4))
                                .addChild(new TreeNode(dialogueComponentBuilder.ans(7), dialogueComponentBuilder.opt(8))
                                        .addChild(new TreeNode(dialogueComponentBuilder.ans(8), dialogueComponentBuilder.opt(8))
                                                .addLeaf(dialogueComponentBuilder.opt(2), (byte) 3))));
            }
            if (upgrade != null) {
                root.addChild(upgrade);
            }
            root.addLeaf(dialogueComponentBuilder.opt(2), (byte) 3); //告辞
            builder.setAnswerRoot(root);
        } else {
            TreeNode startRoot = new TreeNode(dialogueComponentBuilder.ans(0));
            if (DataManager.bossKilled.get(localPlayer)) {
                startRoot.addChild(new TreeNode(ans(14), opt(26))
                        .addLeaf(opt(8), (byte) 29)
                        .addLeaf(opt(2)));
            }
            startRoot.addLeaf(dialogueComponentBuilder.opt(0, 100), (byte) 1); //入职
            startRoot.addLeaf(dialogueComponentBuilder.opt(1, getUpgradeNpcNeed()), (byte) 2); //雇佣
            startRoot.addLeaf(dialogueComponentBuilder.opt(2), (byte) 3);
            //初始态
            builder.setAnswerRoot(startRoot); //告辞
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
        int moneyRate = (int) smcPlayer.getLevelMoneyRate();
        //购买
        if (interactionID == 1) {
            if (smcPlayer.getMoneyCount() < 100 * moneyRate) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                SMCPlayer.consumeMoney(100 * moneyRate, player);
                this.setState(GUIDER);
                this.setOwnerUUID(player.getUUID());
                SMCAdvancementData.finishAdvancement("start_work", player);
                SMCAdvancementData.finishAdvancement(getBiomeTypeNameByBlock() + "_1", player);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                addShopToMap(player);
                player.displayClientMessage(dialogueComponentBuilder.buildEntityAnswer(2), false);
            }
        }

        //雇佣
        if (interactionID == 2) {
            if (smcPlayer.getMoneyCount() < getUpgradeNpcNeed()) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                SMCPlayer.consumeMoney(getUpgradeNpcNeed(), player);
                this.setState(HIRED);
                this.setOwnerUUID(player.getUUID());
                this.setIncomeSpeed(1);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                addShopToMap(player);
                player.displayClientMessage(dialogueComponentBuilder.buildEntityAnswer(2), false);
                SMCAdvancementData.finishAdvancement(getBiomeTypeNameByBlock() + "_1", player);
            }
        }

        //全部取出
        if (interactionID == 4) {
            SMCPlayer.addMoney(this.takeAllIncome(), player);
            return;
        }

        //升级
        if (interactionID == 5) {
            if (smcPlayer.getMoneyCount() < getUpgradeNpcNeed()) {
                this.playSound(SoundEvents.VILLAGER_NO);
                player.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            } else {
                this.setIncomeSpeed(this.getIncomeSpeed() + 1);
                SMCPlayer.consumeMoney(getUpgradeNpcNeed(), player);
                this.playSound(SoundEvents.VILLAGER_CELEBRATE);
                player.displayClientMessage(SkilletManCoreMod.getInfo("shop_upgrade", this.getIncomeSpeed()), false);
            }
        }

        if (interactionID == 6) {
            DataManager.firstGiftGot.put(player, true);
            DataManager.hintUpdated.put(player, true);
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
            ItemUtil.addItem(player, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack(10), true);
            ItemUtil.addItem(player, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack(10), true);
            ItemUtil.addItem(player, SMCRegistrateItems.REDSTONE_RAFFLE.asStack(2), true);
            ItemUtil.addItem(player, Items.OAK_LOG, 10, true);
            ItemUtil.addItem(player, Items.STONE, 10, true);
            ItemUtil.addItem(player, Items.IRON_INGOT, 10, true);

            player.playSound(SoundEvents.PLAYER_LEVELUP);
        }

        if (interactionID == 7) {
            player.displayClientMessage(Component.literal("[").append(name.copy().withStyle(ChatFormatting.YELLOW)).append(Component.literal("]: Zzz....Zzz...Zzz...")), false);
            SMCAdvancementData.finishAdvancement("fake_sleep", player);
        }

        //主食大礼包
        if (interactionID == 12) {
            addIngredient(smcPlayer, player, STAPLE_SET2, 100 * moneyRate, 10);
        }
        //果蔬大礼包
        if (interactionID == 13) {
            addIngredient(smcPlayer, player, VEG_SET2, 100 * moneyRate, 10);
        }
        //肉类大礼包
        if (interactionID == 14) {
            addIngredient(smcPlayer, player, MEAT_SET2, 2000 * moneyRate, 20);
        }
        //海鲜大礼包
        if (interactionID == 15) {
            addIngredient(smcPlayer, player, SEAFOOD_SET2, 5000 * moneyRate, 20);
        }

        //武器抽奖券 1
        if (interactionID == 16) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 160 * moneyRate, 1);
        }
        if (interactionID == 17) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asStack()), 1499 * moneyRate, 10);
        }
        //技能书抽奖券
        if (interactionID == 18) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 1600 * moneyRate, 1);
        }
        if (interactionID == 19) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asStack()), 14999 * moneyRate, 10);
        }
        //宠物
        if (interactionID == 20) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.PET_RAFFLE_TICKET.asStack()), 16000 * moneyRate, 1);
        }
        if (interactionID == 21) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.PET_RAFFLE_TICKET.asStack()), 160000 * moneyRate, 10);
        }
        //碟
        if (interactionID == 22) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DISC_RAFFLE_TICKET.asStack()), 1600 * moneyRate, 1);
        }
        if (interactionID == 23) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DISC_RAFFLE_TICKET.asStack()), 16000 * moneyRate, 10);
        }
//        //玩偶
//        if (interactionID == 24) {
//            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asStack()), 1600 * moneyRate, 1);
//        }
//        if (interactionID == 25) {
//            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asStack()), 16000 * moneyRate, 10);
//        }
        //盔甲
        if (interactionID == 26) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack()), 160 * moneyRate, 1);
        }
        if (interactionID == 27) {
            addIngredient(smcPlayer, player, Set.of(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asStack()), 1499 * moneyRate, 10);
        }

        if(interactionID == 28){
            int need = getUpgradeShopNeed();
            String type = getBiomeTypeNameByBlock();
            if(this.getShopLevel() == 1){
                if(SMCPlayer.hasMoney(player, need, true)) {
                    if(tryPlaceShopPart(player, getShopLocation(2), -6, -1, -9)){
                        SMCPlayer.consumeMoney(need, player);
                        this.setShopLevel(2);
                        SMCAdvancementData.finishAdvancement(type + "_" + this.getShopLevel(), player);
                    }
                }
            } else if(this.getShopLevel() == 2) {
                if(SMCPlayer.hasMoney(player, need, true)) {
                    if(tryPlaceShopPart(player, getShopLocation(3), -6, -1, 1)){
                        SMCPlayer.consumeMoney(need, player);
                        this.setShopLevel(3);
                        SMCAdvancementData.finishAdvancement(type + "_" + this.getShopLevel(), player);
                    }
                }
            } else if(this.getShopLevel() == 3) {
                if(smcPlayer.getStage() < 2 && !player.isCreative()){
                    player.displayClientMessage(SkilletManCoreMod.getInfo("level_no_enough", SMCPlayer.STAGE2_REQUIRE + 1), true);
                    player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_NO, player.getSoundSource(), 1.0F, 1.0F);
                    this.setConversingPlayer(null);
                    return;
                }
                if(SMCPlayer.hasMoney(player, need, true)) {
                    if(tryPlaceShopPart(player, getShopLocation(4), -6, -12, -9)){
                        SMCPlayer.consumeMoney(need, player);
                        StartNPCPlus startNPCPlus = new StartNPCPlus(player.serverLevel(), this.getSpawnPos());
                        startNPCPlus.copyFrom(this);
                        level().addFreshEntity(startNPCPlus);
                        DataManager.shouldShowMachineTicketHint.put(player, true);
                        DataManager.hintUpdated.put(player, true);
                        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenCreateGuideScreenPacket(), player);
                        this.setShopLevel(4);
                        SMCAdvancementData.finishAdvancement(type + "_" + this.getShopLevel(), player);
                    }
                }
            }
        }

        if(interactionID == 29) {
            summonBBQ(player, smcPlayer);
        }

        this.setConversingPlayer(null);
    }

    public void summonBBQ(ServerPlayer player, SMCPlayer smcPlayer){
        if(SMCPlayer.hasMoney(player, 200000, true)) {
            BlockPos referencePos = this.getHomePos().above(1);
            if(tryPlaceShopPart(player, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "village/bbq"), -6, -12, -9)){
                SMCPlayer.consumeMoney(200000, player);
                DataManager.findBBQHint.put(player, false);
                DataManager.hintUpdated.put(player, true);
                addShopToMap(player);
                for(int x = -10; x <= 10; x++) {
                    for(int z = -10; z <= 10; z++) {
                        BlockPos mainPos = referencePos.offset(x, 0 ,z);
                        if(level().getBlockEntity(mainPos) instanceof MainCookBlockEntity2){
                            level().destroyBlock(mainPos, false);
                            Vec3 pos2 = mainPos.getCenter().offsetRandom(this.getRandom(), 0.5F).add(0, 1, 0);
                            ZombieMan zombieMan = new ZombieMan(player, pos2);
                            level().addFreshEntity(zombieMan);
                            Vec3 pos1 = mainPos.getCenter().offsetRandom(this.getRandom(), 0.5F).add(0, 1, 0);
                            StartNPCBBQ startNPCBBQ = new StartNPCBBQ(player, pos1);
                            startNPCBBQ.setState(GUIDER);
                            startNPCBBQ.setShopLevel(5);
                            startNPCBBQ.setOwner(player);
                            startNPCBBQ.setHomePos(mainPos);
                            startNPCBBQ.setSpawnPos(mainPos);
                            level().addFreshEntity(startNPCBBQ);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void copyFrom(StartNPC old) {
        this.setOwnerUUID(old.getOwnerUUID());
        this.setState(old.getState());
        this.setIncomeSpeed(old.getIncomeSpeed());
        this.setShopLevel(old.getShopLevel());
        this.setHomePos(old.getHomePos());
        this.setSpawnPos(old.getSpawnPos());
    }

    public String getBiomeTypeNameByBlock() {
        BlockState referenceBlock = level().getBlockState(this.getHomePos().east());
        String type = "plains";
        if(referenceBlock.is(Blocks.ACACIA_PLANKS)) {
            type = "savanna";
        } else if(referenceBlock.is(Blocks.DARK_OAK_PLANKS)) {
            type = "snowy";
        } else if(referenceBlock.is(Blocks.SPRUCE_PLANKS)) {
            type = "taiga";
        } else if(referenceBlock.is(Blocks.SMOOTH_STONE)) {
            type = "desert";
        }
        return type;
    }

    public ResourceLocation getShopLocation(int level) {
        String type = getBiomeTypeNameByBlock();
        return ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "village/" + type + "/houses/" + type + "_butcher_shop_lv" + level);
    }

    public boolean tryPlaceShopPart(ServerPlayer serverPlayer, ResourceLocation location, int offsetX, int offsetY, int offsetZ) {
        BlockPos mainCookBlockPos = this.getHomePos();
        BlockState stove = level().getBlockState(mainCookBlockPos.above());
        if (!stove.is(ModBlocks.STOVE.get())) {
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("error_when_try_to_upgrade_shop").append(" NO STOVE ABOVE! CAN NOT LOCATE THE DIR."), false);
            return false;
        }
        Direction direction = stove.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Rotation rotation = switch (direction) {
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.CLOCKWISE_90;
            default -> Rotation.NONE;
        };
        ServerLevel serverLevel = serverPlayer.serverLevel();

        serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 50, 0.1D, 0.1D, 0.1D, 0.1);
        serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
        serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.VILLAGER_WORK_ARMORER, SoundSource.BLOCKS, 1, 1);
        serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.BLOCKS, 1, 1);
        serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 1, 1);


        StructureTemplateManager structureManager = serverLevel.getStructureManager();

        BlockPos placePos = switch (direction) {
            case WEST -> mainCookBlockPos.offset(offsetZ, offsetY, -offsetX);
            case SOUTH -> mainCookBlockPos.offset(-offsetX, offsetY, -offsetZ);
            case EAST -> mainCookBlockPos.offset(-offsetZ, offsetY, offsetX);
            default -> mainCookBlockPos.offset(offsetX, offsetY, offsetZ);
        };
        Optional<?> optional = Optional.empty();
        try {
            optional = structureManager.get(location);
        } catch (ResourceLocationException e) {
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("error_when_try_to_upgrade_shop"), false);
            SkilletManCoreMod.LOGGER.error("SMC: Error when try to load structure.", e);
        }

        if (optional.isPresent()) {
            StructureTemplate template = (StructureTemplate) optional.get();
            StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotation(rotation);
            boolean success = template.placeInWorld(serverLevel, placePos, placePos, placeSettings, this.random, 2);
            if (success) {
                serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 2, 1);
                return true;
            } else {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("error_when_try_to_upgrade_shop"), false);
                return false;
            }
        }
        return false;
    }

    public void addShopToMap(ServerPlayer serverPlayer) {
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new RemoveWaypointPacket(SkilletManCoreMod.getInfoKey("no_owner_shop"), this.getHomePos()), serverPlayer);
        PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new AddWaypointPacket(SkilletManCoreMod.getInfoKey("my_new_shop"), this.getHomePos(), null), serverPlayer);
    }

    public void addIngredient(SMCPlayer smcPlayer, ServerPlayer player, Set<ItemStack> itemStackSet, double moneyNeed, int foodCount) {
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
                    if (itemStack.is(newItem.getItem())) {
                        itemStack.setCount(itemStack.getCount() + 1);
                        contain.set(true);
                    }
                });
                if (!contain.get()) {
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
