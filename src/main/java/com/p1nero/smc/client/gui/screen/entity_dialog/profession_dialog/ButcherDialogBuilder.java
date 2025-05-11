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

public class ButcherDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ButcherDialogBuilder() {
        super(VillagerProfession.BUTCHER);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if (interactionID == 3) {
            ItemUtil.tryAddRandomItem(serverPlayer, StartNPC.MEAT_SET, 10000, 120);
        }
    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        TreeNode root = new TreeNode(answer(0));
        TreeNode buy = new TreeNode(answer(1), choice(0));
        buy.addChild(new TreeNode(answer(2), choice(2))
                        .addChild(buy))
                .addLeaf(choice(3), (byte) 3)
                .addLeaf(choice(1));
        root.addChild(buy);
        root.addLeaf(choice(1));

        builder.setAnswerRoot(root);
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §c魁梧的屠夫§r ");
        generator.addVillagerAns(this.profession, 0, "（杀猪宰羊不在话下，或许在这儿可以买到更好的肉？）");
        generator.addVillagerOpt(this.profession, 0, "购买");
        generator.addVillagerOpt(this.profession, 1, "离开");
        generator.addVillagerAns(this.profession, 1, "少侠来点什么？");
        generator.addVillagerOpt(this.profession, 2, "怎么还是大礼包？");
        generator.addVillagerAns(this.profession, 2, "是这样的，本整合包无处不在的抽卡系统。但是我这儿能抽到的肉类的种类比订购到的多多了！");
        generator.addVillagerOpt(this.profession, 3, "肉类超级大礼包 §a10000 绿宝石");
    }


}
