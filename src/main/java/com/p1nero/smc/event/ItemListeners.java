package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
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
        }
    }
}
