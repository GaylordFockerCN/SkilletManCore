package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;

public class PigDialogScreenHandler {
    public static void addPigDialogScreen(Pig pig) {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder("");

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(pig);

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
