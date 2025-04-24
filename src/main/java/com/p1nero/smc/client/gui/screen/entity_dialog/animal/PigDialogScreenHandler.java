package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
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

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialogEntityName(EntityType.PIG, "一只平凡的猪");
        generator.addDialog(EntityType.PIG, -1, "哼哼哼，哼哼（猪叫，看来作者真的没有给猪做对话...）");
        generator.addDialog(EntityType.PIG, 0, "（我大抵是疯了，竟然想和一头猪对话）");
        generator.addDialogChoice(EntityType.PIG, 0, "离开");
        generator.addDialog(EntityType.PIG, 1, "哼，等等！哼，为什么你觉得在这个整合包里猪不会说话？难道你不想和我说话吗？（猪猪君看透了你的心思）");
        generator.addDialogChoice(EntityType.PIG, 1, "你会突然站起来变成苦力怕吗");
        generator.addDialogChoice(EntityType.PIG, 2, "尝试 \\TOT/\\TOT/\\TOT/");
        generator.addDialogChoice(EntityType.PIG, 3, "对着它唱歌");
    }

}
