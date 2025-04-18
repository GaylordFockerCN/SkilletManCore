package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import reascer.wom.world.item.WOMItems;

public class SpecialCustomerData18 extends SpecialCustomerData {

    public SpecialCustomerData18() {
        super(18);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "铸剑大师的客户");
        generator.add(answerPre(-2), "（铁锤砸出火星）凡铁也配入我剑庐？");
        generator.add(choicePre(-2), "掩耳退避");
        generator.add(answerPre(-1), "（独眼罩反射着剑胚红光）");
        generator.add(choicePre(-1), "宗师要试剑粮？");
        generator.add(answerPre(0), "淬火需%s——要如干将泣血般炽烈的！（为什么淬火还需要食物...此刻你不得不怀疑作者较劲了脑汁，已经开始瞎写文案来凑数了）");
        generator.add(choicePre(0), "拉动巨型风箱");
        generator.add(answerPre(1), "（剑鸣穿云）好个焚天热意！这柄断龙匕赠你切肉！（虽然50级才能在战斗模式下使用非平底锅武器，但是还是先留着吧）");
        generator.add(choicePre(1), "接下神兵");
        generator.add(answerPre(2), "滋味一般。");
        generator.add(choicePre(2), "捶胸送客");
        generator.add(answerPre(3), "（钳碎铁块）连马蹄铁都不如！");
        generator.add(choicePre(3), "试图跳入水槽避险");
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
        ItemUtil.addItem(serverPlayer, WOMItems.MOONLESS.get().getDefaultInstance());
    }

}
