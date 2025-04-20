package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.SyncArchivePacket;
import com.p1nero.smc.network.packet.clientbound.OpenStartGuideScreenPacket;
import com.p1nero.smc.network.packet.clientbound.SyncUuidPacket;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.events.FoodEatenEvent;
import net.blay09.mods.waystones.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class PlayerEventListener {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().dimension().equals(SMCDimension.P_SKY_ISLAND_LEVEL_KEY)) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4));
        }
    }

    @SubscribeEvent
    public static void onPlayerAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer && event.getAdvancement().getId().getNamespace().equals(SkilletManCoreMod.MOD_ID)) {
            SMCPlayer.addMoney(200, serverPlayer);
            serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_CELEBRATE, serverPlayer.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            //同步客户端数据
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncArchivePacket(SMCArchiveManager.toNbt()), serverPlayer);
            SMCCapabilityProvider.syncPlayerDataToClient(serverPlayer);
            //防止重进后boss的uuid不同
            SMCBoss.SERVER_BOSSES.forEach(((uuid, integer) -> PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncUuidPacket(uuid, integer), serverPlayer)));
            SMCAdvancementData.finishAdvancement(SkilletManCoreMod.MOD_ID, serverPlayer);

            SMCPlayer.updateWorkingState(false, serverPlayer);//重置上班状态，防止假性上班（

            if (!DataManager.firstJoint.get(serverPlayer)) {
                DataManager.firstJoint.put(serverPlayer, true);
                CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack().withPermission(2);
                Objects.requireNonNull(serverPlayer.getServer()).getCommands().performPrefixedCommand(commandSourceStack, "gamerule keepInventory true");
                serverPlayer.playSound(SoundEvents.VILLAGER_CELEBRATE);
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenStartGuideScreenPacket(), serverPlayer);
            }
        }

    }

    /**
     * 禁用非锅武器
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide) {
            ItemStack mainHandItem = event.player.getMainHandItem();
            if (!EpicFightCapabilities.getItemStackCapability(mainHandItem).isEmpty() && !event.player.isCreative() && EpicFightCapabilities.getEntityPatch(event.player, PlayerPatch.class).isBattleMode()) {
                if (!(mainHandItem.getItem() instanceof CuisineSkilletItem || mainHandItem.getItem() instanceof SpatulaItem)) {
                    SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(event.player);
                    //stage3才解禁
                    if (smcPlayer.getLevel() < SMCPlayer.STAGE3_REQUIRE) {
                        event.player.drop(mainHandItem.copy(), true);
                        mainHandItem.shrink(1);
                        event.player.displayClientMessage(SkilletManCoreMod.getInfo("no_your_power"), true);
                        SMCAdvancementData.finishAdvancement("no_your_power", ((ServerPlayer) event.player));
                    }
                }
            }
        }
    }

    /**
     * 提示开战斗模式
     */
    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            if (!EpicFightCapabilities.getEntityPatch(serverPlayer, PlayerPatch.class).isBattleMode()) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("please_in_battle_mode"), true);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            //传送石就是重生点
            if (event.getLevel().getBlockState(event.getPos()).is(ModBlocks.waystone)) {
                serverPlayer.setRespawnPosition(serverPlayer.serverLevel().dimension(), event.getEntity().blockPosition(), 0.0F, true, true);
            }

            //和灶王爷对话
            if (event.getLevel().getBlockState(event.getPos()).is(vectorwing.farmersdelight.common.registry.ModBlocks.STOVE.get()) && event.getLevel().getBlockState(event.getPos().below()).is(SMCBlocks.MAIN_COOK_BLOCK.get())) {
                MainCookBlockEntity mainCookBlockEntity = ((MainCookBlockEntity) event.getLevel().getBlockEntity(event.getPos().below()));
                if (mainCookBlockEntity != null && mainCookBlockEntity.isWorking()) {
                    mainCookBlockEntity.onClickStove(serverPlayer, event.getPos().below(), event.getFace(), event.getHand());
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerEatFood(FoodEatenEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer) {
            SMCAdvancementData.finishAdvancement("self_eat", serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    /**
     * 前面的区域以后再来探索吧~
     */
    @SubscribeEvent
    public static void enterBiome(TickEvent.PlayerTickEvent event) {

    }


    @SubscribeEvent
    public static void onPlayerUseItem(LivingEntityUseItemEvent.Start event) {

    }


}
