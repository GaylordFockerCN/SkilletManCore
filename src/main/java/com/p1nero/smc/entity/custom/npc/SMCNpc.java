package com.p1nero.smc.entity.custom.npc;

import com.mojang.serialization.Dynamic;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.entity.ai.behavior.VillagerTasks;
import com.p1nero.smc.entity.ai.goal.NpcDialogueGoal;
import com.p1nero.smc.entity.api.HomePointEntity;
import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class SMCNpc extends Villager implements HomePointEntity, NpcDialogue, Merchant {
    @Nullable
    private Player conversingPlayer;
    @Nullable
    private Player tradingPlayer;

    protected static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(SMCNpc.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<BlockPos> SPAWN_POS = SynchedEntityData.defineId(SMCNpc.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SMCNpc.class, EntityDataSerializers.OPTIONAL_UUID);//服务的玩家
    public SMCNpc(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        setHomePos(getOnPos());
    }

    public void setSpawnPos(BlockPos pos) {
        getEntityData().set(SPAWN_POS, pos);
    }

    public BlockPos getSpawnPos() {
        return getEntityData().get(SPAWN_POS);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(HOME_POS, getOnPos());
        getEntityData().define(SPAWN_POS, getOnPos());
        this.entityData.define(OWNER_UUID, Optional.empty());
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
        this.getEntityData().set(HOME_POS, new BlockPos(tag.getInt("home_pos_x"), tag.getInt("home_pos_y"), tag.getInt("home_pos_z")));
        this.getEntityData().set(SPAWN_POS, new BlockPos(tag.getInt("spawn_pos_x"), tag.getInt("spawn_pos_y"), tag.getInt("spawn_pos_z")));
        if (tag.hasUUID("owner_uuid")) {
            this.setOwnerUUID(tag.getUUID("owner_uuid"));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("home_pos_x", this.getEntityData().get(HOME_POS).getX());
        tag.putInt("home_pos_y", this.getEntityData().get(HOME_POS).getY());
        tag.putInt("home_pos_z", this.getEntityData().get(HOME_POS).getZ());
        tag.putInt("spawn_pos_x", this.getEntityData().get(SPAWN_POS).getX());
        tag.putInt("spawn_pos_y", this.getEntityData().get(SPAWN_POS).getY());
        tag.putInt("spawn_pos_z", this.getEntityData().get(SPAWN_POS).getZ());
        if (this.getOwnerUUID() != null) {
            tag.putUUID("owner_uuid", this.getOwnerUUID());
        }
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

    @Override
    public void setHomePos(BlockPos homePos) {
        getEntityData().set(HOME_POS, homePos);
    }

    @Override
    public BlockPos getHomePos() {
        return getEntityData().get(HOME_POS);
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 114514f)
                .build();
    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount % 20 == 0){
            this.onSecond();
        }
    }

    public void onSecond() {

    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.playSound(SoundEvents.VILLAGER_AMBIENT);
        if (player instanceof ServerPlayer serverPlayer) {
            this.lookAt(player, 180.0F, 180.0F);
            if (this.getConversingPlayer() == null) {
                CompoundTag compoundTag = new CompoundTag();
                SMCArchiveManager.BIOME_PROGRESS_DATA.toNbt(compoundTag);
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), getDialogData(compoundTag, serverPlayer)), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    protected CompoundTag getDialogData(CompoundTag compoundTag, ServerPlayer serverPlayer) {
        return compoundTag;
    }

    public void explodeAndDiscard(){
        if(level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 10, 0.0D, 0.1D, 0.0D, 0.01);
            serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1);
        }
        discard();
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float value) {
        if(value == 1145) {
            return super.hurt(source, 1);
        }
        if(source.isCreativePlayer()) {
            return super.hurt(source, value);
        }
        return false;
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new AttemptToGoHomeGoal<>(this, 1.0));
        this.goalSelector.addGoal(0, new NpcDialogueGoal<>(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    public float getHomeRadius() {
        return 3;
    }

    @Override
    public void setConversingPlayer(@Nullable Player conversingPlayer) {
        this.conversingPlayer = conversingPlayer;
    }

    @Override
    public @Nullable Player getConversingPlayer() {
        return conversingPlayer;
    }

    /**
     * 默认不做处理
     */
    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        setConversingPlayer(null);
    }

    /**
     * 开始交易
     * 需要改变交易表则去重写 {@link #getOffers()}
     */
    public void startTrade(ServerPlayer serverPlayer){
        setTradingPlayer(serverPlayer);
        openTradingScreen(serverPlayer, Component.empty(), 5);
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        tradingPlayer = player;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        return new MerchantOffers();
    }

    @Override
    public void overrideOffers(@NotNull MerchantOffers merchantOffers) {

    }

    @Override
    public void notifyTrade(@NotNull MerchantOffer merchantOffer) {

    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack itemStack) {

    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public @NotNull SoundEvent getNotifyTradeSound() {
        return SoundEvents.EXPERIENCE_ORB_PICKUP;
    }

}
