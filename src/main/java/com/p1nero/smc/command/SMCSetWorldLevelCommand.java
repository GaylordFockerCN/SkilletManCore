package com.p1nero.smc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.p1nero.smc.archive.SMCArchiveManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class SMCSetWorldLevelCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dote")
                .then(Commands.literal("set_world_level").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes((context) -> {
                                    if(SMCArchiveManager.setWorldLevel(IntegerArgumentType.getInteger(context, "value")) && context.getSource().getPlayer() != null){
                                        context.getSource().getPlayer().displayClientMessage(Component.literal("World Level changed to: " + SMCArchiveManager.getWorldLevel()), false);
                                    }
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("get_world_level")
                        .executes((context) -> {
                            if(context.getSource().getPlayer() != null){
                                context.getSource().getPlayer().displayClientMessage(Component.literal("Current World Level: " + SMCArchiveManager.getWorldLevel()), false);
                            }
                            return 0;
                        })
                )
        );
    }
}
