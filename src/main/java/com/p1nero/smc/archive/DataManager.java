package com.p1nero.smc.archive;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.PersistentBoolDataSyncPacket;
import com.p1nero.smc.network.packet.clientbound.PersistentDoubleDataSyncPacket;
import com.p1nero.smc.network.packet.clientbound.PersistentStringDataSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * 用于单一玩家数据，而不是全体数据，SaveUtil用于全体数据{@link SMCArchiveManager}
 * <p>
 * 别骂了，后来才知道可以用Capability，不过我发现这个就相当于是封装过的Capability哈哈
 * <p>
 * 玩家的PersistentData管理
 * 本质上只是管理key。由于NBT标签不存在null，所以我加入了一个lock变量，如果还没lock就表示不确定性。比如isWhite，在玩家做出选择之前你不能确定是哪个阵营。
 * 虽然使用的时候麻烦了一点，但是多了个lock相当于把bool拆成四个量来用。
 *
 * @author P1nero
 */
public class DataManager {
    public static BoolData firstJoint = new BoolData("first_joint", false);
    public static BoolData firstGiftGot = new BoolData("first_gift_got", false);
    public static BoolData firstGachaGot = new BoolData("first_gacha_got", false);
    public static BoolData firstWork = new BoolData("first_work", false);
    public static BoolData firstFoodBad = new BoolData("first_food_bad", false);
    public static BoolData firstChangeVillager = new BoolData("first_change_villager", false);
    public static BoolData inRaid = new BoolData("in_raid", false);
    public static BoolData inSpecial = new BoolData("in_special", false);//特殊事件是否解决，将在解决后重置
    public static BoolData specialSolvedToday = new BoolData("special_solved_today", false);//今天是否处于Special事件，将在夜里重置
    public static BoolData specialEvent1Solved = new BoolData("se1s", false);
    public static BoolData specialEvent2Solved = new BoolData("se2s", false);
    public static BoolData specialEvent3Solved = new BoolData("se3s", false);
    public static BoolData specialEvent4Solved = new BoolData("se4s", false);

    /**
     * 获取哪个还没用过的特殊事件
     */
    public static boolean hasAnySpecialEvent(ServerPlayer serverPlayer) {
        return !specialEvent1Solved.get(serverPlayer) || !specialEvent2Solved.get(serverPlayer) || !specialEvent3Solved.get(serverPlayer) || !specialEvent4Solved.get(serverPlayer);
    }

    public static void putData(Player player, String key, double value) {
        getSMCPlayer(player).putDouble(key, value);
    }

    public static void putData(Player player, String key, String value) {
        getSMCPlayer(player).putString(key, value);
    }

    public static void putData(Player player, String key, boolean value) {
        getSMCPlayer(player).putBoolean(key, value);
    }

    public static boolean getBool(Player player, String key) {
        return getSMCPlayer(player).getBoolean(key);
    }

    public static double getDouble(Player player, String key) {
        return getSMCPlayer(player).getDouble(key);
    }

    public static String getString(Player player, String key) {
        return getSMCPlayer(player).getString(key);
    }

    public static SMCPlayer getSMCPlayer(Player player) {
        return player.getCapability(SMCCapabilityProvider.SMC_PLAYER).orElse(new SMCPlayer());
    }


    public abstract static class Data<T> {

        protected String key;
        protected boolean isLocked = false;//增加一个锁，用于初始化数据用
        protected int id;

        public Data(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void init(Player player) {
            isLocked = getSMCPlayer(player).getBoolean(key + "isLocked");

        }

        public boolean isLocked(Player player) {
            return getSMCPlayer(player).getBoolean(key + "isLocked");
        }

        public boolean isLocked(CompoundTag playerData) {
            return playerData.getBoolean(key + "isLocked");
        }

        public void lock(Player player) {
            getSMCPlayer(player).putBoolean(key + "isLocked", true);
            isLocked = true;
        }

        public void unLock(Player player) {
            getSMCPlayer(player).putBoolean(key + "isLocked", false);
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            isLocked = false;
        }

        public abstract T get(Player player);

        public abstract void put(Player player, T data);

    }

    public static class StringData extends Data<String> {

        protected boolean isLocked = false;//增加一个锁
        protected String defaultString = "";

        public StringData(String key, String defaultString) {
            super(key);
            this.defaultString = defaultString;
        }

        @Override
        public void init(Player player) {
            put(player, defaultString);
        }

        @Override
        public void put(Player player, String value) {
            if (!isLocked(player)) {
                getSMCPlayer(player).putString(key, value);
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new PersistentStringDataSyncPacket(key, isLocked, value), serverPlayer);
                }
            }
        }

        @Override
        public String get(Player player) {
            return getSMCPlayer(player).getString(key);
        }

        public String get(CompoundTag playerData) {
            return playerData.getString(key);
        }

    }

    public static class DoubleData extends Data<Double> {

        private double defaultValue = 0;

        public DoubleData(String key, double defaultValue) {
            super(key);
            this.defaultValue = defaultValue;
        }

        public void init(Player player) {
            isLocked = getSMCPlayer(player).getBoolean(key + "isLocked");
            put(player, defaultValue);
        }

        @Override
        public void put(Player player, Double value) {
            if (!isLocked(player)) {
                getSMCPlayer(player).putDouble(key, value);
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new PersistentDoubleDataSyncPacket(key, isLocked, value), serverPlayer);
                }
            }
        }

        @Override
        public Double get(Player player) {
            return getSMCPlayer(player).getDouble(key);
        }

        public double get(CompoundTag playerData) {
            return playerData.getDouble(key);
        }

    }

    public static class BoolData extends Data<Boolean> {

        boolean defaultBool;

        public BoolData(String key, boolean defaultBool) {
            super(key);
            this.defaultBool = defaultBool;
        }

        public void init(Player player) {
            isLocked = getSMCPlayer(player).getBoolean(key + "isLocked");
            put(player, defaultBool);
        }

        @Override
        public void put(Player player, Boolean value) {
            if (isLocked(player))
                return;

            getSMCPlayer(player).putBoolean(key, value);
            if (player instanceof ServerPlayer serverPlayer) {
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new PersistentBoolDataSyncPacket(key, isLocked, value), serverPlayer);
            }
        }

        @Override
        public Boolean get(Player player) {
            return getSMCPlayer(player).getBoolean(key);
        }

        public boolean get(CompoundTag playerData) {
            return playerData.getBoolean(key);
        }

    }

}
