package com.p1nero.smc.client.gui.screen.info_screen;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StartGuideScreen {
    public static void addStartGuideScreen() {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder("start_guide");

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(null, Component.literal("").append(builder.name().copy().withStyle(ChatFormatting.AQUA)).append(": \n"));

        TreeNode root = new TreeNode(builder.ans(0))
                .addChild(new TreeNode(builder.ans(1), builder.opt(0))
                        .addChild(new TreeNode(builder.ans(2), builder.opt(2))
                                .addLeaf(builder.opt(4), (byte) NpcPlayerInteractPacket.DO_NOTHING))
                        .addChild(new TreeNode(builder.ans(2), builder.opt(3))
                                .addLeaf(builder.opt(4), (byte) NpcPlayerInteractPacket.DO_NOTHING))
                )
                .addChild(new TreeNode(builder.ans(3), builder.opt(1))
                        .addLeaf(builder.opt(5), (byte) NpcPlayerInteractPacket.DO_NOTHING)
                        .addChild(new TreeNode(builder.ans(4), builder.opt(6))
                                .addLeaf(builder.opt(7), (byte) NpcPlayerInteractPacket.DO_NOTHING)));

        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }
}
