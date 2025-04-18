package com.p1nero.smc.entity.custom.npc.customer.customer_data.normal;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.NormalCustomerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;

public class NormalCustomerData6 extends NormalCustomerData {

    public NormalCustomerData6() {
        super(6);
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "非常普通的客户");
        generator.add(answerPre(-1), "（客户看着你，投来疑惑的眼神。看来不是所有村民都是绿袍尊者，请提交正确的菜品）");
        generator.add(choicePre(-1), "好吧");
        generator.add(answerPre(0), "哈哈，又是我！来一份 %s ！作者只做了6种普通村民，但是却有20种特殊客户，所以遇到我的概率还是蛮高的。期待后续版本作者添加更加丰富的不同的村民吧。");//顾客的请求，一定要保留%s，不能带有具体食物，只能带修饰词。
        generator.add(choicePre(0), "交之");//玩家的提交选项
        generator.add(answerPre(1), "不赖不赖");//高品质的时候顾客的回答。赠礼可以是秘笈或神兵
        generator.add(choicePre(1), "谢之");//高品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(2), "一般");//中品质的时候顾客的回答
        generator.add(choicePre(2), "谢之");//中品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(3), "难吃");//低品质的时候顾客的回答
        generator.add(choicePre(3), "怎么这么直白。。");//低品质的时候顾客回答后 的玩家选项
    }

    @Override
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodLevel) {
        TreeNode root;

        if (!canSubmit) {
            root = new TreeNode(answer(0, I18n.get(serverData.getString("food_name"))))
                    .addChild(new TreeNode(answer(-1), choice(0))
                            .addLeaf(choice(-1), (byte) -3));
        } else {
            root = switch (foodLevel) {
                case BEST -> new TreeNode(answer(0, I18n.get(serverData.getString("food_name"))))
                        .execute(SUBMIT_FOOD)
                        .addChild(new TreeNode(answer(1), choice(0))
                                .addLeaf(choice(1), BEST));
                case MIDDLE -> new TreeNode(answer(0, I18n.get(serverData.getString("food_name"))))
                        .addChild(new TreeNode(answer(2), choice(0))
                                .addLeaf(choice(2), MIDDLE));
                default -> new TreeNode(answer(0, I18n.get(serverData.getString("food_name"))))
                        .addChild(new TreeNode(answer(3), choice(0))
                                .addLeaf(choice(3), BAD));
            };
        }
        screenBuilder.setAnswerRoot(root);
    }

}
