package com.p1nero.smc.item.custom.skillets;

import com.p1nero.invincible.client.keymappings.InvincibleKeyMappings;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.item.custom.SMCCuisineSkilletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiamondSkilletItemV2 extends DiamondSkilletItem {
    public DiamondSkilletItemV2(Block block, Properties properties) {
        super(block, properties, 2);
    }
}
