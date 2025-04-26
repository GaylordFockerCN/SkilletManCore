package com.p1nero.smc.util.gacha;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import net.kenddie.fantasyarmor.item.FAItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArmorGachaSystem {

    private static final double BASE_5STAR_RATE = 0.006;    // 0.6%
    private static final double BASE_4STAR_RATE = 0.051;    // 5.1%
    private static final int SOFT_PITY_5STAR_START = 73;
    private static final double RATE_INCREMENT_5STAR = 0.06; // 6% per pull after 73

    private static final Random random = new Random();
    public static final List<ItemStack> STAR3_LIST = new ArrayList<>();
    public static final List<ItemStack> STAR4_LIST = new ArrayList<>();
    public static final List<ItemStack> STAR5_LIST = new ArrayList<>();

    /**
     * 五星：hero | dark_cover | spark_of_dawn | gilded_hunt
     * 四星：golden_execution | lady Evening | old knight | eclipse | dragon_slayer | chess_board |dark_load | sunset_wings
     */
    public static void initItemList() {
        STAR3_LIST.addAll(List.of(Items.GOLDEN_HELMET.getDefaultInstance(), Items.GOLDEN_CHESTPLATE.getDefaultInstance(), Items.GOLDEN_LEGGINGS.getDefaultInstance(), Items.GOLDEN_BOOTS.getDefaultInstance(),
                Items.DIAMOND_HELMET.getDefaultInstance(), Items.DIAMOND_CHESTPLATE.getDefaultInstance(), Items.DIAMOND_LEGGINGS.getDefaultInstance(), Items.DIAMOND_BOOTS.getDefaultInstance(),
                Items.NETHERITE_HELMET.getDefaultInstance(), Items.NETHERITE_CHESTPLATE.getDefaultInstance(), Items.NETHERITE_LEGGINGS.getDefaultInstance(), Items.NETHERITE_BOOTS.getDefaultInstance(),
                Items.IRON_HELMET.getDefaultInstance(), Items.IRON_CHESTPLATE.getDefaultInstance(), Items.IRON_LEGGINGS.getDefaultInstance(), Items.IRON_BOOTS.getDefaultInstance()));

        STAR4_LIST.addAll(List.of(FAItems.LADY_MARIA_HELMET.get().getDefaultInstance(), FAItems.LADY_MARIA_CHESTPLATE.get().getDefaultInstance(), FAItems.LADY_MARIA_LEGGINGS.get().getDefaultInstance(), FAItems.LADY_MARIA_BOOTS.get().getDefaultInstance(),
                FAItems.GOLDEN_EXECUTION_HELMET.get().getDefaultInstance(), FAItems.GOLDEN_EXECUTION_CHESTPLATE.get().getDefaultInstance(), FAItems.GOLDEN_EXECUTION_LEGGINGS.get().getDefaultInstance(), FAItems.GOLDEN_EXECUTION_BOOTS.get().getDefaultInstance(),
                FAItems.OLD_KNIGHT_HELMET.get().getDefaultInstance(), FAItems.OLD_KNIGHT_CHESTPLATE.get().getDefaultInstance(), FAItems.OLD_KNIGHT_LEGGINGS.get().getDefaultInstance(), FAItems.OLD_KNIGHT_BOOTS.get().getDefaultInstance(),
                FAItems.ECLIPSE_SOLDIER_HELMET.get().getDefaultInstance(), FAItems.ECLIPSE_SOLDIER_CHESTPLATE.get().getDefaultInstance(), FAItems.ECLIPSE_SOLDIER_LEGGINGS.get().getDefaultInstance(), FAItems.ECLIPSE_SOLDIER_BOOTS.get().getDefaultInstance(),
                FAItems.DRAGONSLAYER_HELMET.get().getDefaultInstance(), FAItems.DRAGONSLAYER_CHESTPLATE.get().getDefaultInstance(), FAItems.DRAGONSLAYER_LEGGINGS.get().getDefaultInstance(), FAItems.DRAGONSLAYER_BOOTS.get().getDefaultInstance(),
                FAItems.CHESS_BOARD_KNIGHT_HELMET.get().getDefaultInstance(), FAItems.CHESS_BOARD_KNIGHT_CHESTPLATE.get().getDefaultInstance(), FAItems.CHESS_BOARD_KNIGHT_LEGGINGS.get().getDefaultInstance(), FAItems.CHESS_BOARD_KNIGHT_BOOTS.get().getDefaultInstance(),
                FAItems.SUNSET_WINGS_HELMET.get().getDefaultInstance(), FAItems.SUNSET_WINGS_CHESTPLATE.get().getDefaultInstance(), FAItems.SUNSET_WINGS_LEGGINGS.get().getDefaultInstance(), FAItems.SUNSET_WINGS_BOOTS.get().getDefaultInstance()));

        STAR5_LIST.addAll(List.of(FAItems.HERO_HELMET.get().getDefaultInstance(), FAItems.HERO_CHESTPLATE.get().getDefaultInstance(), FAItems.HERO_LEGGINGS.get().getDefaultInstance(), FAItems.HERO_BOOTS.get().getDefaultInstance(),
                FAItems.DARK_COVER_HELMET.get().getDefaultInstance(), FAItems.DARK_COVER_CHESTPLATE.get().getDefaultInstance(), FAItems.DARK_COVER_LEGGINGS.get().getDefaultInstance(), FAItems.DARK_COVER_BOOTS.get().getDefaultInstance(),
                FAItems.SPARK_OF_DAWN_HELMET.get().getDefaultInstance(), FAItems.SPARK_OF_DAWN_CHESTPLATE.get().getDefaultInstance(), FAItems.SPARK_OF_DAWN_LEGGINGS.get().getDefaultInstance(), FAItems.SPARK_OF_DAWN_BOOTS.get().getDefaultInstance(),
                FAItems.GILDED_HUNT_HELMET.get().getDefaultInstance(), FAItems.GILDED_HUNT_CHESTPLATE.get().getDefaultInstance(), FAItems.GILDED_HUNT_LEGGINGS.get().getDefaultInstance(), FAItems.GILDED_HUNT_BOOTS.get().getDefaultInstance()));

    }

    public static ItemStack pull(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        return pull(STAR3_LIST, STAR4_LIST, STAR5_LIST, smcPlayer);
    }

    public static ItemStack pull(List<ItemStack> threeStar, List<ItemStack> fourStar, List<ItemStack> fiveStar, SMCPlayer playerData) {
        // 更新保底计数器
        playerData.incrementArmorPity5Star();
        playerData.incrementAmorPity4Star();

        int currentPity5 = playerData.getArmorPity5Star();
        int currentPity4 = playerData.getArmorPity4Star();

        // 先检查五星保底
        if (currentPity5 >= 90) {
            return handle5StarPull(fiveStar, playerData);
        }

        // 计算动态五星概率
        double current5StarRate = calculate5StarRate(currentPity5);
        if (random.nextDouble() <= current5StarRate) {
            return handle5StarPull(fiveStar, playerData);
        }

        // 检查四星保底
        if (currentPity4 >= 10) {
            return handle4StarPull(fourStar, playerData);
        }

        // 常规四星概率
        if (random.nextDouble() <= BASE_4STAR_RATE) {
            return handle4StarPull(fourStar, playerData);
        }

        // 保底三星
        return getRandomItem(threeStar);
    }

    private static ItemStack handle5StarPull(List<ItemStack> fiveStarPool, SMCPlayer playerData) {
        playerData.resetArmorPity5Star();
        playerData.resetArmorPity4Star(); // 抽到五星会重置四星保底
        return getRandomItem(fiveStarPool);
    }

    private static ItemStack handle4StarPull(List<ItemStack> fourStarPool, SMCPlayer playerData) {
        playerData.resetArmorPity4Star();
        return getRandomItem(fourStarPool);
    }

    private static double calculate5StarRate(int pityCounter) {
        if (pityCounter <= SOFT_PITY_5STAR_START) {
            return BASE_5STAR_RATE;
        }
        return Math.min(BASE_5STAR_RATE + (pityCounter - SOFT_PITY_5STAR_START) * RATE_INCREMENT_5STAR, 1.0);
    }

    private static ItemStack getRandomItem(List<ItemStack> itemPool) {
        if (itemPool.isEmpty()) {
            throw new IllegalArgumentException("Item pool cannot be empty");
        }
        return itemPool.get(random.nextInt(itemPool.size()));
    }
}