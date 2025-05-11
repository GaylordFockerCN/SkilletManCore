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

public class FishmanDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public FishmanDialogBuilder() {
        super(VillagerProfession.FISHERMAN);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if (interactionID == 3) {
            ItemUtil.tryAddRandomItem(serverPlayer, StartNPC.SEAFOOD_SET, 50000, 220);
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
        generator.addVillagerName(this.profession, " §b友善的渔夫§r ");
        generator.addVillagerAns(this.profession, 0, "（授人以鱼不如卖人以鱼，有了渔夫就不用下海抓食材咯）");
        generator.addVillagerOpt(this.profession, 0, "购买");
        generator.addVillagerOpt(this.profession, 1, "离开");
        generator.addVillagerAns(this.profession, 1, "恭喜你发现了本整合包提前获取海鲜的办法！正常需要等到阶段2才可在订购里解锁！要来点什么呢？");
        generator.addVillagerOpt(this.profession, 2, "怎么还是大礼包？");
        generator.addVillagerAns(this.profession, 2, "是这样的，本整合包无处不在的抽卡系统。但是我这儿能买到的种类可比订购到的多多了！");
        generator.addVillagerOpt(this.profession, 3, "海鲜超级大礼包 §a50000 绿宝石");
    }


}
