package com.p1nero.smc.util.gacha;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeaponGachaSystem {

    private static final double BASE_5STAR_RATE = 0.006;    // 0.6%
    private static final double BASE_4STAR_RATE = 0.101;    // 10.1%
    private static final int SOFT_PITY_5STAR_START = 73;
    private static final double RATE_INCREMENT_5STAR = 0.06; // 6% per pull after 73

    private static final Random random = new Random();
    public static final List<ItemStack> STAR3_LIST = new ArrayList<>();
    public static final List<ItemStack> STAR4_LIST = new ArrayList<>();
    public static final List<ItemStack> STAR5_LIST = new ArrayList<>();

    public static void initItemList() {
        STAR3_LIST.addAll(List.of(Items.IRON_GOLEM_SPAWN_EGG.getDefaultInstance(), Items.SNOW_GOLEM_SPAWN_EGG.getDefaultInstance(), Items.SNOW_GOLEM_SPAWN_EGG.getDefaultInstance()));
        STAR3_LIST.addAll(List.of(CDItems.SPATULA.asStack(), SMCRegistrateItems.SPATULA_V2.asStack(), SMCRegistrateItems.SPATULA_V3.asStack()));
        STAR3_LIST.addAll(List.of(CDItems.SKILLET.asStack(), SMCRegistrateItems.IRON_SKILLET_LEVEL2.asStack(), SMCRegistrateItems.IRON_SKILLET_LEVEL3.asStack()));
        STAR3_LIST.addAll(List.of(Items.BOW.getDefaultInstance(), Items.ARROW.getDefaultInstance().copyWithCount(16)));
        STAR4_LIST.addAll(List.of(SMCItems.GOOD_SUPER_GOLEM_SPAWN_EGG.get().getDefaultInstance(), SMCItems.GOOD_SUPER_GOLEM_SPAWN_EGG.get().getDefaultInstance()));
        STAR4_LIST.addAll(List.of(SMCRegistrateItems.GOLDEN_SKILLET.asStack(), SMCRegistrateItems.GOLDEN_SKILLET_V2.asStack(), SMCRegistrateItems.GOLDEN_SKILLET_V3.asStack()));
        STAR4_LIST.addAll(List.of(SMCRegistrateItems.GOLDEN_SPATULA.asStack(), SMCRegistrateItems.GOLDEN_SPATULA_V2.asStack(), SMCRegistrateItems.GOLDEN_SPATULA_V3.asStack()));
        STAR5_LIST.addAll(List.of(SMCRegistrateItems.DIAMOND_SKILLET.asStack(), SMCRegistrateItems.DIAMOND_SKILLET_V2.asStack(), SMCRegistrateItems.DIAMOND_SKILLET_V3.asStack()));
        STAR5_LIST.addAll(List.of(SMCRegistrateItems.DIAMOND_SPATULA.asStack(), SMCRegistrateItems.DIAMOND_SPATULA_V2.asStack(), SMCRegistrateItems.DIAMOND_SPATULA_V3.asStack()));
        STAR5_LIST.addAll(List.of(SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get().getDefaultInstance(), SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get().getDefaultInstance(), SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get().getDefaultInstance(), SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get().getDefaultInstance(), SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get().getDefaultInstance()));
    }

    public static ItemStack pull(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        return pull(STAR3_LIST, STAR4_LIST, STAR5_LIST, smcPlayer);
    }

    public static ItemStack pull(List<ItemStack> threeStar, List<ItemStack> fourStar, List<ItemStack> fiveStar, SMCPlayer playerData) {
        // 更新保底计数器
        playerData.incrementWeaponPity5Star();
        playerData.incrementWeaponPity4Star();

        int currentPity5 = playerData.getWeaponPity5Star();
        int currentPity4 = playerData.getWeaponPity4Star();

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
        playerData.resetWeaponPity5Star();
        playerData.resetWeaponPity4Star(); // 抽到五星会重置四星保底
        return getRandomItem(fiveStarPool);
    }

    private static ItemStack handle4StarPull(List<ItemStack> fourStarPool, SMCPlayer playerData) {
        playerData.resetWeaponPity4Star();
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
            return Items.AIR.getDefaultInstance();
        }
        return itemPool.get(random.nextInt(itemPool.size())).copy();
    }
}