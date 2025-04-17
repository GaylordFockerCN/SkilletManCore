package com.p1nero.smc.block.entity;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlockEntities;
import com.p1nero.smc.block.custom.INpcDialogueBlock;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.custom.npc.start_npc.Customer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCBlockDialoguePacket;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;

public class MainCookBlockEntity extends BlockEntity implements INpcDialogueBlock {
    @Nullable
    private StartNPC startNPC;
    private boolean isWorking;
    private final int workingRadius = 5;
    private List<Customer> customers;

    public MainCookBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMCBlockEntities.MAIN_COOK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public boolean isWorking() {
        return isWorking;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if(level.isClientSide) {
            return;
        }
        if (t instanceof MainCookBlockEntity mainCookBlockEntity) {
            if (mainCookBlockEntity.startNPC == null) {
                int offset = 3;
                StartNPC startNPC = level.getNearestEntity(StartNPC.class, TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ(), new AABB(pos.offset(offset, offset, offset), pos.offset(-offset, -offset, -offset)));
                if (startNPC != null) {
                    //互相通知
                    mainCookBlockEntity.startNPC = startNPC;
                    startNPC.setHomePos(pos);
                }
            }

            if (mainCookBlockEntity.startNPC != null && mainCookBlockEntity.startNPC.isGuider()) {
                LivingEntity owner = mainCookBlockEntity.startNPC.getOwner();
                if (owner instanceof ServerPlayer serverPlayer && owner.isAlive()) {
                    mainCookBlockEntity.isWorking = mainCookBlockEntity.checkWorking();
                    if (mainCookBlockEntity.isWorking) {
                        SMCPlayer.updateWorkingState(true, serverPlayer);
                        //抓回来上班
                        if(pos.getCenter().distanceTo(serverPlayer.position()) > mainCookBlockEntity.workingRadius && !mainCookBlockEntity.canPlayerLeave(serverPlayer)) {
                            serverPlayer.teleportTo(mainCookBlockEntity.startNPC.getX(), mainCookBlockEntity.startNPC.getY(), mainCookBlockEntity.startNPC.getZ());
                            serverPlayer.playSound(SoundEvents.VILLAGER_NO);
                            CompoundTag tag = new CompoundTag();
                            tag.putBoolean("is_catching_escaping_player", true);
                            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCBlockDialoguePacket(pos, tag), serverPlayer);
                        }
                        //生成客户 TODO


                    } else {
                        SMCPlayer.updateWorkingState(false, serverPlayer);
                    }
                }
            }
        }
    }

    /**
     * 点击顶上的炉子的时候
     * 对话，发起进阶挑战以继续迎客
     */
    public void onClickStove(ServerPlayer serverPlayer, BlockPos below, Direction face, InteractionHand hand) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int stage = smcPlayer.getStage();

    }

    public void onNPCFinishTrade(Customer customer){
        this.customers.remove(customer);
    }

    /**
     * 判断是否开始营业
     */
    public boolean checkWorking() {
        if (this.level == null) {
            return false;
        }
        long currentTime = this.level.getDayTime();
        if(currentTime > 600 && currentTime < 12700) {
            return this.level.getBlockState(this.getBlockPos().above()).is(ModBlocks.STOVE.get()) && this.level.getBlockState(this.getBlockPos().above(2)).getBlock().asItem() instanceof CuisineSkilletItem;
        }
        return false;
    }

    public boolean canPlayerLeave(ServerPlayer serverPlayer) {
        //TODO 在试炼则可以离开
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(null, ModBlocks.STOVE.get().getName().withStyle(ChatFormatting.YELLOW));

        if(senderData.getBoolean("is_catching_escaping_player")) {
            builder.start(SkilletManCoreMod.getInfo("cannot_left_customers"))
                    .addFinalChoice(SkilletManCoreMod.getInfo("alr"), (byte) 0)
                    .addFinalChoice(SkilletManCoreMod.getInfo("god_stove_talk"), (byte) 0);
        }

        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(Player player, byte interactionID) {

    }
}
