package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.SyncArchivePacket;
import com.p1nero.smc.network.packet.clientbound.SyncUuidPacket;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class PlayerEventListener {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        if(event.getEntity().level().dimension().equals(SMCDimension.P_SKY_ISLAND_LEVEL_KEY)){
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer){
            //同步客户端数据
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncArchivePacket(SMCArchiveManager.toNbt()), serverPlayer);
            //防止重进后boss的uuid不同
            SMCBoss.SERVER_BOSSES.forEach(((uuid, integer) -> PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new SyncUuidPacket(uuid, integer), serverPlayer)));
        } else {
            //单机世界的同步数据
            if(SMCArchiveManager.isAlreadyInit()){
                PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new SyncArchivePacket(SMCArchiveManager.toNbt()));
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event){

    }

    /**
     * 前面的区域以后再来探索吧~
     */
    @SubscribeEvent
    public static void enterBiome(TickEvent.PlayerTickEvent event) {

    }


    @SubscribeEvent
    public static void onPlayerUseItem(LivingEntityUseItemEvent.Start event){

    }


}
