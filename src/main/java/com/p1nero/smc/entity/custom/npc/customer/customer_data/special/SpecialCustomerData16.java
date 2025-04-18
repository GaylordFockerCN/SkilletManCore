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

public class SpecialCustomerData16 extends SpecialCustomerData {

    public SpecialCustomerData16() {
        super(16);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "说书先生模样的客户");
        generator.add(answerPre(-2), "（惊堂木震落灰尘）这桥段早被《江湖异闻录》写烂了！");
        generator.add(choicePre(-2), "拱手退场");
        generator.add(answerPre(-1), "（折扇上墨迹未干的《十二楼台》新章）");
        generator.add(choicePre(-1), "先生要润喉茶点？");
        generator.add(answerPre(0), "下回分解需佐%s——要有侠客夜行的意境！");
        generator.add(choicePre(0), "以剑风切片");
        generator.add(answerPre(1), "（抚掌大笑）此味当入武林野史！这残卷《葵花录》赠你");
        generator.add(choicePre(1), "郑重收下");
        generator.add(answerPre(2), "（撕稿纸）比村口货郎的故事还寡淡");
        generator.add(choicePre(2), "躬身送客");
        generator.add(answerPre(3), "（突然摔碎茶碗）当我三岁小儿糊弄？！");
        generator.add(choicePre(3), "疾退三步闭柜");
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
    }

}
