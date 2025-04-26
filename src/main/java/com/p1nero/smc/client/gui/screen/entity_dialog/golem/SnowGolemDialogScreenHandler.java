package com.p1nero.smc.client.gui.screen.entity_dialog.golem;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SnowGolemDialogScreenHandler {
    public static void handle(ServerPlayer serverPlayer, SnowGolem ironGolem, byte interactId) {

    }

    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(SnowGolem snowGolem) {
        DialogueComponentBuilder builder = new DialogueComponentBuilder(snowGolem);
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(snowGolem);
        TreeNode root = new TreeNode(builder.ans(0))
                .addChild(new TreeNode(builder.ans(1), builder.opt(0))
                        .addLeaf(builder.opt(1)))
                .addLeaf(builder.opt(1));
        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialog(EntityType.SNOW_GOLEM, 0, "唉，虽然我比铁傀儡更没存在感，但是作者没有忘记我，还给我加了对话。");
    }

}
