package com.p1nero.smc.entity.custom.npc.customer;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.normal.*;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.special.*;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCBlockDialoguePacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDMisc;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Customer extends SMCNpc {

    protected static final EntityDataAccessor<Boolean> TRADED = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Integer> MOOD_AFTER_TRADE = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.INT);//交易后是否happy
    protected static final EntityDataAccessor<Boolean> IS_SPECIAL = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否是隐士，可以给予武林秘籍
    protected static final EntityDataAccessor<Integer> SMC_ID = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.INT);//村民编号，用于区别对话
    protected static final EntityDataAccessor<ItemStack> ORDER = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.ITEM_STACK);//请求的食物
    public static int MAX_CUSTOMER_TYPE;
    public static final int NO_MOOD = 0;
    public static final int HAPPY = 1;
    public static final int UN_HAPPY = 2;
    public static final List<CustomerData> CUSTOMERS = new ArrayList<>();
    public static final List<CustomerData> SPECIAL_CUSTOMERS = new ArrayList<>();

    static {
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData1());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData2());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData3());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData4());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData5());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData6());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData7());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData8());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData9());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData10());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData11());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData12());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData13());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData14());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData15());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData16());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData17());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData18());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData19());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData20());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData21());
        SPECIAL_CUSTOMERS.add(new SpecialCustomerData22());

        CUSTOMERS.add(new NormalCustomerData1());
        CUSTOMERS.add(new NormalCustomerData2());
        CUSTOMERS.add(new NormalCustomerData3());
        CUSTOMERS.add(new NormalCustomerData4());
        CUSTOMERS.add(new NormalCustomerData5());
        CUSTOMERS.add(new NormalCustomerData6());

        MAX_CUSTOMER_TYPE = SPECIAL_CUSTOMERS.size() + CUSTOMERS.size();
    }

    @Nullable
    protected CustomerData customerData;
    protected int foodLevel;
    private int dieTimer = 0;
    public Customer(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        this.setSMCId(this.getRandom().nextInt(MAX_CUSTOMER_TYPE));//确保村民随机且固定
    }

    public Customer(Player owner, Vec3 pos) {
        this(SMCEntities.CUSTOMER.get(), owner.level());
        this.setPos(pos);
        this.setOwnerUUID(owner.getUUID());
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(owner);
        List<PlateFood> foodList = smcPlayer.getOrderList();
        this.setOrder(foodList.get(owner.getRandom().nextInt(foodList.size())).item.asStack());
    }

    public @Nullable CustomerData getCustomerData() {
        return customerData;
    }

    public void setTraded(boolean traded) {
        this.getEntityData().set(TRADED, traded);
        if(traded && !level().isClientSide) {
            dieTimer = 10;
        }
        if(this.isSpecial() && this.getOwner() instanceof ServerPlayer serverPlayer) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
            smcPlayer.setSpecialAlive(false);
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

    @Override
    public boolean isCurrentlyGlowing() {
        return this.isSpecial() || this.tickCount > 60 * 20;//大于一分钟就提示
    }

    @Override
    public int getTeamColor() {
        if(isSpecial()) {
            return 0xfff66d;//金色传说！
        }
        return super.getTeamColor();
    }

    public int getSMCId() {
        return this.getEntityData().get(SMC_ID);
    }

    public void setSMCId(int id) {
        this.getEntityData().set(SMC_ID, id);
    }

    public void setOrder(ItemStack order) {
        this.getEntityData().set(ORDER, order);
    }

    public ItemStack getOrder() {
        return this.getEntityData().get(ORDER);
    }
    public void setMoodAfterTrade(int moodAfterTrade) {
        this.getEntityData().set(MOOD_AFTER_TRADE, moodAfterTrade);
    }

    public int getMoodAfterTrade() {
        return this.getEntityData().get(MOOD_AFTER_TRADE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ORDER, ItemStack.EMPTY);
        this.getEntityData().define(SMC_ID, 0);
        this.getEntityData().define(MOOD_AFTER_TRADE, NO_MOOD);
        this.getEntityData().define(TRADED, false);
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

    @Nullable
    public MainCookBlockEntity getHomeBlockEntity(){
        return level().getBlockEntity(getHomePos()) instanceof MainCookBlockEntity mainCookBlockEntity ? mainCookBlockEntity : null;
    }

    public void onFinishTrade(ServerPlayer owner, int result) {
        MainCookBlockEntity mainCookBlockEntity = this.getHomeBlockEntity();
        if(mainCookBlockEntity != null) {
            mainCookBlockEntity.onNPCFinishTrade(owner, this, result);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getUnhappyCounter() > 0) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        }

        if(this.getConversingPlayer() == null && !level().isClientSide) {
            this.getNavigation().moveTo(this.getNavigation().createPath(this.isTraded() ? this.getSpawnPos() : this.getHomePos(), 3), 1.0F);
            //开门
            Path path = this.getNavigation().getPath();
            if(path != null && path.getNextNodeIndex() < path.getNodeCount()) {
                BlockPos pos = path.getNextNode().asBlockPos();
                BlockState blockState = level().getBlockState(pos);
                if (blockState.is(BlockTags.WOODEN_DOORS, (base) -> base.getBlock() instanceof DoorBlock)) {
                    DoorBlock block = (DoorBlock)blockState.getBlock();
                    if (!block.isOpen(blockState)) {
                        block.setOpen(this, level(), blockState, pos, true);
                    }
                }
            }

        }

        if(!this.isTraded() && this.getOwner() != null) {
            this.getLookControl().setLookAt(this.getOwner());
        }

    }

    @Override
    public void onSecond() {
        super.onSecond();
        if(this.dieTimer > 0) {
            this.dieTimer --;
            if(this.dieTimer <= 0) {
                this.discard();//直接消失，无需多言
            }
        }

        if(this.tickCount > 3600 && !this.isTraded()) {
            if(this.getOwner() instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("customer_left"), false);
                if(this.isSpecial()) {
                    SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
                    smcPlayer.setSpecialAlive(false);
                }
                this.setTraded(true);
            }
        }

        if(this.isTraded() && this.getOnPos().getCenter().distanceTo(this.getSpawnPos().getCenter()) < 2) {
            this.discard();
        }

    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float value) {
        //可以攻击他人的顾客
        if(source.getEntity() instanceof ServerPlayer serverPlayer && !serverPlayer.equals(this.getOwner())){
            this.setTraded(true);
            return super.hurt(source, 1145);
        }
        return super.hurt(source, value);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(this.isTraded()) {
            return InteractionResult.FAIL;
        }
        if(player instanceof ServerPlayer serverPlayer && this.getOwner() != null && !player.getUUID().equals(this.getOwnerUUID())) {
            SMCAdvancementData.finishAdvancement("hijack_customer", serverPlayer);
        }

        if(this.getOrder().is(player.getMainHandItem().getItem())) {
            CookedFoodData cookedFoodData = BaseFoodItem.getData(player.getMainHandItem());
            if(cookedFoodData != null){
                int score = cookedFoodData.score;
                if(score >= 90) {
                    this.foodLevel = CustomerData.BEST;
                } else if(score >= 70){
                    this.foodLevel = CustomerData.MIDDLE;
                } else {
                    this.foodLevel = CustomerData.BAD;
                }

                if(cookedFoodData.toFoodData() == CookedFoodData.BAD) {
                    this.foodLevel = CustomerData.BAD;
                }
            }
        }
        if(this.foodLevel == CustomerData.BAD && !DataManager.firstFoodBad.get(player)) {
            if(player instanceof ServerPlayer serverPlayer) {
                //灶王爷不乐意了
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("is_first_food_bad", true);
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCBlockDialoguePacket(this.getHomePos(), tag), serverPlayer);
                level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 3.0F, 1.0F);
                player.hurt(player.damageSources().magic(), 0.1F);
                DataManager.firstFoodBad.put(player, true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        //双端
        if(customerData == null && !level().isClientSide) {
            //每级1/5概率刷新神人
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
            if(smcPlayer.getLevel() > 0 && !smcPlayer.isSpecialAlive() && this.getRandom().nextInt(3) == 1) {
                customerData = SPECIAL_CUSTOMERS.get(this.getSMCId() % SPECIAL_CUSTOMERS.size());
                this.setSpecial(true);
                smcPlayer.setSpecialAlive(true);
            } else {
                customerData = CUSTOMERS.get(this.getSMCId() % CUSTOMERS.size());
            }
            if(customerData != null && player instanceof ServerPlayer){
                customerData.onInteract(((ServerPlayer) player), this);
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected CompoundTag getDialogData(CompoundTag compoundTag, ServerPlayer serverPlayer) {
        compoundTag.putBoolean("is_special", isSpecial());
        compoundTag.putInt("smc_id", getSMCId());
        compoundTag.putInt("food_level", foodLevel);
        BaseFoodItem.getData(this.getOrder());
        compoundTag.putString("food_name", this.getOrder().getDescriptionId());
        compoundTag.putBoolean("can_submit", !this.getOrder().isEmpty() && this.getOrder().is(serverPlayer.getMainHandItem().getItem()));
        return super.getDialogData(compoundTag, serverPlayer);
    }

    /**
     * 一句寒暄，一个提交选项。
     * customer _ 实体编号 _ 是否为特殊顾客 _ 对话编号
     *            1 ~ 100    _s            0 ~ 4
     * 0:需求
     * 1:选项 （交付）
     * 2：100%时的评价
     * 3：80%时的评价
     * 4：60%时的评价
     * 如果是特殊村民则弹出 气场不凡 的提示
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);
        if(senderData.getBoolean("is_special")){
            customerData = SPECIAL_CUSTOMERS.get(this.getSMCId() % SPECIAL_CUSTOMERS.size());
        } else {
            customerData = CUSTOMERS.get(this.getSMCId() % CUSTOMERS.size());
        }
        this.customerData.getDialogScreen(senderData, builder, dialogueComponentBuilder, senderData.getBoolean("can_submit"), senderData.getInt("food_level"));
        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if(interactionID == CustomerData.BAD) {
            this.setMoodAfterTrade(UN_HAPPY);
            this.setTraded(true);//离去
            onFinishTrade(player, CustomerData.BAD);
        }

        if(interactionID == CustomerData.BEST || interactionID == CustomerData.MIDDLE) {
            SMCPlayer.addExperience(player);//能吃就能升级
            this.setMoodAfterTrade(interactionID == CustomerData.BEST ? HAPPY : UN_HAPPY);
            this.setTraded(true);//离去
            onFinishTrade(player, interactionID);
        }

        if(interactionID == CustomerData.SUBMIT_FOOD) {
            //提交食物专用的代码
            this.setOrder(player.getMainHandItem());
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            ItemUtil.addItem(player, SMCRegistrateItems.DIRT_PLATE.asItem(), 1);
            return;
        } else if(this.customerData != null){
            this.customerData.handle(player, this, interactionID);
        }

        this.setConversingPlayer(null);
    }

    public void displayRecipeInfo(Player player){
        if(this.isTraded()){
            return;
        }
        player.displayClientMessage(SkilletManCoreMod.getInfo("ingredient_info"), false);
        RecipeManager recipeManager = level().getRecipeManager();
        for(BaseCuisineRecipe<?> baseCuisineRecipe : recipeManager.getAllRecipesFor(CDMisc.RT_CUISINE.get())){
            if(this.getOrder().is(baseCuisineRecipe.holderItem)) {
                baseCuisineRecipe.list.forEach(cuisineRecipeMatch -> {
                    ItemStack[] itemStacks = cuisineRecipeMatch.ingredient().getItems();
                    Component info = Component.empty();
                    if(itemStacks.length > 1) {
                        ItemStack refer = itemStacks[0];
                        IngredientConfig.IngredientEntry entry = IngredientConfig.get().getEntry(refer);
                        if(entry != null) {
                            info = Component.literal("[").append(entry.type.get()).append("]").withStyle(entry.type.format);
                        }
                    } else {
                        info = itemStacks[0].getDisplayName();
                    }
                    player.displayClientMessage(info.copy().append(SkilletManCoreMod.getInfo("ingredient_rate")).append("§a [" + cuisineRecipeMatch.min() + " ~ " + cuisineRecipeMatch.max() + "]"), false);
                });
            }
        }
    }

    @Override
    public @NotNull Component getName() {
        return this.customerData == null ? Component.translatable(this.getType().getDescriptionId()) : this.customerData.getTranslation();
    }

    public static abstract class CustomerData{
        protected static final byte BEST = 3;
        protected static final byte MIDDLE = 2;
        protected static final byte BAD = 1;
        protected static final byte NO_FOOD = -1;
        protected static final byte SUBMIT_FOOD = 114;

        public abstract void generateTranslation(SMCLangGenerator generator);
        public abstract void onInteract(ServerPlayer player, Customer self);
        @OnlyIn(Dist.CLIENT)
        public abstract void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodLevel);

        public abstract void handle(ServerPlayer serverPlayer, Customer self, byte interactId);
        public abstract Component getTranslation();

    }

}
