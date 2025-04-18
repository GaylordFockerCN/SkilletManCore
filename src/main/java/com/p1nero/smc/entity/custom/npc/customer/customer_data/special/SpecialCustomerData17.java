package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;

public class SpecialCustomerData17 extends SpecialCustomerData {

    public SpecialCustomerData17() {
        super(17);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "流浪的客户");
        generator.add(answerPre(-2), "（鹅毛笔突然折断）这与《神曲》的韵律毫不相称！");
        generator.add(choicePre(-2), "拾稿告退");
        generator.add(answerPre(-1), "（破斗篷上沾着未干的墨水）");
        generator.add(choicePre(-1), "阁下需要灵感源泉？");
        generator.add(answerPre(0), "新诗篇需要%s——要像维纳斯诞生般神圣的！");
        generator.add(choicePre(0), "用金箔纸包裹呈递");
        generator.add(answerPre(1), "（泪染羊皮卷）这滋味让缪斯降临！这杆雅典娜羽毛笔赠你！（但你还是没能看出这书与笔有什么奇特的地方）");
        generator.add(choicePre(1), "躬身接笔");
        generator.add(answerPre(2), "（撕碎诗稿）比酒馆醉汉的呓语还拙劣");
        generator.add(choicePre(2), "折纸送客");
        generator.add(answerPre(3), "（突然焚烧手稿）你在亵渎诗歌艺术！");
        generator.add(choicePre(3), "抱头冲出阁楼");
    }

    @Override
    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        if (!canSubmit) {
            root.addChild(new TreeNode(answer(0), choice(-1))
                    .addChild(new TreeNode(answer(-2, serverData.get("food_name")), choice(0))
                            .addLeaf(choice(-2), (byte) -3)));
        } else {
            switch (foodScore) {
                case BEST:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(1), choice(0))
                                    .addLeaf(choice(1), BEST)));
                    break;
                case MIDDLE:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(2), choice(0))
                                    .addLeaf(choice(2), MIDDLE)));
                    break;
                default:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(3), choice(0))
                                    .addLeaf(choice(3), BAD)));
            }
        }
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Items.WRITABLE_BOOK.getDefaultInstance());
    }

}
