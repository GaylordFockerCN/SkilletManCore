package com.p1nero.smc.gameasset.skill.combo;

import com.google.common.collect.Lists;
import com.p1nero.invincible.skill.ComboBasicAttack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import reascer.wom.skill.WOMSkillDataKeys;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;

public class DiamondSpatulaCombo extends ComboBasicAttack {
    public DiamondSpatulaCombo(Builder builder) {
        super(builder);
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerpatch) {
        List<Component> list = Lists.newArrayList();
        list.add(Component.translatable(""));
        return list;
    }
}
