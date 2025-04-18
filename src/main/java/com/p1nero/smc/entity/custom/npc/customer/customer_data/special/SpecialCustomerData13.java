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

public class SpecialCustomerData13 extends SpecialCustomerData {

    public SpecialCustomerData13() {
        super(13);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "走镖的客户");
        generator.add(answerPre(-2), "（红缨枪往地上一顿）道上规矩，非镖物不接！");
        generator.add(choicePre(-2), "抱拳退下");
        generator.add(answerPre(-1), "（牛皮护腕上还带着狼牙划痕）");
        generator.add(choicePre(-1), "镖头要补些硬菜？");
        generator.add(answerPre(0), "这趟暗镖凶险，需能壮胆的%s压阵！");
        generator.add(choicePre(0), "拍开酒坛泥封");
        generator.add(answerPre(1), "（掷出飞镖钉中梁柱）痛快！这金丝软甲赠你防身！");
        generator.add(choicePre(1), "躬身接过");
        generator.add(answerPre(2), "（摇头）比黑风寨的伙食还软三分");
        generator.add(choicePre(2), "抱拳送客");
        generator.add(answerPre(3), "（突然吹响警哨）莫非是劫镖的探子？！");
        generator.add(choicePre(3), "连声道歉闭门");
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
        ItemUtil.addItem(serverPlayer, Items.GOLDEN_HELMET, 1);
        ItemUtil.addItem(serverPlayer, Items.GOLDEN_CHESTPLATE, 1);
        ItemUtil.addItem(serverPlayer, Items.GOLDEN_LEGGINGS, 1);
        ItemUtil.addItem(serverPlayer, Items.GOLDEN_BOOTS, 1);
    }

}
