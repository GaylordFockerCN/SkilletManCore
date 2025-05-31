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
public class FastKillBossScreenHandler {
    public static final String name = "fast_kill_boss";
    @OnlyIn(Dist.CLIENT)
    public static void addScreen() {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder(name);

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(null, Component.literal("").append(builder.name().copy().withStyle(ChatFormatting.AQUA)).append(": \n"));

        TreeNode root = new TreeNode(builder.ans(0))
                .addLeaf(builder.opt(0), NpcPlayerInteractPacket.FAST_KILL_BOSS)
                .addLeaf(builder.opt(1), (byte) NpcPlayerInteractPacket.DO_NOTHING)
                .addLeaf(builder.opt(2), (byte) NpcPlayerInteractPacket.DO_NOTHING);

        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerate(SMCLangGenerator generator) {
        generator.addScreenName(name, "系统");
        generator.addScreenAns(name, 0, "系统检测到您被boss爆杀，是否愿意花费§a100,000§r开启§c速杀boss模式§r？§f机会难得，先到先得！§c一旦启动便无法取消！不过我们目前只有这一个BOSS");
        generator.addScreenOpt(name, 0, "好好好！有这种好事！风灵月影宗启动！");
        generator.addScreenOpt(name, 1, "我要靠自己，我是类魂糕手！");
        generator.addScreenOpt(name, 2, "要不是相机兼容太难用了，不然我早杀了！");
    }

}
