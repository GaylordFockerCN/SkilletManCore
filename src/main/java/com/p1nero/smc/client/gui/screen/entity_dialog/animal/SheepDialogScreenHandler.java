package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SheepDialogScreenHandler {
    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Sheep sheep) {

        DialogueComponentBuilder builder = new DialogueComponentBuilder(sheep);

        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(sheep);

        TreeNode root = new TreeNode(builder.ans(0));

        TreeNode mie = new TreeNode(builder.ans(2), builder.opt(2));
                mie.addChild(new TreeNode(builder.ans(3), builder.opt(3))
                        .addChild(new TreeNode(builder.ans(4), builder.opt(4))
                                .addChild(new TreeNode(builder.ans(5), builder.opt(5))
                                        .addChild(new TreeNode(builder.ans(6), builder.opt(6))
                                                .addChild(new TreeNode(builder.ans(1), builder.opt(7))
                                                        .addChild(mie))))));
            root.addChild(new TreeNode(builder.ans(1), builder.opt(0))
                            .addChild(mie))
                    .addLeaf(builder.opt(1));
        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialog(EntityType.SHEEP, 0, "咩~");
        generator.addDialogChoice(EntityType.SHEEP, 0, "尝试对着它咩");
        generator.addDialogChoice(EntityType.SHEEP, 1, "离开");
        generator.addDialog(EntityType.SHEEP, 1, "咩—咩—咩—咩—");//H
        generator.addDialogChoice(EntityType.SHEEP, 2, "咩—咩—咩—");//S
        generator.addDialog(EntityType.SHEEP, 2, "咩—");//E
        generator.addDialogChoice(EntityType.SHEEP, 3, "咩— 咩———");//T
        generator.addDialog(EntityType.SHEEP, 3, "咩—咩———咩—咩—");//L
        generator.addDialogChoice(EntityType.SHEEP, 4, "咩—咩—咩———");//U
        generator.addDialog(EntityType.SHEEP, 4, "咩—咩———咩—咩—");//L
        generator.addDialogChoice(EntityType.SHEEP, 5, "咩—咩———咩———咩—");//P
        generator.addDialog(EntityType.SHEEP, 5, "咩———咩———咩———");//O
        generator.addDialogChoice(EntityType.SHEEP, 6, "咩—   ");//E
        generator.addDialog(EntityType.SHEEP, 6, "咩——— 咩— 咩——— 咩— 咩——— 咩———");//!
        generator.addDialogChoice(EntityType.SHEEP, 7, "咩———咩—咩—");//D
    }

}
