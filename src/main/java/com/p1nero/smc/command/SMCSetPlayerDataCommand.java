package com.p1nero.smc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SMCSetPlayerDataCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("smc")
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
                .then(Commands.literal("addMoney").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes((context) -> {
                                    if(context.getSource().getPlayer() != null){
                                        SMCPlayer.addMoney(IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer());
                                    }
                                    return 0;
                                })
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes((context) -> {
                                            SMCPlayer.addMoney(IntegerArgumentType.getInteger(context, "value"), EntityArgument.getPlayer(context, "player"));
                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("consumeMoney").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes((context) -> {
                                    if(context.getSource().getPlayer() != null){
                                        SMCPlayer.consumeMoney(IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer());
                                    }
                                    return 0;
                                })
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes((context) -> {
                                            SMCPlayer.consumeMoney(IntegerArgumentType.getInteger(context, "value"), EntityArgument.getPlayer(context, "player"));
                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("setTradeLevel").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes((context) -> {
                                    if(context.getSource().getPlayer() != null){
                                        SMCCapabilityProvider.getSMCPlayer(context.getSource().getPlayer()).setLevelSync(IntegerArgumentType.getInteger(context, "value"), context.getSource().getPlayer());
                                    }
                                    return 0;
                                })
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes((context) -> {
                                            ServerPlayer serverPlayer = EntityArgument.getPlayer(context, "player");
                                            SMCCapabilityProvider.getSMCPlayer(serverPlayer).setLevelSync(IntegerArgumentType.getInteger(context, "value"), serverPlayer);
                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("levelUp").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes((context) -> {
                                    SMCPlayer.levelUPPlayer(EntityArgument.getPlayer(context, "player"));
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("stageUp").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes((context) -> {
                                    SMCPlayer.stageUp(EntityArgument.getPlayer(context, "player"));
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("defendSuccess").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes((context) -> {
                                    SMCPlayer.defendSuccess(EntityArgument.getPlayer(context, "player"));
                                    return 0;
                                })
                        )
                )
                .then(Commands.literal("defendFailed").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes((context) -> {
                                    SMCPlayer.defendFailed(EntityArgument.getPlayer(context, "player"));
                                    return 0;
                                })
                        )
                )
        );
    }
}
