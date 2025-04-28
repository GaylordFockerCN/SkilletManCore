package com.p1nero.smc.util;

import com.google.common.collect.ImmutableList;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.entity.custom.CustomColorItemEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author P1nero
 * 做一些通用的物品栏处理
*/
public class ItemUtil {

    public static boolean tryAddRandomItem(ServerPlayer serverPlayer, Set<ItemStack> itemStackSet, int moneyNeed, int need) {
        return tryAddRandomItem(serverPlayer, new ArrayList<>(itemStackSet), moneyNeed, need);
    }

    public static boolean tryAddRandomItem(ServerPlayer serverPlayer, List<ItemStack> itemList, int moneyNeed, int need) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (smcPlayer.getMoneyCount() < moneyNeed) {
            serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_NO, serverPlayer.getSoundSource(), 1.0F, 1.0F);
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("no_enough_money"), true);
            return false;
        } else {
            SMCPlayer.consumeMoney(moneyNeed, serverPlayer);
            serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            List<ItemStack> applyItems = new ArrayList<>();
            for (int i = 0; i < need; i++) {
                applyItems.add(itemList.get(serverPlayer.getRandom().nextInt(itemList.size())));
            }
            for (ItemStack itemStack : applyItems) {
                ItemUtil.addItemEntity(serverPlayer, itemStack);
            }
            return true;
        }
    }

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
        addItem(player, item, count, false);
    }

    public static void addItem(Player player, ItemStack item){
        addItem(player, item, false);
    }

    public static void addItem(Player player, ItemStack itemStack, boolean showTip){
        if(!player.level().isClientSide && showTip) {
            player.displayClientMessage(SkilletManCoreMod.getInfo("add_item_tip", itemStack.getDisplayName(), itemStack.getCount()), false);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        }
        if(!player.addItem(itemStack)){
            addItemEntity(player, itemStack);
        }
    }

    /**
     * 是否是需要加倍翻倍的奖励
     */
    public static void addItem(Player player, Item item, int count, boolean showTip){
        if(!player.level().isClientSide && showTip) {
            player.displayClientMessage(SkilletManCoreMod.getInfo("add_item_tip", item.getDefaultInstance().getDisplayName(), count), false);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        }
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

    public static CustomColorItemEntity addItemEntity(Entity spawnOn, Item item, int count){
        CustomColorItemEntity itemEntity = new CustomColorItemEntity(spawnOn.level(), spawnOn.getX(), spawnOn.getY(), spawnOn.getZ(), item.getDefaultInstance().copyWithCount(count));
        itemEntity.setPickUpDelay(40);
        spawnOn.level().addFreshEntity(itemEntity);
        return itemEntity;
    }

    public static CustomColorItemEntity addItemEntity(Entity spawnOn, ItemStack item){
        CustomColorItemEntity itemEntity = new CustomColorItemEntity(spawnOn.level(), spawnOn.getX(), spawnOn.getY(), spawnOn.getZ(), item);
        itemEntity.setPickUpDelay(40);
        spawnOn.level().addFreshEntity(itemEntity);
        return itemEntity;
    }
    public static CustomColorItemEntity addItemEntity(ServerLevel level, BlockPos pos, ItemStack item){
        CustomColorItemEntity itemEntity = new CustomColorItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), item);
        itemEntity.setPickUpDelay(40);
        level.addFreshEntity(itemEntity);
        return itemEntity;
    }
    public static CustomColorItemEntity addItemEntity(ServerLevel level, double x, double y, double z, ItemStack item){
        CustomColorItemEntity itemEntity = new CustomColorItemEntity(level, x, y, z, item);
        itemEntity.setPickUpDelay(40);
        level.addFreshEntity(itemEntity);
        return itemEntity;
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
