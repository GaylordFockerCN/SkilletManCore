package com.p1nero.smc.entity.custom.npc.start_npc;

import com.p1nero.dialog_lib.api.component.DialogueComponentBuilder;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.effect.SMCEffects;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.p1nero.smc.block.entity.MainCookBlockEntity.*;

public class StartNPCPlus extends StartNPC {
    protected static final EntityDataAccessor<Boolean> IS_WORKING = SynchedEntityData.defineId(StartNPCPlus.class, EntityDataSerializers.BOOLEAN);
    private boolean firstCustomerSummoned = false;//摆锅马上就有客户
    private final List<Customer> customers = new ArrayList<>();
    public static final int PLUS_WORKING_RADIUS = 16;
    private boolean tooFarTipShown = false;
    public static final List<VillagerProfession> PROFESSION_LIST = ForgeRegistries.VILLAGER_PROFESSIONS.getValues().stream().toList();
    public StartNPCPlus(EntityType<? extends StartNPC> entityType, Level level) {
        super(entityType, level);
    }


    public StartNPCPlus(ServerLevel level, BlockPos pos) {
        this(SMCEntities.START_NPC_PLUS.get(), level);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.setSpawnPos(pos);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_WORKING, false);
    }

    public boolean isWorking(){
        return this.getEntityData().get(IS_WORKING);
    }

    public void setWorking(boolean isWorking){
        this.getEntityData().set(IS_WORKING, isWorking);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("is_working", this.getEntityData().get(IS_WORKING));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setWorking(tag.getBoolean("is_working"));
        if(this.getOwner() instanceof ServerPlayer serverPlayer) {
            this.updateWorkingState(serverPlayer);
        }
    }

    /**
     * 客户生成逻辑换到此处
     */
    @Override
    public void tick() {
        super.tick();
        if(this.isGuider()) {
            LivingEntity owner = this.getOwner();
            if (owner instanceof ServerPlayer serverPlayer && owner.isAlive()) {
                if(this.isWorking()) {
                    this.workingTick(serverPlayer);
                    updateWorkingState(serverPlayer);
                }
            }
        }
    }
    public int getDayTime(){
        return (int) (level().getDayTime() / 24000);
    }
    private void workingTick(ServerPlayer owner) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(owner);
        if(!isWorkingTime()){
            setWorking(false);
            return;
        }
        if(this.level() instanceof ServerLevel serverLevel && !DummyEntityManager.getDummyEntities(serverLevel).isEmpty()) {
            for(ServerPlayer player : serverLevel.players()) {
                player.displayClientMessage(SkilletManCoreMod.getInfo("raid_no_work"), true);
            }
            return;
        }
        if(owner.hasEffect(SMCEffects.RUMOR.get())) {
            setWorking(false);
            owner.displayClientMessage(SkilletManCoreMod.getInfo("rumor_no_work"), false);
            return;
        }
        int dayTime = this.getDayTime();
        //第一天后开始生成随机事件
        if(dayTime > 0 && dayTime % 2 == 0 && DataManager.hasAnySpecialEvent(owner) && !DataManager.specialSolvedToday.get(owner)){
            setWorking(false);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("special_event", true);
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), tag), owner);
            owner.serverLevel().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.FIRE_EXTINGUISH, owner.getSoundSource(), 1.0F, 1.0F);
            if (!DataManager.inSpecial.get(owner)) {
                summonSpecial(owner, this.getOnPos(), 15, 20);
                DataManager.inSpecial.put(owner, true);
            }
            return;
        }

        //提示距离
        if(this.position().distanceTo(owner.position()) > PLUS_WORKING_RADIUS) {
            if(!DataManager.inSpecial.get(owner) && !tooFarTipShown) {
                owner.displayClientMessage(Component.literal("[").append(this.getName()).append("] : ").append(SkilletManCoreMod.getInfo("move_too_far_from_npc_plus")), false);
                owner.serverLevel().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.VILLAGER_AMBIENT, owner.getSoundSource(), 1.0F, 1.0F);
                tooFarTipShown = true;
            }
        } else {
            tooFarTipShown = false;
        }

        //生客户
        this.customers.removeIf(customer -> customer == null || customer.isRemoved() || !customer.isAlive());
        if(owner.tickCount % (1200 - smcPlayer.getStage() * 100) == 0 || !firstCustomerSummoned){
            summonCustomer(owner, 6);
        }
    }

    public void updateWorkingState(ServerPlayer serverPlayer) {
        if (this.isWorking()) {
            SMCPlayer.updateWorkingState(true, serverPlayer);
        } else {
            firstCustomerSummoned = false;
            SMCPlayer.updateWorkingState(false, serverPlayer);
            this.clearCustomers();
        }
    }

    public void summonCustomer(ServerPlayer owner, int sizeLimit){
        //生成顾客，30s一只，最多6只
        if(this.customers.size() < sizeLimit) {
            firstCustomerSummoned = true;
            BlockPos spawnPos = getRandomPos(owner, 15, 20, this.getOnPos());
            Customer customer = new Customer(owner, spawnPos.getCenter());
            customer.setHomePos(this.getHomePos());
            customer.setSpawnPos(spawnPos);
            customer.getNavigation().moveTo(customer.getNavigation().createPath(this.getHomePos(), 5), 1.0);
            VillagerProfession profession = PROFESSION_LIST.get(customer.getRandom().nextInt(PROFESSION_LIST.size()));//随机抽个职业，换皮肤好看
            customer.setVillagerData(customer.getVillagerData().setType(VillagerType.byBiome(owner.serverLevel().getBiome(this.getOnPos()))).setProfession(profession));
            this.customers.add(customer);
            owner.serverLevel().addFreshEntity(customer);
        }
    }

    public void clearCustomers(){
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            iterator.next().setTraded(true);//遣散
            iterator.remove();
        }
    }

    @Override
    protected boolean shouldRemoveWithoutMainBlock() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this, name.copy().append(": "));
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);

        if(senderData.getBoolean("special_event")) {
            builder.start(SkilletManCoreMod.getInfo("special_event_ans2"))
                    .addFinalChoice(SkilletManCoreMod.getInfo("special_event_opt1"), (byte) 0)
                    .addFinalChoice(SkilletManCoreMod.getInfo("special_event_opt3"), (byte) 0);
            Minecraft.getInstance().setScreen(builder.build());
            return;
        }

        int moneyRate = (int) smcPlayer.getLevelMoneyRate();
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);

        if (isHired()) {

            if (isWorkingTime()) {
                TreeNode takeMoney = new TreeNode(dialogueComponentBuilder.ans(3), dialogueComponentBuilder.opt(5))
                        .addExecutable((byte) 4);
                TreeNode main = new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(7))
                        .addChild(takeMoney)//全部取出
                        .addLeaf(dialogueComponentBuilder.opt(6, this.getUpgradeNpcNeed()), (byte) 5);//升级

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

            TreeNode talk = new TreeNode(dialogueComponentBuilder.ans(13), dialogueComponentBuilder.opt(26))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(14), dialogueComponentBuilder.opt(27))
                            .addLeaf(dialogueComponentBuilder.opt(2)))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(15), dialogueComponentBuilder.opt(28))
                            .addLeaf(dialogueComponentBuilder.opt(2)))
                    .addChild(new TreeNode(dialogueComponentBuilder.ans(16), dialogueComponentBuilder.opt(29))
                            .addLeaf(dialogueComponentBuilder.opt(2)));

            int stage = senderData.getInt("player_stage");

            if (stage >= 1) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(14, (2000 * moneyRate)), (byte) 14);
            }

            if (stage >= 2) {
                foodBuyer.addLeaf(dialogueComponentBuilder.opt(15, (5000 * moneyRate)), (byte) 15);
            }

            TreeNode root = new TreeNode(dialogueComponentBuilder.ans(1));


            root.addChild(ticketExchange)
                    .addChild(foodBuyer);
            root.addLeaf(dialogueComponentBuilder.opt(25), (byte) 29);
            root.addChild(talk);
            root.addLeaf(dialogueComponentBuilder.opt(2), (byte) 3); //告辞

            builder.setAnswerRoot(root);
        }

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {

        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        if(interactionID == 29) {
            if(!this.isWorkingTime()) {
                player.displayClientMessage(Component.literal("[").append(this.getName()).append("] : ").append(SkilletManCoreMod.getInfo("npc_plus_need_rest")), false);
                this.setConversingPlayer(null);
                return;
            }
            this.setWorking(!this.isWorking());
            this.updateWorkingState(player);
            this.setConversingPlayer(null);
            return;
        }

        super.handleNpcInteraction(player, interactionID);
    }

}
