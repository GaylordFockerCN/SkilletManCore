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
import com.p1nero.smc.item.custom.skillets.DiamondSkilletItem;
import com.p1nero.smc.item.custom.skillets.GoldenSkilletItem;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Min;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
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
import net.minecraft.world.entity.ai.Brain;
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
 * 河神
 */
public class HeShen extends SMCNpc implements SpecialNpc {

    protected static final EntityDataAccessor<Boolean> SOLVED = SynchedEntityData.defineId(HeShen.class, EntityDataSerializers.BOOLEAN);//是否交易过
    protected static final EntityDataAccessor<Boolean> TALKED = SynchedEntityData.defineId(HeShen.class, EntityDataSerializers.BOOLEAN);//是否对话过，用来渲染黄色感叹号
    private int dieTimer = 0;

    public HeShen(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
        this.setVillagerData(this.getVillagerData().setProfession(SMCVillagers.HE_SHEN.get()));
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return !isTalked();
    }

    @Override
    public int getTeamColor() {
        return 0xfff66d;
    }

    public HeShen(Player owner, Vec3 pos) {
        this(SMCEntities.HE_SHEN.get(), owner.level());
        this.setPos(pos);
        this.setOwnerUUID(owner.getUUID());
    }

    public void setSolved(boolean traded) {
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
        return this.getEntityData().get(TALKED);
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
        if(this.isSolved() || (player != this.getOwner() && !player.isCreative())) {
            return InteractionResult.PASS;
        }
        if(!level().isClientSide){
            this.setTalked(true);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {

        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);

        DialogueComponentBuilder dialogueComponentBuilder = new DialogueComponentBuilder(this);

        TreeNode diamond1 = new TreeNode(dialogueComponentBuilder.ans(3), dialogueComponentBuilder.opt(2))
                .addExecutable((byte) 3)
                .addLeaf(dialogueComponentBuilder.opt(-3));

        TreeNode diamond2 = new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(2))
                .addExecutable((byte) 1)
                .addLeaf(dialogueComponentBuilder.opt(-1));

        TreeNode root = new TreeNode(dialogueComponentBuilder.ans(0))
                .addChild(new TreeNode(dialogueComponentBuilder.ans(1), dialogueComponentBuilder.opt(0))//普通
                        .addExecutable((byte) 1)
                        .addLeaf(dialogueComponentBuilder.opt(-1)))
                .addChild(new TreeNode(dialogueComponentBuilder.ans(2), dialogueComponentBuilder.opt(1))//黄金
                        .addExecutable((byte) 2)
                        .addLeaf(dialogueComponentBuilder.opt(-2)));

        LocalPlayer player = Minecraft.getInstance().player;

        if(player != null && player.getInventory().hasAnyMatching(itemStack -> itemStack.getItem() instanceof DiamondSkilletItem)) {
            root.addChild(diamond2);
        } else {
            root.addChild(diamond1);
        }

        root.addChild(new TreeNode(dialogueComponentBuilder.ans(3), dialogueComponentBuilder.opt(3))//都是
                        .addExecutable((byte) 4)
                        .addLeaf(dialogueComponentBuilder.opt(-3)));

        builder.setAnswerRoot(root);

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }

    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        this.playSound(SoundEvents.VILLAGER_AMBIENT, this.getSoundVolume(), this.getVoicePitch());
        if(interactionID == 0) {
            this.setConversingPlayer(null);
            return;
        }
        if(interactionID == 1) {
            SMCPlayer.addMoney(1600, player);
            SMCPlayer.levelUPPlayer(player);
        } else if(interactionID == 2) {
            SMCPlayer.consumeMoney(1600, player);
            ItemUtil.addItem(player, SMCRegistrateItems.GOLDEN_SKILLET_V3.asStack(), true);
            SMCPlayer.levelUPPlayer(player);
        } else if(interactionID == 3 || interactionID == 4) {
            SMCPlayer.consumeMoney(1600, player);
            ItemUtil.searchAndConsumeItem(player, item -> item instanceof CuisineSkilletItem, 1);
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1, 1);
        }
        this.setSolved(true);
        SMCAdvancementData.finishAdvancement("he_shen", player);
        DataManager.inSpecial.put(player, false);
        DataManager.specialEvent1Solved.put(player, true);
        DataManager.specialSolvedToday.put(player, true);
        this.setConversingPlayer(null);
    }
}
