package com.p1nero.smc.item.custom.skillets;

import com.p1nero.invincible.client.keymappings.InvincibleKeyMappings;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.SMCCuisineSkilletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiamondSkilletItem extends SMCCuisineSkilletItem {
    protected String star = "⭐";
    protected final int level;
    public DiamondSkilletItem(Block block, Properties properties) {
        this(block, properties, 1);
    }

    public DiamondSkilletItem(Block block, Properties properties, int level) {
        super(block, properties);
        star = star.repeat(level);
        this.level = level;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        ChatFormatting formatting;
        formatting = switch (level) {
            case 2 -> ChatFormatting.GREEN;
            case 3 -> ChatFormatting.AQUA;
            case 4 -> ChatFormatting.DARK_PURPLE;
            case 5 -> ChatFormatting.GOLD;
            default -> ChatFormatting.GRAY;
        };
        if(this.level > 1) {
            return super.getDescription().copy().withStyle(ChatFormatting.AQUA).append(Component.literal(" " + star).withStyle(formatting));
        }
        return super.getDescription().copy().withStyle(ChatFormatting.AQUA);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(SkilletManCoreMod.getInfo("diamond_weapon_tip"));
        list.add(Component.translatable("item.smc.diamond_skillet_skill1", InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey2(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1()).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("item.smc.diamond_skillet_skill2", InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey2(), InvincibleKeyMappings.getTranslatableKey2()).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("item.smc.diamond_skillet_skill3", InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1(), InvincibleKeyMappings.getTranslatableKey1()).withStyle(ChatFormatting.GRAY));

    }

}
