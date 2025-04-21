package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

/**
 * TODO 宠物抽卡 / 玩偶抽卡
 * 宠物抽卡：直接召唤随机生物，命名为 XXX 的 XXX，外送一根栓绳和一个命名牌。能Tame则Tame
 * 购买玩偶抽奖机：直接购买一个盒子。
 */
public class ShepherdDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ShepherdDialogBuilder() {
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
        generator.addVillagerAns(this.profession, 0, "（你愿意花费辛苦赚来的绿宝石，为枯燥的营业生活添加点乐趣吗？）");
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
