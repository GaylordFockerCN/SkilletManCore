package com.p1nero.smc.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SimpleDescriptionArmorItem extends ArmorItem {
    protected static final UUID MAX_STAMINA_UUID = UUID.fromString("CC111E1C-4180-4820-B01B-BCCE1234ACA9");
    protected static final UUID STAMINA_REGEN_UUID = UUID.fromString("CC222E1C-4180-4820-B01B-BCCE1234ACA9");
    public SimpleDescriptionArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, list, flag);
        list.add(Component.translatable(this.getDescriptionId()+".usage"));
    }

}
