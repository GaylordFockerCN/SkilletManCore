package com.p1nero.smc.client.gui.screen.info_screen;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.serverbound.ContractCallbackPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ContractScreenHandler {
    public static final String name = "contract_portal";
    @OnlyIn(Dist.CLIENT)
    public static void addScreen(Component playerName, UUID playerUUID) {
        DialogueScreen.ScreenDialogueBuilder builder = new DialogueScreen.ScreenDialogueBuilder(name);

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(null, Component.literal("").append(builder.name(playerName).copy().withStyle(ChatFormatting.AQUA)).append(": \n"));

        TreeNode root = new TreeNode(builder.ans(0, playerName))
                .addChild(new TreeNode(builder.ans(1, playerName), builder.opt(0))
                        .addExecutable((screen -> PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new ContractCallbackPacket(playerUUID))))
                        .addLeaf(builder.opt(2)))
                .addLeaf(builder.opt(1));

        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerate(SMCLangGenerator generator) {
        generator.addScreenName(name, "来自 %s 的契约");
        generator.addScreenAns(name, 0, "%s 向您发来了契约申请。同意后，双方将共享所有金币收入，但不共享声望等级。如需共享等级收入，可在向村民交付料理时轮流交付以共享声望等级！您是否愿意与之签订契约？§c§l注意，除非新的契约成立，否则契约将不可取消！");
        generator.addScreenOpt(name, 0, "同意签署");
        generator.addScreenOpt(name, 1, "我拒绝！");
        generator.addScreenAns(name, 1, "您成功与 %s 签订了契约！");
        generator.addScreenOpt(name, 2, "好耶！");
    }

}
