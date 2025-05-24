package com.p1nero.smc.entity.custom.npc.start_npc;

import com.mao.barbequesdelight.content.item.SeasoningItem;
import com.mao.barbequesdelight.init.data.BBQTagGen;
import com.mao.barbequesdelight.init.food.BBQSeasoning;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class StartNPCBBQ extends StartNPC {
    protected static final EntityDataAccessor<Boolean> IS_WORKING = SynchedEntityData.defineId(StartNPCBBQ.class, EntityDataSerializers.BOOLEAN);
    private boolean firstCustomerSummoned = false;//摆锅马上就有客户
    private final List<Customer> customers = new ArrayList<>();
    public static final int PLUS_WORKING_RADIUS = 16;
    private boolean tooFarTipShown = false;
    public static final List<VillagerProfession> PROFESSION_LIST = ForgeRegistries.VILLAGER_PROFESSIONS.getValues().stream().toList();

    public StartNPCBBQ(EntityType<? extends StartNPC> entityType, Level level) {
        super(entityType, level);
    }


    public StartNPCBBQ(Player player, Vec3 pos) {
        this(SMCEntities.START_NPC_BBQ.get(), player.level());
        this.setOwner(player);
        this.setPos(pos);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(IS_WORKING, false);
    }

    public boolean isWorking() {
        return this.getEntityData().get(IS_WORKING);
    }

    public void setWorking(boolean isWorking) {
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
//        if(this.getOwner() instanceof ServerPlayer serverPlayer) {
//            this.updateWorkingState(serverPlayer);
//        }
    }

    public int getDayTime() {
        return (int) (level().getDayTime() / 24000);
    }

    @Override
    protected boolean shouldRemoveWithoutMainBlock() {
        return false;
    }

    @Override
    public boolean isWorkingTime() {
        return !super.isWorkingTime();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer && this.getOwner() == serverPlayer) {
            return super.mobInteract(player, hand);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this, name.copy().append(": "));
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
        int moneyRate = (int) smcPlayer.getLevelMoneyRate();

        TreeNode foodBuyer = new TreeNode(ans(4), opt(2))
                .addLeaf(opt(4, (2000 * moneyRate)), (byte) 1)//配菜大礼包
                .addLeaf(opt(5, (1000 * moneyRate)), (byte) 2);//调料大礼包

        TreeNode talk = new TreeNode(ans(1), opt(0))
                .addLeaf(opt(3));
        TreeNode work;
        if (!isWorkingTime()) {
            work = new TreeNode(ans(2), opt(1))
                    .addLeaf(opt(3));
        } else {
            work = new TreeNode(ans(3), opt(1))
                    .addLeaf(opt(3));
        }

        TreeNode root = new TreeNode(ans(0));
        root.addChild(talk);
        root.addChild(foodBuyer);
        root.addChild(work);
        root.addLeaf(opt(3));

        builder.setAnswerRoot(root);

        if (!builder.isEmpty()) {
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        int moneyRate = (int) smcPlayer.getLevelMoneyRate();
        if (interactionID == 1) {
            List<ItemStack> sides = ForgeRegistries.ITEMS.getValues().stream().filter(item ->
                    item.getDefaultInstance().is(ForgeTags.SALAD_INGREDIENTS_CABBAGE)
                            || item.getDefaultInstance().is(ForgeTags.VEGETABLES_ONION)
                            || item.getDefaultInstance().is(BBQTagGen.FRUITS)).map(Item::getDefaultInstance).toList();
            ItemUtil.tryAddRandomItem(player, sides, 1000 * moneyRate, 10);
        }
        if (interactionID == 2) {
            ItemUtil.tryAddRandomItem(player, Arrays.stream(BBQSeasoning.values()).map(bbqSeasoning -> bbqSeasoning.item.get().getDefaultInstance()).toList(), 1000 * moneyRate, 10);
        }
        this.setConversingPlayer(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getType().getDescriptionId());
    }

}
