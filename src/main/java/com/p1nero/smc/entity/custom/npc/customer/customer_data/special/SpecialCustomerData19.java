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
import reascer.wom.world.item.WOMItems;

public class SpecialCustomerData19 extends SpecialCustomerData {

    public SpecialCustomerData19() {
        super(19);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "丐帮长老的客户");
        generator.add(answerPre(-2), "（打狗棒横挡柜台）叫花子也分得清残羹剩饭！");
        generator.add(choicePre(-2), "抱棍退避");
        generator.add(answerPre(-1), "（九袋破袍上油光发亮）");
        generator.add(choicePre(-1), "长老要验验新乞食？");
        generator.add(answerPre(0), "降龙掌练到紧要处，需带刚猛气的%s！");
        generator.add(choicePre(0), "掀开泥壳");
        generator.add(answerPre(1), "（龙吟隐隐）痛快！这秘笈与神棍予你防身");
        generator.add(choicePre(1), "褡裢收钱");
        generator.add(answerPre(2), "（敲碎陶碗）比马厩草料还糙");
        generator.add(choicePre(2), "打躬送客");
        generator.add(answerPre(3), "（突然摆出打狗阵）欺我丐帮无人？！");
        generator.add(choicePre(3), "饶命！饶命！");
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
        ItemUtil.addItem(serverPlayer, SMCItems.SKILL_BOOK_RAFFLE_TICKET.get().getDefaultInstance());
        ItemUtil.addItem(serverPlayer, SMCItems.WEAPON_RAFFLE_TICKET.get().getDefaultInstance());
    }

}
