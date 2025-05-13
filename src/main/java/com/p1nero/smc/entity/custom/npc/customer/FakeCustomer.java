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
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCBlockDialoguePacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FakeCustomer extends SMCNpc {

    protected static final EntityDataAccessor<Boolean> TRADED = SynchedEntityData.defineId(FakeCustomer.class, EntityDataSerializers.BOOLEAN);//是否交易过
    private int dieTimer = 0;
    public FakeCustomer(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    public FakeCustomer(StartNPC owner, Vec3 pos) {
        this(SMCEntities.FAKE_CUSTOMER.get(), owner.level());
        this.setPos(pos);
        this.setOwnerUUID(owner.getUUID());
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(TRADED, false);
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
        BlockEntity blockEntity = level().getBlockEntity(getHomePos());
        return blockEntity instanceof MainCookBlockEntity mainCookBlockEntity ? mainCookBlockEntity : null;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity owner = this.getOwner();
        if(owner != null){
            this.getLookControl().setLookAt(owner);
        }

        if(this.getUnhappyCounter() > 0) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        }

        if(this.getConversingPlayer() == null) {
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

        if(!this.isTraded() && this.position().distanceTo(this.getHomePos().getCenter()) < 5 && this.tickCount > 600) {
            this.setTraded(true);
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

        if(!this.isTraded() && this.tickCount > 1200) {
            this.setTraded(true);
        }

        if(this.isTraded() && this.getOnPos().getCenter().distanceTo(this.getSpawnPos().getCenter()) < 2) {
            this.discard();
        }

    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return InteractionResult.PASS;
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
    public void openDialogueScreen(CompoundTag senderData) {

    }
}
