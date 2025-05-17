package com.p1nero.smc.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.p1nero.smc.SMCConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.p1nero.smc.archive.SMCArchiveManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

public class SMCSetConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("smc")
                .then(Commands.literal("setConfig").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.literal("enable_better_structure_block_load")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes((context) -> setData(SMCConfig.ENABLE_BETTER_STRUCTURE_BLOCK_LOAD, BoolArgumentType.getBool(context, "value"), context))
                                )
                                .then(Commands.literal("reset")
                                        .executes((context) -> resetData(SMCConfig.ENABLE_BETTER_STRUCTURE_BLOCK_LOAD, context))
                                )
                        )

                        .then(Commands.literal("test_x")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes((context) -> setData(SMCConfig.TEST_X, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                        )
                        .then(Commands.literal("test_y")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes((context) -> setData(SMCConfig.TEST_Y, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                        )
                        .then(Commands.literal("test_z")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                        .executes((context) -> setData(SMCConfig.TEST_Z, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                        )
                        .then(Commands.literal("fast_kill_boss")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes((context) -> setData(SMCConfig.FAST_BOSS_FIGHT, BoolArgumentType.getBool(context, "value"), context))
                                )
                        )
                        .then(Commands.literal("fast_kill_boss")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes((context) -> setData(SMCConfig.FAST_BOSS_FIGHT, BoolArgumentType.getBool(context, "value"), context))
                                )
                        )
                        .then(Commands.literal("task_tip_x")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 1))
                                        .executes((context) -> setData(SMCConfig.INFO_X, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                                .then(Commands.literal("reset")
                                        .executes((context) -> resetData(SMCConfig.INFO_X, context))
                                )
                        )
                        .then(Commands.literal("task_tip_y")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 1))
                                        .executes((context) -> setData(SMCConfig.INFO_Y, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                                .then(Commands.literal("reset")
                                        .executes((context) -> resetData(SMCConfig.INFO_Y, context))
                                )
                        )
                        .then(Commands.literal("task_tip_size")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0))
                                        .executes((context) -> setData(SMCConfig.TASK_SIZE, DoubleArgumentType.getDouble(context, "value"), context))
                                )
                                .then(Commands.literal("reset")
                                        .executes((context) -> resetData(SMCConfig.TASK_SIZE, context))
                                )
                        )
                        .then(Commands.literal("task_tip_interval")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0))
                                        .executes((context) -> setData(SMCConfig.INTERVAL, ((int) DoubleArgumentType.getDouble(context, "value")), context))
                                )
                                .then(Commands.literal("reset")
                                        .executes((context) -> resetData(SMCConfig.INTERVAL, context))
                                )
                        )
                )
        );
    }

    private static <T extends ForgeConfigSpec.ConfigValue<E>, E> int setData(T key, E value, CommandContext<CommandSourceStack> context) {
        CommandSourceStack stack = context.getSource();
        key.set(value);
        if(stack.getPlayer() != null){
            stack.getPlayer().displayClientMessage(Component.literal( context.getInput() + " : SUCCESS"), false);
            if(key == SMCConfig.TEST_Y || key == SMCConfig.TEST_X || key == SMCConfig.TEST_Z){
                stack.getPlayer().displayClientMessage(Component.literal(SMCConfig.TEST_X.get()+ ", " + SMCConfig.TEST_Y.get() + ", " + SMCConfig.TEST_Z.get()), false);
            }
        }
        return 0;
    }

    private static <T extends ForgeConfigSpec.ConfigValue<E>, E> int resetData(T key, CommandContext<CommandSourceStack> context) {
        CommandSourceStack stack = context.getSource();
        key.set(key.getDefault());
        if(stack.getPlayer() != null){
            stack.getPlayer().displayClientMessage(Component.literal( context.getInput() + " : reset to " + key.getDefault()), false);
        }
        return 0;
    }
}
