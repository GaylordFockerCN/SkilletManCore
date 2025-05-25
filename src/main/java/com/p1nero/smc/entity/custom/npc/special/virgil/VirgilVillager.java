package com.p1nero.smc.entity.custom.npc.special.virgil;

import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class VirgilVillager extends Vindicator implements NpcDialogue {

    protected ServerBossEvent bossInfo;
    protected static final EntityDataAccessor<Boolean> TALKED = SynchedEntityData.defineId(VirgilVillager.class, EntityDataSerializers.BOOLEAN);//是否对话过，用来渲染黄色感叹号
    protected static final EntityDataAccessor<Boolean> FIGHTING = SynchedEntityData.defineId(VirgilVillager.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(VirgilVillager.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(VirgilVillager.class, EntityDataSerializers.OPTIONAL_UUID);//服务的玩家

    @Nullable
    private Player conversingPlayer;

    public VirgilVillager(EntityType<? extends Vindicator> entityType, Level level) {
        super(entityType, level);
        bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    }

    public VirgilVillager(Player owner, Vec3 pos) {
        this(SMCEntities.VIRGIL_VILLAGER.get(), owner.level());
        this.setPos(pos);
        this.setOwner(owner);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossInfo.removePlayer(player);
    }

    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance instance) {
        this.setItemInHand(InteractionHand.MAIN_HAND, SMCRegistrateItems.IRON_SKILLET_LEVEL5.asStack());
        this.setItemInHand(InteractionHand.OFF_HAND, SMCRegistrateItems.IRON_SKILLET_LEVEL5.asStack());
    }

    @Override
    public void applyRaidBuffs(int p_34079_, boolean p_34080_) {

    }

    @Override
    public boolean isCurrentlyGlowing() {
        return !isTalked();
    }

    @Override
    public int getTeamColor() {
        return 0xfff66d;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TALKED, false);
        getEntityData().define(FIGHTING, false);
        getEntityData().define(SITTING, true);
        this.getEntityData().define(OWNER_UUID, Optional.empty());
    }
    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            if(uuid != null){
                Player player = this.level().getPlayerByUUID(uuid);
                if(player == null){
                    if(this.level() instanceof ServerLevel serverLevel){
                        return serverLevel.getEntity(uuid) instanceof LivingEntity livingEntity ? livingEntity : null;
                    }
                } else {
                    return player;
                }
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setOwner(LivingEntity livingEntity) {
        this.setOwnerUUID(livingEntity.getUUID());
    }

    public void setOwnerUUID(@Nullable UUID pUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(pUuid));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(FIGHTING, tag.getBoolean("fighting"));
        if(isFighting()){
            setSitting(false);
            setTalked(true);
        }
        if (tag.hasUUID("owner_uuid")) {
            this.setOwnerUUID(tag.getUUID("owner_uuid"));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("fighting", this.getEntityData().get(FIGHTING));
        if (this.getOwnerUUID() != null) {
            tag.putUUID("owner_uuid", this.getOwnerUUID());
        }
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.MAX_HEALTH, 280.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    public boolean isFighting() {
        return this.getEntityData().get(FIGHTING);
    }

    public void setFighting(boolean fighting) {
        this.getEntityData().set(FIGHTING, fighting);
    }

    public boolean isTalked() {
        return this.getEntityData().get(TALKED);
    }

    public void setTalked(boolean talked) {
        this.getEntityData().set(TALKED, talked);
    }

    public boolean isSitting() {
        return this.getEntityData().get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.getEntityData().set(SITTING, sitting);
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float v) {
        if (!isFighting()) {
            return false;
        }
        return super.hurt(damageSource, v);
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);
        if (damageSource.getEntity() instanceof ServerPlayer player) {
            setFinished(player);
            if(this.getOwner() instanceof ServerPlayer player1) {
                setFinished(player1);
            }
        }
    }

    public void setFinished(ServerPlayer player) {
        player.removeEffect(MobEffects.BAD_OMEN);
        SMCAdvancementData.finishAdvancement("virgil", player);
        SMCPlayer.addMoney(20000, player);
        DataManager.inSpecial.put(player, false);
        DataManager.specialEvent4Solved.put(player, true);
        DataManager.specialSolvedToday.put(player, true);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if(!level().isClientSide){
            this.setTalked(true);
        }
        if (isFighting()) {
            return InteractionResult.PASS;
        }
        this.playSound(SoundEvents.VILLAGER_AMBIENT);
        if (player instanceof ServerPlayer serverPlayer && (player == this.getOwner() || player.isCreative())) {
            if (this.getConversingPlayer() == null) {
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), new CompoundTag()), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public void tick() {
        super.tick();

        //固定旋转，老实坐好
        if (this.isSitting()) {
            float yRot = this.getYRot();
            this.setYRot(yRot);
            this.setYBodyRot(yRot);
            this.setYHeadRot(yRot);
        } else if(this.getConversingPlayer() != null) {
            this.getLookControl().setLookAt(this.getConversingPlayer());
        }
        if (!this.isFighting()) {
            this.setTarget(null);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true, livingEntity -> this.isFighting()));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        if(getAmbientSound() != null) {
            level().playLocalSound(getX(), getY(), getZ(), getAmbientSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        builder.start(0)
                .addChoice(0, 1)
                .addChoice(1, 2)
                .thenExecute((byte) 1)//起身
                .addChoice(2, 3)
                .thenExecute((byte) 2)//鼓点1
                .addChoice(3, 4)
                .thenExecute((byte) 3)//鼓点2
                .addChoice(4, 5)
                .thenExecute((byte) 4)//鼓点3
                .addFinalChoice(5, (byte) 5);//开打
        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if(interactionID == 5) {
            this.setFighting(true);
        }
        if(interactionID == 4) {
            level().playSound(null, player.getX(), player.getY() + 0.75, player.getZ(), SMCSounds.DRUMBEAT_3.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }

        if(interactionID == 3) {
            level().playSound(null, player.getX(), player.getY() + 0.75, player.getZ(), SMCSounds.DRUMBEAT_2.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }

        if(interactionID == 2) {
            level().playSound(null, player.getX(), player.getY() + 0.75, player.getZ(), SMCSounds.DRUMBEAT_1.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }

        if(interactionID == 1) {
            this.setSitting(false);
            player.serverLevel().sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 50, 0, 0.1, 0, 0.1);
            return;
        }
        this.setConversingPlayer(null);
    }

    @Override
    public void setConversingPlayer(@Nullable Player player) {
        this.conversingPlayer = player;
    }

    @Nullable
    @Override
    public Player getConversingPlayer() {
        return conversingPlayer;
    }
}
