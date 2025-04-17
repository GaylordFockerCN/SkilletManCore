package com.p1nero.smc.util;

import com.google.common.collect.ImmutableList;
import com.p1nero.smc.SMCConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author LZY
 * 做一些通用的物品栏处理
*/
public class ItemUtil {

    public static List<NonNullList<ItemStack>> getCompartments(Player player){
        return ImmutableList.of(player.getInventory().items, player.getInventory().armor, player.getInventory().offhand);
    };

    /**
     * 判断是否全穿了
     */
    public static boolean isFullSets(Entity entity, ObjectArrayList<?> objects){
        return isFullSets(entity, objects, 4);
    }

    /**
     * 判断穿了几件
     * @param need 集齐了几套
     */
    public static boolean isFullSets(Entity entity, ObjectArrayList<?> objects, int need){
        int cnt = 0;
        for (ItemStack stack : entity.getArmorSlots()) {
            if (stack.isEmpty()){
                continue;
            }
            if(objects.contains(stack.getItem())){
                cnt++;
            }
        }
        return cnt >= need;
    }

    /**
     * 添加物品，失败则掉落
     */
    public static void addItem(Player player, Item item, int count){
        int maxStackSize = item.getDefaultInstance().getMaxStackSize();
        if(!player.addItem(item.getDefaultInstance().copyWithCount(count))){
            if(maxStackSize < count){
                for(int i = 0; i < count / maxStackSize; i++){
                    addItemEntity(player, item, maxStackSize);
                }
                addItemEntity(player, item, count % maxStackSize);
            } else {
                addItemEntity(player, item, count);
            }
        }
    }

    public static void addItem(Player player, ItemStack item){
        if(!player.addItem(item)){
            addItemEntity(player, item);
        }
    }

    /**
     * 是否是需要加倍翻倍的奖励
     */
    public static void addItem(Player player, Item item, int count, boolean isImportantLoot){
        if(isImportantLoot && SMCConfig.BOSS_HEALTH_AND_LOOT_MULTIPLE.get() && player.level() instanceof ServerLevel serverLevel){
            addItem(player, item, serverLevel.getPlayers((serverPlayer -> true)).size() * count);
        }
    }

    public static void addItemEntity(Entity spawnOn, Item item, int count){
        ItemEntity itemEntity = new ItemEntity(spawnOn.level(), spawnOn.getX(), spawnOn.getY(), spawnOn.getZ(), item.getDefaultInstance().copyWithCount(count));
        spawnOn.level().addFreshEntity(itemEntity);
    }

    public static void addItemEntity(Entity spawnOn, ItemStack item){
        ItemEntity itemEntity = new ItemEntity(spawnOn.level(), spawnOn.getX(), spawnOn.getY(), spawnOn.getZ(), item);
        spawnOn.level().addFreshEntity(itemEntity);
    }
    public static void addItemEntity(ServerLevel level, BlockPos pos, ItemStack item){
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), item);
        level.addFreshEntity(itemEntity);
    }
    public static void addItemEntity(ServerLevel level, double x, double y, double z, ItemStack item){
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, item);
        level.addFreshEntity(itemEntity);
    }

    /**
     * 递归搜索并消耗物品栏物品
     * @param need 需要消耗的个数
     * @return 返回找到的数量，此数值小于等于need
     */
    public static int searchAndConsumeItem(Player player, Item item, int need){
        int total = 0;
        ItemStack stack = ItemStack.EMPTY;
        if(item == player.getMainHandItem().getItem()){
            stack = player.getMainHandItem();
        }else if(item == player.getOffhandItem().getItem()){
            stack = player.getOffhandItem();
        }else {
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack teststack = player.getInventory().items.get(i);
                if (teststack.getItem() == item) {
                    stack = teststack;
                    break;
                }
            }
        }

        if (stack != ItemStack.EMPTY) {
            if (stack.getCount() >= need) {
                stack.shrink(need);
                return need;
            } else {
                int cnt = stack.getCount();
                stack.shrink(cnt);
                total += cnt;
                total += searchAndConsumeItem(player,item,need - cnt);
                return total;
            }
        }else{
            return 0;
        }
    }

    /**
     * 搜索第一个物品所在的物品栈
     * @return 返回物品栈
     */
    public static ItemStack searchItemStack(Player player, Item item) {
        ItemStack stack = ItemStack.EMPTY;
        if (item == player.getMainHandItem().getItem()) {
            stack = player.getMainHandItem();
        } else if (item == player.getOffhandItem().getItem()) {
            stack = player.getOffhandItem();
        } else {
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack teststack = player.getInventory().items.get(i);
                if (teststack.getItem() == item) {
                    stack = teststack;
                    break;
                }
            }
        }
        return stack;
    }

    public static void clearItem(Player player, Item item){
        ItemStack stack;
        while((stack = searchItemStack(player, item)) != ItemStack.EMPTY){
            stack.setCount(0);
        }
    }

}
