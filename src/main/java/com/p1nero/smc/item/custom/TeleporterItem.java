package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TeleporterItem extends SimpleDescriptionFoilItem{
    public TeleporterItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(!level.isClientSide && player.isShiftKeyDown() && level.getServer() != null) {
            if(SMCCapabilityProvider.getSMCPlayer(player).getStage() <= 1){
                player.displayClientMessage(SkilletManCoreMod.getInfo("game_time_no_enough"), true);
                return super.use(level, player, hand);
            }

            ServerLevel end = level.getServer().getLevel(Level.END);
            if(end != null) {
                player.changeDimension(end);
                ItemUtil.addItem(player, Items.NETHERITE_PICKAXE.getDefaultInstance(), true);
            }
        }
        return super.use(level, player, hand);
    }
}
