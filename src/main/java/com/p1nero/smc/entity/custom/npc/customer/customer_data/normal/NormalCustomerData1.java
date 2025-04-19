package com.p1nero.smc.entity.custom.npc.customer.customer_data.normal;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.NormalCustomerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;

public class NormalCustomerData1 extends NormalCustomerData {

    public NormalCustomerData1() {
        super(1);
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "非常普通的村民");
        generator.add(answerPre(-1), "（的村民看着你，投来疑惑的眼神。看来不是所有村民都是绿袍尊者，请提交正确的菜品）");
        generator.add(choicePre(-1), "好吧");
        generator.add(choicePre(-2), "离开");
        generator.add(answerPre(0), "%s？（客户向你发出请求，但作者已经懒得花时间在普通客户上写文案了）");//顾客的请求，一定要保留%s，不能带有具体食物，只能带修饰词。
        generator.add(choicePre(0), "交之");//玩家的提交选项
        generator.add(answerPre(1), "？！");//高品质的时候顾客的回答。赠礼可以是秘笈或神兵
        generator.add(choicePre(1), "再见~");//高品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(2), "。。");//中品质的时候顾客的回答
        generator.add(choicePre(2), "有那么无语吗...");//中品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(3), "§k# % &@*U*.. §r （或许这是在谩骂吧）");//低品质的时候顾客的回答
        generator.add(choicePre(3), "？？？有那么难吃吗（糊成这样你心里没数吗= =）");//低品质的时候顾客回答后 的玩家选项
    }

}
