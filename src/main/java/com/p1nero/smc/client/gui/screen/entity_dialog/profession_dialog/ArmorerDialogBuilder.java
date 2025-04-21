package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

/**
 * 盔甲抽卡
 * 只能抽取煎锅侠面罩
 */
public class ArmorerDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ArmorerDialogBuilder() {
        super(VillagerProfession.ARMORER);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if (interactionID == 3) {
            ItemUtil.tryAddIngredient(serverPlayer, StartNPC.MEAT_SET, 10000, 120);
        }
    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        TreeNode root = new TreeNode(answer(0));
        TreeNode buy = new TreeNode(answer(1), choice(0));
        root.addChild(buy);
        root.addLeaf(choice(1));
        buy.addChild(new TreeNode(answer(2), choice(2))
                        .addChild(buy
                                .addLeaf(choice(1))))
                .addLeaf(choice(3, (byte) 3));

        builder.setAnswerRoot(root);
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, "传奇盔甲匠");
        generator.addVillagerAns(this.profession, 0, "（是看来）");
        generator.addVillagerOpt(this.profession, 0, "购买");
        generator.addVillagerOpt(this.profession, 1, "离开");
        generator.addVillagerAns(this.profession, 1, "少侠来点什么？（看来交易并没有被取代，而是换成了繁琐的对话）");
        generator.addVillagerOpt(this.profession, 2, "怎么还是大礼包？");
        generator.addVillagerAns(this.profession, 2, "是这样的，本整合包无处不在的抽卡系统。");
        generator.addVillagerOpt(this.profession, 3, "盔甲小礼包 - §a160 绿宝石");
        generator.addVillagerOpt(this.profession, 4, "盔甲小礼包 - §a1600 绿宝石");
    }


}
