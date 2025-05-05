package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import com.p1nero.smc.gameasset.skill.SMCSkills;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.SyncArchivePacket;
import com.p1nero.smc.network.packet.clientbound.OpenStartGuideScreenPacket;
import com.p1nero.smc.network.packet.clientbound.SyncUuidPacket;
import com.p1nero.smc.util.ItemUtil;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.events.FoodEatenEvent;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import hungteen.htlib.common.event.events.RaidEvent;
import net.blay09.mods.waystones.block.ModBlocks;
import net.kenddie.fantasyarmor.item.FAItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class PlayerEventListeners {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().dimension().equals(SMCDimension.P_SKY_ISLAND_LEVEL_KEY)) {
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4));
        }
    }

    @SubscribeEvent
    public static void onPlayerAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer && event.getAdvancement().getId().getNamespace().equals(SkilletManCoreMod.MOD_ID)) {
            if(!event.getAdvancement().getId().getPath().contains("recipe")) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("advancement_look_tip"), false);
                SMCPlayer.addMoney(200, serverPlayer);
                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_CELEBRATE, serverPlayer.getSoundSource(), 1.0F, 1.0F);
            }
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
                serverPlayer.playSound(SMCSounds.VILLAGER_YES.get());
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenStartGuideScreenPacket(), serverPlayer);
                serverPlayer.setItemSlot(EquipmentSlot.HEAD, FAItems.THIEF_HELMET.get().getDefaultInstance());
                serverPlayer.setItemSlot(EquipmentSlot.CHEST, FAItems.THIEF_CHESTPLATE.get().getDefaultInstance());

                ItemUtil.addItem(serverPlayer, ModItems.NETHERITE_BACKPACK.get(), 1);
                ItemUtil.addItem(serverPlayer, CDItems.SKILLET.asItem(), 1);
                ItemUtil.addItem(serverPlayer, CDItems.SPATULA.asItem(), 1);
                ItemStack step = new ItemStack(EpicFightItems.SKILLBOOK.get());
                step.getOrCreateTag().putString("skill", EpicFightSkills.STEP.toString());
                ItemStack parrying = new ItemStack(EpicFightItems.SKILLBOOK.get());
                parrying.getOrCreateTag().putString("skill", EpicFightSkills.PARRYING.toString());
                ItemStack technician = new ItemStack(EpicFightItems.SKILLBOOK.get());
                technician.getOrCreateTag().putString("skill", EpicFightSkills.TECHNICIAN.toString());
                ItemStack dodgeDisplay = new ItemStack(EpicFightItems.SKILLBOOK.get());
                dodgeDisplay.getOrCreateTag().putString("skill", SMCSkills.BETTER_DODGE_DISPLAY.toString());
                ItemStack guard = new ItemStack(EpicFightItems.SKILLBOOK.get());
                guard.getOrCreateTag().putString("skill", EpicFightSkills.GUARD.toString());
                ItemUtil.addItem(serverPlayer, step);
                ItemUtil.addItem(serverPlayer, technician);
                ItemUtil.addItem(serverPlayer, dodgeDisplay);
                ItemUtil.addItem(serverPlayer, guard);
                ItemUtil.addItem(serverPlayer, parrying);

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
            ServerPlayerPatch serverPlayerPatch = EpicFightCapabilities.getEntityPatch(event.player, ServerPlayerPatch.class);
            if (!EpicFightCapabilities.getItemStackCapability(mainHandItem).isEmpty() && !event.player.isCreative() && serverPlayerPatch != null && serverPlayerPatch.isBattleMode()) {
                if (!(mainHandItem.getItem() instanceof CuisineSkilletItem || mainHandItem.getItem() instanceof SpatulaItem)) {
                    SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(event.player);
                    //stage2才解禁
                    if (smcPlayer.getLevel() < SMCPlayer.STAGE2_REQUIRE) {
                        event.player.drop(mainHandItem.copy(), true);
                        mainHandItem.shrink(1);
                        event.player.displayClientMessage(SkilletManCoreMod.getInfo("no_your_power"), true);
                        event.player.displayClientMessage(SkilletManCoreMod.getInfo("no_your_power2"), false);
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
        if (event.getSource().getEntity() instanceof Player player) {
            if (!EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode()) {
                if(player.level().isClientSide) {
                    player.displayClientMessage(SkilletManCoreMod.getInfo("please_in_battle_mode", EpicFightKeyMappings.SWITCH_MODE.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.YELLOW)), true);
                }
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

    @SubscribeEvent
    public static void onRaidSuccess(RaidEvent.RaidDefeatedEvent event) {

    }

    @SubscribeEvent
    public static void onRaidLoss(RaidEvent.RaidLostEvent event) {

    }


}
