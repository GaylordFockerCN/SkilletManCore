package com.p1nero.smc.entity.custom.npc.customer;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Customer extends SMCNpc {

    protected static final EntityDataAccessor<Boolean> TRADED = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Float> DISTANCE = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.FLOAT);//距炉子距离
    protected static final EntityDataAccessor<Boolean> IS_SPECIAL = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.BOOLEAN);//是否是隐士，可以给予武林秘籍
    protected static final EntityDataAccessor<Integer> SMC_ID = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.INT);//村民编号，用于区别对话
    protected static final EntityDataAccessor<ItemStack> ORDER = SynchedEntityData.defineId(Customer.class, EntityDataSerializers.ITEM_STACK);//请求的食物

    public static final List<CustomerData> customers = new ArrayList<>();
    public static final List<CustomerData> specialCustomers = new ArrayList<>();

    static {
        specialCustomers.add(new SpecialCustomerData1());
        specialCustomers.add(new SpecialCustomerData2());
        specialCustomers.add(new SpecialCustomerData3());
        specialCustomers.add(new SpecialCustomerData4());
        specialCustomers.add(new SpecialCustomerData5());
        specialCustomers.add(new SpecialCustomerData6());
        specialCustomers.add(new SpecialCustomerData7());
        specialCustomers.add(new SpecialCustomerData8());
        specialCustomers.add(new SpecialCustomerData9());
        specialCustomers.add(new SpecialCustomerData10());
        specialCustomers.add(new SpecialCustomerData11());
        specialCustomers.add(new SpecialCustomerData12());
        specialCustomers.add(new SpecialCustomerData13());
        specialCustomers.add(new SpecialCustomerData14());
        specialCustomers.add(new SpecialCustomerData15());
        specialCustomers.add(new SpecialCustomerData16());
        specialCustomers.add(new SpecialCustomerData17());
        specialCustomers.add(new SpecialCustomerData18());
        specialCustomers.add(new SpecialCustomerData19());
        specialCustomers.add(new SpecialCustomerData20());

        customers.add(new NormalCustomerData1());
        customers.add(new NormalCustomerData2());
        customers.add(new NormalCustomerData3());
        customers.add(new NormalCustomerData4());
        customers.add(new NormalCustomerData5());
        customers.add(new NormalCustomerData6());
    }

    protected CustomerData customerData;
    protected int foodLevel;
    private int dieTimer = 0;
    public Customer(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        customerData = customers.get(0);
    }

    public Customer(Level level, Vec3 pos) {
        this(SMCEntities.CUSTOMER.get(), level);
        this.setPos(pos);
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
        return this.isSpecial() || this.tickCount > 60 * 20;//大于一分钟就提示
    }

    @Override
    public int getTeamColor() {
        if(isSpecial()) {
            return this.tickCount % 0xFFFFFF;//会五颜六色吗
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ORDER, ItemStack.EMPTY);
        this.getEntityData().define(SMC_ID, 0);
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
    public void tick() {
        super.tick();

        if(this.getUnhappyCounter() > 0) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
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

        if(this.tickCount > 20 * 60 * 3) {
            this.discard();
            if(this.getOwner() instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("customer_left"), false);
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(this.getOrder().is(player.getMainHandItem().getItem())) {
            CookedFoodData cookedFoodData = BaseFoodItem.getData(this.getOrder());
            if(cookedFoodData != null){
                int score = cookedFoodData.score;
                if(score >= 90) {
                    this.foodLevel = CustomerData.BEST;
                } else if(score >= 60){
                    this.foodLevel = CustomerData.MIDDLE;
                } else {
                    this.foodLevel = CustomerData.BAD;
                }
            }
        }
        //每五个顾客随机一个隐士
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        if(smcPlayer.getLevel() % 5 == 0) {
            customerData = specialCustomers.get(this.getRandom().nextInt(specialCustomers.size()));
        } else {
            customerData = customers.get(this.getRandom().nextInt(customers.size()));
        }
        customerData = specialCustomers.get(this.getRandom().nextInt(specialCustomers.size()));
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
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);
        this.customerData.getDialogScreen(senderData, builder, dialogueComponentBuilder, senderData.getBoolean("can_submit"), senderData.getInt("food_level"));
        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {

        if(interactionID == CustomerData.BAD) {
            if(!DataManager.firstFoodBad.get(player)){
                //灶王爷不乐意了
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("is_first_food_bad", true);
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCBlockDialoguePacket(this.getHomePos(), tag), player);
                DataManager.firstFoodBad.put(player, true);
            }
        }

        if(interactionID == CustomerData.BEST || interactionID == CustomerData.MIDDLE) {
            SMCPlayer.upgradePlayer(player);//能吃就能升级
        }

        if(interactionID == CustomerData.SUBMIT_FOOD) {
            //提交食物专用的代码
            player.getMainHandItem().shrink(1);
            ItemUtil.addItem(player, SMCRegistrateItems.DIRT_PLATE.asItem(), 1);
            return;
        } else {
            this.customerData.handle(player, this, interactionID);
        }
        super.handleNpcInteraction(player, interactionID);
        this.setTraded(true);//离去
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
        return this.customerData.getTranslation();
    }

    public static abstract class CustomerData{
        protected static final byte BEST = 3;
        protected static final byte MIDDLE = 2;
        protected static final byte BAD = 1;
        protected static final byte NO_FOOD = 0;
        protected static final byte SUBMIT_FOOD = -1;

        public abstract void generateTranslation(SMCLangGenerator generator);
        @OnlyIn(Dist.CLIENT)
        public abstract void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodLevel);

        public abstract void handle(ServerPlayer serverPlayer, Customer self, byte interactId);
        public abstract Component getTranslation();

    }

}
