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
import yesman.epicfight.gameasset.EpicFightSounds;

public class SpecialCustomerData4 extends SpecialCustomerData {

    public SpecialCustomerData4() {
        super(4);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "异域的客户");
        generator.add(answerPre(-2), "（摸着弯刀柄微笑）中原人总是搞不清饭前点心与正餐的区别");
        generator.add(choicePre(-2), "行抚胸礼");
        generator.add(answerPre(-1), "（琉璃盏中的香料正冒出蛇形青烟）");
        generator.add(choicePre(-1), "远方来的贵客想要什么珍馐？");
        generator.add(answerPre(0), "以真主之名，可有散发着沙漠热风的%s？");
        generator.add(choicePre(0), "撒入金色香料");
        generator.add(answerPre(1), "（展开镶金边的卷轴）此物当记录在《东方见闻录》中，这 神兵 便赠与你作为见证吧");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（皱眉）驼铃之城三岁孩童做得比这好");
        generator.add(choicePre(2), "低头送客");
        generator.add(answerPre(3), "（突然用陌生语言咒骂）卡菲勒！");
        generator.add(choicePre(3), "低头送客");
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
        ItemUtil.addItem(serverPlayer, SMCItems.WEAPON_RAFFLE_TICKET.get().getDefaultInstance());

    }

}
