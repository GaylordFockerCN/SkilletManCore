package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.util.SMCRaidManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;

import java.util.Objects;

/**
 * 主线!
 */
public class ClericDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ClericDialogBuilder() {
        super(VillagerProfession.CLERIC);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if(interactionID == 1) {
            SMCRaidManager.startTrial(serverPlayer, smcPlayer);
        }

        if(interactionID == 2) {
            if(smcPlayer.getStage() <= 1) {
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("game_time_no_enough"), true);
            } else {
                ServerLevel end = serverPlayer.serverLevel().getServer().getLevel(Level.END);
                if(end != null){
                    serverPlayer.changeDimension(end);
                }
            }
        }

    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) {
            return;
        }

//        if(击败过boss){
//
//        }

        TreeNode directToEnd = new TreeNode(answer(4), choice(1))
                .addLeaf(choice(8), (byte) 2)
                .addLeaf(choice(7));
        if(SMCCapabilityProvider.getSMCPlayer(localPlayer).isTrialRequired()) {
            builder.setAnswerRoot(new TreeNode(answer(3))
                    .addChild(new TreeNode(answer(5), choice(6))
                            .addLeaf(choice(8), (byte) 1)
                            .addLeaf(choice(7)))
                    .addChild(directToEnd)
                    .addLeaf(choice(2)));
        } else {
            builder.setAnswerRoot(new TreeNode(answer(0))
                    .addChild(new TreeNode(answer(1), choice(0))
                            .addLeaf(choice(3)))
                    .addChild(directToEnd)
                    .addLeaf(choice(2)));
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §b曾经凝视过§c『终界』§r§b的牧师§r ");
        generator.addVillagerAns(this.profession, 0, "（曾经去过§c『终界』§r的牧师目光十分深邃）  如果你觉得你已经足够强大，可以找我询问更多终界相关的信息。");
        generator.addVillagerOpt(this.profession, 0, "终界是什么？");
        generator.addVillagerOpt(this.profession, 1, "我可以直接打最终boss吗？");
        generator.addVillagerOpt(this.profession, 2, "离开");
        generator.addVillagerOpt(this.profession, 3, "结束对话");

        generator.addVillagerAns(this.profession, 1, "§c『终界』§r是这个世界的邪恶力量，夜晚的袭击便来自他们。  而只有解放了§c『终界』§r，我们才能获得永恒的和平");
        generator.addVillagerOpt(this.profession, 4, "你在说谎，对么？");//TODO 解放末地后回来对话解锁成就 true_end
        generator.addVillagerAns(this.profession, 2, "？！（牧师感到震惊，看来他知道你在§c『终界』§r找到了答案。 什么，你说你没看懂？你是不是跳过了通关动画？）");
        generator.addVillagerOpt(this.profession, 5, "但事已至此，再见吧。");

        generator.addVillagerAns(this.profession, 3, "（在这些日子里，你的力量逐渐恢复，于是找到牧师询问敌人的信息）不，不！作为蹭误入§c『终界』§r的我可以明确的告诉你，远远不够！终界的魔物日益猖狂，若你能通过试炼，我便相信你有击败恶龙的力量，并愿意赞助你的店铺。");

        generator.addVillagerOpt(this.profession, 6, "进行突破试炼");
        generator.addVillagerOpt(this.profession, 7, "再等等");
        generator.addVillagerAns(this.profession, 4, "你确定要直接挑战最终boss吗？我可以将你传送至终界，但无法将你送回来…");
        generator.addVillagerAns(this.profession, 5, "确定要进行突破试炼吗？");
        generator.addVillagerOpt(this.profession, 8, "确定");

    }


}
