package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

public class BabyVillagerDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public BabyVillagerDialogBuilder() {
        super(VillagerProfession.NONE);
    }

    @Override
    protected String getTranslationKey() {
        return "baby";
    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        builder.setAnswerRoot(new TreeNode(answer(0))
                .addChild(new TreeNode(answer(1), choice(0))
                        .addLeaf(choice(1)))
                .addLeaf(choice(1)));
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.getTranslationKey(), " §6谁家的小孩§r ");
        generator.addVillagerAns(this.getTranslationKey(), 0, "（他还只是个孩子，或许多个系统时以后他也会获得工作，成为芸芸众生，成为你通关的工具，并且再也不会是孩子。）");
        generator.addVillagerOpt(this.getTranslationKey(), 0, "小朋友，不要等到失去了才懂得珍惜！");
        generator.addVillagerOpt(this.getTranslationKey(), 1, "离开");
        generator.addVillagerAns(this.getTranslationKey(), 1, "（小孩向你投来疑惑的目光）");

    }
}
