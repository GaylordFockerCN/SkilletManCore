package com.p1nero.smc.util;

import hungteen.htlib.common.world.raid.DefaultRaid;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SMCRaidManager {

    public static final Map<UUID, DefaultRaid> COMMON_RAID_MAP = new HashMap<>();

    /**
     * 突破等级试炼 TODO
     */
    public static void startTrial(ServerPlayer serverPlayer) {

    }

    /**
     * 夜晚袭击 TODO
     */
    public static void startNightRaid(ServerPlayer serverPlayer) {

    }

    /**
     * 白天随机袭击 TODO
     */
    public static void startRandomRaid(ServerPlayer serverPlayer) {

    }

}
