package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class BlockListener {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getLevel() instanceof ServerLevel serverLevel) {
            Block block = event.getState().getBlock();
        }
    }
}