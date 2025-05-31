package com.p1nero.smc.item.custom.spatulas;

import com.p1nero.invincible.client.keymappings.InvincibleKeyMappings;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.SMCSpatulaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiamondSpatulaBase extends SMCSpatulaItem {
    public DiamondSpatulaBase(Properties pProperties) {
        super(pProperties);
        this.cooldown = 10;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return super.getDescription().copy().withStyle(ChatFormatting.AQUA);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(SkilletManCoreMod.getInfo("diamond_spatula_tip"));
        list.add(Component.empty());
        list.add(SkilletManCoreMod.getInfo("diamond_weapon_tip"));
        list.add(Component.translatable("item.smc.diamond_spatula_skill1", InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey2()).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("item.smc.diamond_spatula_skill3").withStyle(ChatFormatting.GRAY));
    }
}
