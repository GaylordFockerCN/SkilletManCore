package com.p1nero.smc.entity.custom.npc.special;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.SMCVillagers;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 毛贼
 */
public class Thief1 extends SMCNpc implements SpecialNpc {

    protected static final EntityDataAccessor<Boolean> SOLVED = SynchedEntityData.defineId(Thief1.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Boolean> TALKED = SynchedEntityData.defineId(Thief1.class, EntityDataSerializers.BOOLEAN);//是否对话过，用来渲染黄色感叹号
    private int dieTimer = 0;
    private Thief2 thief2;

    public Thief1(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        this.setVillagerData(this.getVillagerData().setProfession(SMCVillagers.THIEF1.get()));
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return !isTalked();
    }

    @Override
    public int getTeamColor() {
        return 0xfff66d;
    }

    public Thief1(Player owner, Vec3 pos) {
        this(SMCEntities.THIEF1.get(), owner.level());
        this.setPos(pos);
        this.setOwnerUUID(owner.getUUID());
    }

    public void setSolved(boolean traded) {
        this.getEntityData().set(SOLVED, traded);
        if (traded && !level().isClientSide) {
            dieTimer = 10;
        }
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 114514f)
                .build();
    }

    public void setTalked(boolean talked) {
        this.getEntityData().set(TALKED, talked);
    }

    @Override
    public boolean isTalked() {
        return this.isSolved() || this.getEntityData().get(TALKED);
    }

    public boolean isSolved() {
        return this.getEntityData().get(SOLVED);
    }

    public void setThief2(Thief2 thief2) {
        this.thief2 = thief2;
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
        villagerBrain.addActivity(Activity.CORE, VillagerTasks.getSMCVillagerCorePackage(this));
        villagerBrain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    @Nullable
    public MainCookBlockEntity getHomeBlockEntity() {
        BlockEntity blockEntity = level().getBlockEntity(getHomePos());
        return blockEntity instanceof MainCookBlockEntity mainCookBlockEntity ? mainCookBlockEntity : null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getConversingPlayer() == null) {
            this.getNavigation().moveTo(this.getNavigation().createPath(this.isSolved() ? this.getSpawnPos() : this.getHomePos(), 3), 1.0F);
        }

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
        if (this.isSolved() || (player != this.getOwner() && !player.isCreative())) {
            return InteractionResult.PASS;
        }
        if (!level().isClientSide) {
            this.setTalked(true);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);
        builder.setAnswerRoot(new TreeNode(dialogueComponentBuilder.ans(0))
                .addExecutable((byte) 2)
                .addChild(new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(0))
                        .addExecutable((byte) 2)
                        .addLeaf(dialogueComponentBuilder.opt(1))
                        .addChild(new TreeNode(dialogueComponentBuilder.ans(2), dialogueComponentBuilder.opt(2))
                                .addExecutable((byte) 2)
                                .addLeaf(dialogueComponentBuilder.opt(3), (byte) 1))));
        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if (interactionID == 2) {
            this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
            return;
        }
        if (interactionID == 1) {
            SMCPlayer.addMoney(3200, player);
            this.setSolved(true);
            if (this.thief2 == null || !this.thief2.isAlive() || this.thief2.isSolved()) {
                SMCAdvancementData.finishAdvancement("thief", player);
                DataManager.inSpecial.put(player, false);
                DataManager.specialEvent3Solved.put(player, true);
                DataManager.specialSolvedToday.put(player, true);
                SMCPlayer.levelUPPlayer(player);
            }
        }
        this.setConversingPlayer(null);
    }
}
