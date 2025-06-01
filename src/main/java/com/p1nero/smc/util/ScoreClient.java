package com.p1nero.smc.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 评分纯属娱乐，仅收集玩家名（避免重复提交，不会公示姓名）和分数
 */
public class ScoreClient {
    public static void sendScore(String score, Player player) {
        if (!isValidScore(score)) {
            player.displayClientMessage(Component.literal("Invalid Score: [" + score + "] !").withStyle(ChatFormatting.RED), false);
            return;
        }

        if (player.getServer() != null) {
            if (player.getServer().isSingleplayer() && Minecraft.getInstance().getUser().getType() != User.Type.MSA) {
                player.displayClientMessage(Component.literal("Online Required!").withStyle(ChatFormatting.RED), false);
                return;
            }
        }

        if (player.getServer() instanceof DedicatedServer dedicatedServer && !dedicatedServer.usesAuthentication()) {
            player.displayClientMessage(Component.literal("Online Required!").withStyle(ChatFormatting.RED), false);
            return;
        }

        String serverAddress = "175.178.186.228";//坦诚相待Bro
        int port = 1145;
        String name = player.getGameProfile().getName();
        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(name + "," + score);
            player.displayClientMessage(Component.literal("Sent Score: [" + score + "] !").withStyle(ChatFormatting.GREEN), false);
        } catch (IOException e) {
            player.displayClientMessage(Component.literal("Failed to connect server: " + e.getMessage()).withStyle(ChatFormatting.RED), false);
        }
    }

    public static boolean isValidScore(String score) {
        return score != null &&
                score.length() == 8 &&
                score.matches("[0-5]+");
    }

}
