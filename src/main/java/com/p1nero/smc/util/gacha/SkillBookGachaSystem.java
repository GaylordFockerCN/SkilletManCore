package com.p1nero.smc.util.gacha;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import reascer.wom.main.WeaponsOfMinecraft;
import reascer.wom.world.item.WOMCreativeTabs;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.item.EpicFightCreativeTabs;
import yesman.epicfight.world.item.EpicFightItems;
import yesman.epicfight.world.item.SkillBookItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkillBookGachaSystem {
    private static final double BASE_4STAR_RATE = 0.051;    // 5.1%

    private static final Random random = new Random();
    public static final List<ItemStack> COMMON_LIST = new ArrayList<>();
    public static final List<ItemStack> RARE_LIST = new ArrayList<>();

    public static void initItemList() {
        SkillManager.getSkillNames((skill) -> skill.getCategory().learnable() && skill.getRegistryName().getNamespace().equals(EpicFightMod.MODID)).forEach((rl) -> {
            ItemStack stack = new ItemStack(EpicFightItems.SKILLBOOK.get());
            SkillBookItem.setContainingSkill(rl.toString(), stack);
            COMMON_LIST.add(stack);
        });
        SkillManager.getSkillNames((skill) -> skill.getCategory().learnable() && skill.getRegistryName().getNamespace().equals(WeaponsOfMinecraft.MODID)).forEach((rl) -> {
            ItemStack stack = new ItemStack(EpicFightItems.SKILLBOOK.get());
            SkillBookItem.setContainingSkill(rl.toString(), stack);
            RARE_LIST.add(stack);
        });
    }

    public static ItemStack pull(ServerPlayer serverPlayer) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        return pull(COMMON_LIST, RARE_LIST, smcPlayer);
    }

    public static ItemStack pull(List<ItemStack> common, List<ItemStack> rare, SMCPlayer playerData) {
        // 更新保底计数器
        playerData.incrementSkillBookPity4Star();

        int currentPity4 = playerData.getSkillBookPity();

        // 检查四星保底
        if (currentPity4 >= 10) {
            return handleRarePull(rare, playerData);
        }

        // 常规四星概率
        if (random.nextDouble() <= BASE_4STAR_RATE) {
            return handleRarePull(rare, playerData);
        }

        // 保底三星
        return getRandomItem(common);
    }

    private static ItemStack handleRarePull(List<ItemStack> rarePool, SMCPlayer playerData) {
        playerData.resetSkillBookPity();
        return getRandomItem(rarePool);
    }

    private static ItemStack getRandomItem(List<ItemStack> itemPool) {
        if (itemPool.isEmpty()) {
            throw new IllegalArgumentException("Item pool cannot be empty");
        }
        return itemPool.get(random.nextInt(itemPool.size()));
    }
}