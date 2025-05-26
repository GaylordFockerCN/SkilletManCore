package com.p1nero.smc.item.custom;

import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.effect.SMCEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RumorItem extends ThrowablePotionItem {
    public RumorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack instance = new ItemStack(this);
        PotionUtils.setCustomEffects(instance, List.of(new MobEffectInstance(SMCEffects.RUMOR.get(), 1200)));
        instance.setHoverName(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.AQUA));
        return instance;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        PotionUtils.setCustomEffects(player.getItemInHand(hand), List.of(new MobEffectInstance(SMCEffects.RUMOR.get(), 2400)));
        if(player instanceof ServerPlayer serverPlayer) {
            SMCAdvancementData.finishAdvancement("rumor", serverPlayer);
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable(this.getDescriptionId() + ".usage").withStyle(ChatFormatting.GRAY));
    }

}
