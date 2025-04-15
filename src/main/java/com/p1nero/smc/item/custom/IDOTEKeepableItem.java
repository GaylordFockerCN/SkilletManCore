package com.p1nero.smc.item.custom;

import com.p1nero.smc.util.ItemUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 进出维度不会被删除的物品
 */
public interface IDOTEKeepableItem {
    default boolean shouldKeepWhenEnterDim(){
        return true;
    }
    default boolean shouldKeepWhenExitDim(){
        return true;
    }

    /**
     * 确保不存在非DOTEKeepableItem的物品
     */
    static boolean check(Player player, boolean isEnter){
        AtomicBoolean checkCurios = new AtomicBoolean(true);
        if(ModList.get().isLoaded("curios")){

        }
        for(NonNullList<ItemStack> list : ItemUtil.getCompartments(player)){
            for (ItemStack stack : list){
                if(isEnter){
                    if(!stack.isEmpty() && !(stack.getItem() instanceof IDOTEKeepableItem doteKeepableItem && doteKeepableItem.shouldKeepWhenEnterDim())){
                        return false;
                    }
                } else {
                    if(!stack.isEmpty() && !(stack.getItem() instanceof IDOTEKeepableItem doteKeepableItem && doteKeepableItem.shouldKeepWhenExitDim())){
                        return false;
                    }
                }
            }
        }
        return checkCurios.get();
    }

}
