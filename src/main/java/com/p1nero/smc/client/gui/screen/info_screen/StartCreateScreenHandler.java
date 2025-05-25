package com.p1nero.smc.client.gui.screen.info_screen;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StartCreateScreenHandler {
    public static final String name = "start_create";

    @OnlyIn(Dist.CLIENT)
    public static void addScreen() {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder(name);

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(null, Component.literal("").append(builder.name().copy().withStyle(ChatFormatting.AQUA)).append(": \n"));

        TreeNode root = new TreeNode(builder.ans(0))
                .addChild(new TreeNode(builder.ans(1), builder.opt(0))
                        .addLeaf(builder.opt(2))
                )
                .addChild(new TreeNode(builder.ans(2), builder.opt(1))
                        .addLeaf(builder.opt(2))
                );

        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerate(SMCLangGenerator generator) {
        generator.addScreenName(name, "§6炉灶");
        generator.addScreenAns(name, 0, "嘿小子，生意越来越好了！接下来给你展示展示本整合包留下的一个谜题：三只机械臂！听说若将其补全，便可成为究极无敌厨神！");
        generator.addScreenOpt(name, 0, "为什么不是机械手？");
        generator.addScreenAns(name, 1, "笨！机械臂比机械手优雅多了！这还是作者特意为这盘醋包的饺子！快去新餐厅地下室里看看机械臂吧！");
        generator.addScreenOpt(name, 1, "我不会机械动力怎么办？");
        generator.addScreenAns(name, 2, "不用担心！照着蓝图和成就做就行了！多用用思索，多上网查查，或者请教周围的人，任何时候开始学习机械动力都不晚！快去楼下看看机械臂吧！");
        generator.addScreenOpt(name, 2, "我已经等不及啦！");
    }

}
