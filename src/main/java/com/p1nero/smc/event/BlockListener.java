package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.OpenBanPortalScreenPacket;
import com.p1nero.smc.network.packet.clientbound.OpenStartGuideScreenPacket;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class BlockListener {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            Block block = event.getState().getBlock();
            //锅被拆的时候放下的时候通知炉灶核心
            if (block.asItem() instanceof CuisineSkilletItem) {
                BlockPos mainBlockPos = event.getPos().below(2);
                if (serverPlayer.serverLevel().getBlockState(event.getPos().below(2)).is(SMCBlocks.MAIN_COOK_BLOCK.get())) {
                    if (serverPlayer.serverLevel().getBlockEntity(mainBlockPos) instanceof MainCookBlockEntity mainCookBlockEntity) {
                        if (!mainCookBlockEntity.tryBreakSkillet(serverPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
            //禁止破坏核心方块上的炉子
            if (block.equals(ModBlocks.STOVE.get())) {
                if (serverPlayer.serverLevel().getBlockState(event.getPos().below()).is(SMCBlocks.MAIN_COOK_BLOCK.get()) && !serverPlayer.isCreative()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Block block = event.getState().getBlock();
            //锅被放下的时候通知炉灶核心
            if (block.asItem() instanceof CuisineSkilletItem) {
                BlockPos mainBlockPos = event.getPos().below(2);
                if (serverPlayer.serverLevel().getBlockState(event.getPos().below(2)).is(SMCBlocks.MAIN_COOK_BLOCK.get())) {
                    if (serverPlayer.serverLevel().getBlockEntity(mainBlockPos) instanceof MainCookBlockEntity mainCookBlockEntity) {
                        mainCookBlockEntity.onSkilletPlace(serverPlayer);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onPortalBlock(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
        if (!event.getLevel().isClientSide()) {
            for (Player player : event.getLevel().players()) {
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenBanPortalScreenPacket(), ((ServerPlayer) player));
            }
        }
    }
}