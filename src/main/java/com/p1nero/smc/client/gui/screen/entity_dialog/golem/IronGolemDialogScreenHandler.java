package com.p1nero.smc.client.gui.screen.entity_dialog.golem;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IronGolemDialogScreenHandler {

    public static final byte FLOWER = 1;
    public static void handle(ServerPlayer serverPlayer, IronGolem ironGolem, byte interactId) {
        if(interactId == FLOWER) {
            ironGolem.offerFlower(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(IronGolem ironGolem) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
        DialogueComponentBuilder builder = new DialogueComponentBuilder(ironGolem);
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(ironGolem);
        TreeNode root;
        if(smcPlayer.getMorality() >= 0) {
            root = new TreeNode(builder.ans(0))
                    .addChild(new TreeNode(builder.ans(1), builder.opt(0))
                            .execute(FLOWER)
                            .addLeaf(builder.opt(1)))
                    .addLeaf(builder.opt(1));
        } else {
            root = new TreeNode(builder.ans(0))
                    .addChild(new TreeNode(builder.ans(2), builder.opt(0))
                            .addLeaf(builder.opt(1)))
                    .addLeaf(builder.opt(1));
        }
        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialog(EntityType.IRON_GOLEM, 0, "...（铁傀儡不语，只是一味地盯着你。看来不能指望这个大块头会说话。）");
        generator.addDialogChoice(EntityType.IRON_GOLEM, 0, "人品测试");
        generator.addDialogChoice(EntityType.IRON_GOLEM, 1, "离开");
        generator.addDialog(EntityType.IRON_GOLEM, 1, "（铁傀儡向你献花，看来你崇高的道德得到了它的认可。）");
        generator.addDialog(EntityType.IRON_GOLEM, 2, "（铁傀儡没有反应，看来你的道德不够崇高。）");
    }

}
