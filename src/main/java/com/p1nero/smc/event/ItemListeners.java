package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.SMCItems;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class ItemListeners {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            if(stack.getItem() instanceof SpatulaItem || stack.getItem() instanceof CuisineSkilletItem) {
                event.getToolTip().add(SkilletManCoreMod.getInfo("two_craft_tip").withStyle(ChatFormatting.YELLOW));
            }
            if(stack.is(SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get())) {
                event.getToolTip().add(SkilletManCoreMod.getInfo("no_brain_villager_spawn_egg_tip").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
