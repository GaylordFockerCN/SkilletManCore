package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CatDialogScreenHandler {

    public static final byte TAME = 1;
    public static final byte SIT_DOWN = 2;
    public static final byte SIT_UP = 3;
    public static final byte LYE_DOWN = 4;
    public static final byte PET = 5;
    public static final byte KILL = 6;

    public static void handle(ServerPlayer serverPlayer, Cat cat, byte interactId) {
        if(interactId == KILL) {
            SMCCapabilityProvider.getSMCPlayer(serverPlayer).consumeMorality();
        }
        if(interactId > 0 && interactId <= 5) {
            cat.setInLove(serverPlayer);
            if(interactId == TAME) {
                cat.tame(serverPlayer);
                ItemUtil.addItem(serverPlayer, Items.NAME_TAG.getDefaultInstance(), true);
                ItemUtil.addItem(serverPlayer, Items.LEAD.getDefaultInstance(), true);
            } else if(interactId == SIT_DOWN) {
                cat.setOrderedToSit(true);
            } else if(interactId == SIT_UP) {
                cat.setOrderedToSit(false);
                cat.setLying(false);
            } else if(interactId == LYE_DOWN) {
                cat.setLying(true);
                cat.setRelaxStateOne(false);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Cat cat) {
        DialogueComponentBuilder builder = new DialogueComponentBuilder(cat);
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(cat);
        TreeNode root;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (cat.getOwner() == localPlayer) {
            root = new TreeNode(builder.ans(3), builder.opt(3));
            root.addChild(new TreeNode(builder.ans(4), builder.opt(5))
                    .execute(SIT_DOWN)
                    .addChild(root));
            root.addChild(new TreeNode(builder.ans(5), builder.opt(6))
                    .execute(SIT_UP)
                    .addChild(root));
            root.addChild(new TreeNode(builder.ans(6), builder.opt(7))
                    .execute(LYE_DOWN)
                    .addChild(root));
            root.addChild(new TreeNode(builder.ans(7), builder.opt(8))
                    .execute(PET)
                    .addChild(root));
        } else {
            root = new TreeNode(builder.ans(0), builder.opt(3))
                    .addChild(new TreeNode(builder.ans(1), builder.opt(0))
                            .execute(TAME)
                            .addLeaf(builder.opt(4)));
            root.addChild(new TreeNode(builder.ans(2), builder.opt(1))
                            .execute(KILL)
                            .addChild(root));
        }
        screenBuilder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialogEntityName(EntityType.CAT, "可爱的猫猫");
        generator.addDialog(EntityType.CAT, 0, "喵~（可爱的猫猫对你充满了好奇）");
        generator.addDialogChoice(EntityType.CAT, 0, "抚摸之");
        generator.addDialogChoice(EntityType.CAT, 1, "§l§4杀之");
        generator.addDialogChoice(EntityType.CAT, 2, "离开");
        generator.addDialog(EntityType.CAT, 1, "喵呜~（你成功收服了猫猫！）");
        generator.addDialog(EntityType.CAT, 2, "你不能这么做喵");
        generator.addDialogChoice(EntityType.CAT, 3, "返回");
        generator.addDialogChoice(EntityType.CAT, 4, "这么好驯服的吗！！");
        generator.addDialog(EntityType.CAT, 3, "老大，接下来要干什么喵？");
        generator.addDialog(EntityType.CAT, 4, "老大，我坐下了喵");
        generator.addDialog(EntityType.CAT, 5, "老大，我起立了喵");
        generator.addDialog(EntityType.CAT, 6, "老大，我躺下了喵");
        generator.addDialog(EntityType.CAT, 7, "老大，好舒服喵");
        generator.addDialogChoice(EntityType.CAT, 5, "坐下");
        generator.addDialogChoice(EntityType.CAT, 6, "起立");
        generator.addDialogChoice(EntityType.CAT, 7, "躺下");
        generator.addDialogChoice(EntityType.CAT, 8, "抚摸之");
    }
}
