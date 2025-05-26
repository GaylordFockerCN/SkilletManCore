package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.effect.SMCEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BadCat extends SimpleDescriptionFoilItem{
    public BadCat(Properties properties) {
        super(properties);
    }

    public static void onInteractCat(ItemStack itemStack, Player player, Cat cat){
        if(player instanceof ServerPlayer serverPlayer && cat.getOwner() == serverPlayer) {
            if(!serverPlayer.level().isDay()) {
                player.displayClientMessage(SkilletManCoreMod.getInfo("npc_plus_need_rest"), false);
                return;
            }
            cat.addEffect(new MobEffectInstance(SMCEffects.BAD_CAT.get(), 600));
            player.displayClientMessage(SkilletManCoreMod.getInfo("cat_go").withStyle(ChatFormatting.RED), true);
            SMCAdvancementData.finishAdvancement("cat_group", serverPlayer);
            itemStack.shrink(1);
        }
    }

}
