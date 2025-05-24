package com.p1nero.smc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import hungteen.htlib.HTLib;
import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import hungteen.htlib.util.helper.CodecHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SMCRaidManager {

    private static final BiMap<Integer, UUID> RAID_MAP = HashBiMap.create();//TODO 持久化
    private static final ResourceLocation DUMMY_TYPE = ResourceLocation.fromNamespaceAndPath(HTLib.MOD_ID, "default_raid");

    public static Player getPlayer(ServerLevel serverLevel, int raidId) {
        return serverLevel.getPlayerByUUID(RAID_MAP.get(raidId));
    }

    /**
     * 突破等级试炼
     */
    public static void startTrial(ServerPlayer serverPlayer, SMCPlayer smcPlayer) {
        if(Level.isInSpawnableBounds(serverPlayer.blockPosition())) {
            int stage = smcPlayer.getStage() + 1;
            DummyEntity dummyEntity = createRaid(serverPlayer.serverLevel(), ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "trial_" + stage), serverPlayer.position());
            if(dummyEntity != null) {
                smcPlayer.setTodayInRaid(true);
                DataManager.inRaid.put(serverPlayer, true);
//                RAID_MAP.put(dummyEntity.getEntityID(), serverPlayer.getUUID());
            }
        }
    }

    /**
     * 夜晚袭击
     */
    public static void startNightRaid(ServerPlayer serverPlayer, SMCPlayer smcPlayer) {
        if(Level.isInSpawnableBounds(serverPlayer.blockPosition())) {
            //保险
            if(DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).size() > serverPlayer.serverLevel().players().size()){
                return;
            }

            int day = (int) (serverPlayer.serverLevel().getDayTime() / 24000);
            if(day > 30) {
                day = 30;
            }
            DummyEntity dummyEntity = createRaid(serverPlayer.serverLevel(), ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "raid_" + day), serverPlayer.position());
            if(dummyEntity != null) {
                smcPlayer.setTodayInRaid(true);
                DataManager.inRaid.put(serverPlayer, true);
//                RAID_MAP.put(dummyEntity.getEntityID(), serverPlayer.getUUID());
            }
        }
    }

    public static void startDayNetherRaid(ServerPlayer serverPlayer, SMCPlayer smcPlayer) {
        if(Level.isInSpawnableBounds(serverPlayer.blockPosition())) {
            //保险
            if(DummyEntityManager.getDummyEntities(serverPlayer.serverLevel()).size() > serverPlayer.serverLevel().players().size()){
                return;
            }

            int day = (int) (serverPlayer.serverLevel().getDayTime() / 24000);
            if(day > 30) {
                day = 30;
            }
            DummyEntity dummyEntity = createRaid(serverPlayer.serverLevel(), ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "nether_raid_" + day), serverPlayer.position());
            if(dummyEntity != null) {
                smcPlayer.setTodayInRaid(true);
                DataManager.inRaid.put(serverPlayer, true);
//                RAID_MAP.put(dummyEntity.getEntityID(), serverPlayer.getUUID());
            }
        }
    }

    /**
     * 白天的随机袭击
     */
    public static void startRandomRaid(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if(Level.isInSpawnableBounds(serverPlayer.blockPosition())) {
            int day = (int) (serverPlayer.serverLevel().getDayTime() / 24000);
            if(day > 30) {
                day = 30;
            }
            DummyEntity dummyEntity = createRaid(serverPlayer.serverLevel(), ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "raid_" + day), serverPlayer.position());
            if(dummyEntity != null) {
                smcPlayer.setTodayInRaid(true);
                DataManager.inRaid.put(serverPlayer, true);
//                RAID_MAP.put(dummyEntity.getEntityID(), serverPlayer.getUUID());
            }
        }
    }

    public static DummyEntity createRaid(ServerLevel serverLevel, ResourceLocation raidId, Vec3 position) {
        CompoundTag nbt = new CompoundTag();
        IRaidComponent raidComponent = HTRaidComponents.registry().getValue(serverLevel, HTRaidComponents.registry().createKey(raidId));
        CodecHelper.encodeNbt(HTRaidComponents.getDirectCodec(), raidComponent).result().ifPresent((tag) -> nbt.put("RaidTag", tag));
        return DummyEntityManager.createDummyEntity(serverLevel, DUMMY_TYPE, position, nbt);
    }

}
