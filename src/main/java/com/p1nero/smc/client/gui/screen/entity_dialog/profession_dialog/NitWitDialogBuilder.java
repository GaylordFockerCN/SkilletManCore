package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NitWitDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public NitWitDialogBuilder() {
        super(VillagerProfession.NITWIT);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        builder.setAnswerRoot(new TreeNode(answer(0))
                .addChild(new TreeNode(answer(1), choice(0))
                        .addLeaf(choice(-1), (byte) -1))
                .addChild(new TreeNode(answer(2), choice(1))
                        .addLeaf(choice(-1), (byte) -1))
                .addChild(new TreeNode(answer(3), choice(2))
                        .addLeaf(choice(-1), (byte) -1)));
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §a绿袍尊者§r ");
        generator.addVillagerOpt(this.profession, -1, "结束对话");
        generator.addVillagerAns(this.profession, 0, "（绿袍尊者眼里充满了智慧，在这个整合包里你获得了与村民对话的能力，因此你迫不及待地想和他对话）");
        generator.addVillagerOpt(this.profession, 0, "你存在的意义是什么？");
        generator.addVillagerAns(this.profession, 1, "先搞清楚你是谁，你从哪里来，到哪里去，再来问我吧。（这样一想，我们好像没有资格询问他存在的意义，就像我们无法弄明白宇宙的秘密，说不定我们每个人都像你眼前的村民一般，只是高维文明制作的游戏呢。）");
        generator.addVillagerOpt(this.profession, 1, "Mojang是不是忘了给你写交易代码？");
        generator.addVillagerAns(this.profession, 2, "他们说这是“特色”，就像洞穴更新前的地下岩浆湖。");
        generator.addVillagerOpt(this.profession, 2, "你真的是村子的守护者吗？");
        generator.addVillagerAns(this.profession, 3, "阿巴，阿巴阿巴。其实作者本来是想给我上一个在夜里可以攻击敌对生物的功能的，但是怕我喧宾夺主，抢了平底锅侠的风头，于是便放弃了，诶嘿嘿");
    }


}
