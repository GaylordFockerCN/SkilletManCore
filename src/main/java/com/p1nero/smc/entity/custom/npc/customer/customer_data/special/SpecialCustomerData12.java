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

public class SpecialCustomerData12 extends SpecialCustomerData {

    public SpecialCustomerData12() {
        super(12);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "病弱的客户");
        generator.add(answerPre(-2), "（剧烈咳嗽）咳咳...这...这不是《食经》记载之物...");
        generator.add(choicePre(-2), "轻拍后背顺气");
        generator.add(answerPre(-1), "（青白手指正颤抖着翻阅药膳典籍）");
        generator.add(choicePre(-1), "公子需要药引调理？");
        generator.add(answerPre(0), "按岐黄之术...需以%s佐三钱晨露煎服... （这真的是治病的吗= =）");
        generator.add(choicePre(0), "呈上“药”");
        generator.add(answerPre(1), "（面现红晕）此物正合阴阳五行！这方祖传药赠予你！");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（吐血染红绢帕）火候...有违...《本草纲目》...");
        generator.add(choicePre(2), "低头送客");
        generator.add(answerPre(3), "（突然昏厥）庸医...害命...");
        generator.add(choicePre(3), "是不是该去请大夫...");
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
        ItemUtil.addItem(serverPlayer, Items.ENCHANTED_GOLDEN_APPLE, 32);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }

}
