package com.p1nero.smc.client.gui.screen.info_screen;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BanPortalScreenHandler {
    public static final String name = "ban_portal";
    @OnlyIn(Dist.CLIENT)
    public static void addScreen() {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder(name);

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(null, Component.literal("").append(builder.name().copy().withStyle(ChatFormatting.AQUA)).append(": \n"));

        TreeNode root = new TreeNode(builder.ans(0))
                .addLeaf(builder.opt(0));

        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerate(SMCLangGenerator generator) {
        generator.addScreenName(name, "不知何处传来的声音");
        generator.addScreenAns(name, 0, "（传送门并没有如期出现，看来作者并不需要你进入地狱，或者逃课直接前往末地。还是老实经营吧，咱们这是创新玩法的整合包，就别想着干这种原版的事情啦）");
        generator.addScreenOpt(name, 0, "好吧");
    }

}
