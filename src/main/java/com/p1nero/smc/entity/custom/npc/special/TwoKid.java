package com.p1nero.smc.entity.custom.npc.special;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.SMCVillagers;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 俩小儿
 */
public class TwoKid extends SMCNpc implements SpecialNpc {

    protected static final EntityDataAccessor<Boolean> SOLVED = SynchedEntityData.defineId(TwoKid.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Boolean> TALKED = SynchedEntityData.defineId(TwoKid.class, EntityDataSerializers.BOOLEAN);//是否对话过，用来渲染黄色感叹号
    private int dieTimer = 0;
    private int soundTimer = 0;
    private LivingEntity lookTarget = null;

    public TwoKid(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        this.setVillagerData(this.getVillagerData().setProfession(SMCVillagers.KID.get()));
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    public void setLookTarget(LivingEntity lookTarget) {
        this.lookTarget = lookTarget;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return !isTalked();
    }

    @Override
    public int getTeamColor() {
        return 0xfff66d;
    }

    public TwoKid(Player owner, Vec3 pos) {
        this(SMCEntities.TWO_KID.get(), owner.level());
        this.setPos(pos);
        this.setOwnerUUID(owner.getUUID());
    }

    public void setSolved(boolean traded) {
        this.lookTarget = null;
        this.getEntityData().set(SOLVED, traded);
        if (traded && !level().isClientSide) {
            dieTimer = 10;
        }
    }

    public void setTalked(boolean talked) {
        this.getEntityData().set(TALKED, talked);
    }

    @Override
    public boolean isTalked(){
        return this.isSolved() || this.getEntityData().get(TALKED);
    }

    public boolean isSolved() {
        return this.getEntityData().get(SOLVED);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(SOLVED, false);
        this.getEntityData().define(TALKED, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setSolved(tag.getBoolean("solved"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("solved", this.isSolved());
        //不保存是否读过
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
//        villagerBrain.addActivity(Activity.CORE, VillagerTasks.getSMCVillagerCorePackage(this));
//        villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    @Nullable
    public MainCookBlockEntity getHomeBlockEntity() {
        BlockEntity blockEntity = level().getBlockEntity(getHomePos());
        return blockEntity instanceof MainCookBlockEntity mainCookBlockEntity ? mainCookBlockEntity : null;
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide){
            return;
        }
        if(lookTarget == null) {
            if(!this.isSolved()){
                lookTarget = getNearestTwoKid();
                if(lookTarget != null){
                    ((TwoKid)lookTarget).lookTarget = this;
                    this.getLookControl().setLookAt(lookTarget);
                    ((TwoKid)lookTarget).getLookControl().setLookAt(this);
                }
            }
        } else {
            this.getLookControl().setLookAt(lookTarget);
        }
        if (this.getConversingPlayer() == null && this.lookTarget == null) {
            this.getNavigation().moveTo(this.getNavigation().createPath(this.isSolved() ? this.getSpawnPos() : this.getHomePos(), 3), 1.0F);
        }

        if(soundTimer > 0 && tickCount % 5 == 0){
            soundTimer--;
            this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    public TwoKid getNearestTwoKid(){
        for(TwoKid twoKid : EntityUtil.getNearByEntities(TwoKid.class, this, 10)){
            return twoKid;
        }
        return null;
    }
    @Override
    public void onSecond() {
        super.onSecond();
        if (this.dieTimer > 0) {
            this.dieTimer--;
            if (this.dieTimer <= 0) {
                this.explodeAndDiscard();//直接消失，无需多言
            }
        }

        if (this.isSolved() && this.getOnPos().getCenter().distanceTo(this.getSpawnPos().getCenter()) < 2) {
            this.explodeAndDiscard();
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
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable(this.getType().getDescriptionId());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(this.isSolved() || player!= this.getOwner()) {
            return InteractionResult.PASS;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            this.setTalked(true);
            if(this.lookTarget instanceof TwoKid twoKid) {
                twoKid.setTalked(true);
            }

            this.soundTimer = 10;
            if (this.getConversingPlayer() == null) {
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), new CompoundTag()), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {

        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);

        DialogueComponentBuilder dBuilder = new DialogueComponentBuilder(this);
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null){
            return;
        }
        builder.setAnswerRoot(new TreeNode(dBuilder.ans(0, localPlayer.getGameProfile().getName()))
                .execute((byte) 2)
                .addChild(new TreeNode(dBuilder.ans(1), dBuilder.opt(0))
                        .execute((byte) 2)
                        .addChild(new TreeNode(dBuilder.ans(2), dBuilder.opt(0))
                                .execute((byte) 2)
                                .addChild(new TreeNode(dBuilder.ans(3), dBuilder.opt(0))
                                        .execute((byte) 2)
                                        .addChild(new TreeNode(dBuilder.ans(4), dBuilder.opt(0))
                                                .execute((byte) 1)
                                                .addChild(new TreeNode(dBuilder.ans(5), dBuilder.opt(1))
                                                        .execute((byte) 2)
                                                        .addLeaf(dBuilder.opt(-1), (byte) 3)//好了小朋友
                                                        .addLeaf(dBuilder.opt(-2), (byte) 3)//玩够了该把锅还我了
                                                )
                                                .addChild(new TreeNode(dBuilder.ans(5), dBuilder.opt(2))
                                                        .execute((byte) 2)
                                                        .addLeaf(dBuilder.opt(-1), (byte) 3)//好了小朋友
                                                        .addLeaf(dBuilder.opt(-2), (byte) 3)//玩够了该把锅还我了
                                                )
                                                .addChild(new TreeNode(dBuilder.ans(5), dBuilder.opt(3))
                                                        .execute((byte) 2)
                                                        .addLeaf(dBuilder.opt(-1), (byte) 3)//好了小朋友
                                                        .addLeaf(dBuilder.opt(-2), (byte) 3)//玩够了该把锅还我了
                                                )
                                                .addChild(new TreeNode(dBuilder.ans(5), dBuilder.opt(4))
                                                        .execute((byte) 2)
                                                        .addLeaf(dBuilder.opt(-1), (byte) 3)//好了小朋友
                                                        .addLeaf(dBuilder.opt(-2), (byte) 3)//玩够了该把锅还我了
                                                )
                                        )))));

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }

    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if(interactionID == 0) {
            this.setConversingPlayer(null);
            this.setLookTarget(null);
            return;
        }
        if(interactionID == 1) {
            if(this.lookTarget instanceof TwoKid twoKid) {
                twoKid.setLookTarget(player);
            }
            this.setLookTarget(player);
            this.soundTimer = 10;
            return;
        }
        if(interactionID == 2){
            this.soundTimer = 10;
            return;
        }
        TwoKid anotherTwoKid = this.getNearestTwoKid();
        if(anotherTwoKid != null) {
            anotherTwoKid.setSolved(true);
            anotherTwoKid.setConversingPlayer(null);
        }
        this.setSolved(true);
        SMCAdvancementData.finishAdvancement("two_kid", player);
        SMCPlayer.levelUPPlayer(player);
        SMCPlayer.addMoney(1600, player);
        DataManager.inSpecial.put(player, false);
        DataManager.specialEvent2Solved.put(player, true);
        DataManager.specialSolvedToday.put(player, true);
        this.setConversingPlayer(null);
    }
}
