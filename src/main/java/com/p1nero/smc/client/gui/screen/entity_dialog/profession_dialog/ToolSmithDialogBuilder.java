package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

/**
 * TODO 碟片抽卡
 */
public class ToolSmithDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ToolSmithDialogBuilder() {
        super(VillagerProfession.SHEPHERD);
    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        builder.setAnswerRoot(new TreeNode(answer(0))
                .addChild(new TreeNode(answer(1), choice(0))
                        .addLeaf(choice(1), (byte) -1))
                        .addChild(new TreeNode(answer(2), choice(2)))
                .addLeaf(choice(-1), (byte) -1));
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, "平凡的牧羊人");
        generator.addVillagerOpt(this.profession, -1, "离去");
        generator.addVillagerAns(this.profession, 0, "（图书管理员今日无事，正在闲逛。  要不找他问问有没有卖附魔书吧，刚好给我的锅和铲升升级！）");
        generator.addVillagerOpt(this.profession, 0, "购买附魔书");
        generator.addVillagerAns(this.profession, 1, "小子，你不会以为我真的会卖给你附魔书吧？你是不是对作者设计的五合一升级系统和抽卡系统有意见？还企图通过附魔这种旁门左道提升实力？！");
        generator.addVillagerOpt(this.profession, 1, "好嘛");
        generator.addVillagerOpt(this.profession, 2, "抽取技能书");
        generator.addVillagerAns(this.profession, 2, "所有技能书概率均等，童叟无欺！抽10次必出奇迹武器技能书！");
        generator.addVillagerOpt(this.profession, 3, "抽 1 次 §a160 绿宝石");
        generator.addVillagerOpt(this.profession, 4, "抽 10 次 §a1599 绿宝石");
        generator.addVillagerOpt(this.profession, 5, "使用技能书抽奖券");
    }



}
