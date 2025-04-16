package com.p1nero.smc.command;

import com.p1nero.smc.SkilletManCoreMod;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SMCCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        SMCSetConfigCommand.register(dispatcher);
        SMCSetWorldLevelCommand.register(dispatcher);
//        SMCTimeCommand.register(dispatcher);
    }
}